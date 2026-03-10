package com.ssblur.unfocused.config

import com.ssblur.unfocused.UtilityExpectPlatform
import com.ssblur.unfocused.event.common.ServerStartEvent
import java.nio.charset.Charset
import kotlin.io.path.*

class Config(val id: String, val delimiter: String = "=", val topComment: String? = null) {
  init {
    ServerStartEvent.register {
      it.addTickable { if (dirty) save() }
    }
  }

  private val values: MutableMap<String, String> = mutableMapOf()
  private var dirty = false
  private var loaded = false
  private var defaultComments: MutableMap<String, List<String>> = mutableMapOf()

  operator fun set(key: String, value: String) {
    if (!loaded) load()
    values[key] = value
    dirty = true
  }

  operator fun get(key: String): String? {
    return values[key]
  }

  fun get(key: String, default: String, vararg comment: String): String {
    if (!loaded) load()
    if (values[key] == null) {
      set(key, default)
    }
    defaultComments[key] = comment.toList()
    return values[key]!!
  }

  fun save() {
    if (!loaded) load()
    dirty = false
    val dir = UtilityExpectPlatform.configDir()
    val file = dir.resolve("$id.cfg~")
    val oldFile = dir.resolve("$id.cfg")
    val exists = oldFile.exists()
    val writer = file.writer(Charset.defaultCharset())
    val reader = if(oldFile.exists()) oldFile.reader(Charset.defaultCharset()) else null
    if(!exists)
      if (topComment?.isNotEmpty() == true) {
        writer.write("## Config file for $id\n")
        writer.write("## These are default values for gamerules.\n")
        writer.write("## If your world has already been created, you can change most of them\n")
        writer.write("## using '/gamerule $id:[var]'\n\n\n")
      } else {
        writer.write(topComment ?: "")
      }

    var key: String
    val predefined = mutableListOf<String>()
    reader?.forEachLine {
      val split = it.split(delimiter.toRegex(), 2)
      if (!it.matches("^\\s*##".toRegex()) && split.size >= 2) {
        key = split[0].trim()
        predefined.add(key)
        writer.write("$key$delimiter${values[key]}\n")
      } else {
        writer.write("$it\n")
      }
    }
    for (line in values.entries.filter { !predefined.contains(it.key) }) {
      defaultComments[line.key]?.forEach { writer.write("## $it \n") }

      writer.write(line.key)
      writer.write(delimiter)
      writer.write(line.value.replace("\n", "\n  "))
      writer.write("\n")
    }
    reader?.close()
    writer.close()

    oldFile.deleteIfExists()
    file.moveTo(oldFile)
    file.deleteIfExists()
  }

  fun load() {
    if (loaded) return
    dirty = false
    val dir = UtilityExpectPlatform.configDir()
    val file = dir.resolve("$id.cfg")
    if (!file.exists()) {
      loaded = true
      save()
      return
    }
    val reader = file.reader(Charset.defaultCharset())
    var value = ""
    var key = ""
    reader.forEachLine {
      if ("^\\s+(.*)".toRegex().matches(it)) {
        "^\\s+(.*)".toRegex().find(it)?.let { match -> value += match.groups[1]?.value + "\n" }
      } else {
        if (key.isNotEmpty()) {
          values[key] = value
        }
        val split = it.split(delimiter.toRegex(), 2)
        if (split.size >= 2) {
          key = split[0].trim()
          value = split[1].trim()
        }
      }
    }
    if (key.isNotEmpty()) {
      values[key] = value
    }
    loaded = true
  }
}