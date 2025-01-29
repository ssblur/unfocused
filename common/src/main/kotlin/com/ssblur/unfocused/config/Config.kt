package com.ssblur.unfocused.config

import com.ssblur.unfocused.UtilityExpectPlatform
import com.ssblur.unfocused.event.common.ServerStartEvent
import java.nio.charset.Charset
import kotlin.io.path.exists
import kotlin.io.path.reader
import kotlin.io.path.writer

class Config(val id: String, val delimeter: String = "=", val topComment: String? = null) {
    init {
        ServerStartEvent.register{
            it.addTickable{ if(dirty) save() }
        }
    }

    private val values: MutableMap<String, String> = mutableMapOf()
    private val comments: MutableMap<String, List<String>> = mutableMapOf()
    private var dirty = false
    private var loaded = false

    operator fun set(key: String, value: String) {
        if(!loaded) load()
        values[key] = value
        dirty = true
    }

    operator fun get(key: String): String? {
        return values[key]
    }

    fun get(key: String, default: String, vararg comment: String): String {
        if(!loaded) load()
        if(values[key] == null) {
            set(key, default)
            if(comment.isEmpty())
                comments[key] = listOf("Default value for '$id:$key' gamerule")
            else
                comments[key] = comment.toList()
        }
        return values[key]!!
    }

    fun save() {
        if(!loaded) load()
        dirty = false
        val dir = UtilityExpectPlatform.configDir()
        val file = dir.resolve("$id.cfg")
        val writer = file.writer(Charset.defaultCharset())
        if(topComment != null) {
            writer.write("## Config file for $id\n")
            writer.write("## These are default values for gamerules.\n")
            writer.write("## If your world has already been created, you can change most of them\n")
            writer.write("## using '/gamerule $id:[var]'\n\n\n")
        }
        for(line in values.entries) {
            for(comment in comments[line.key] ?: listOf())
                writer.write("## $comment\n")
            writer.write(line.key)
            writer.write(delimeter)
            writer.write(line.value.replace("\n", "\n  "))
            writer.write("\n")
        }
        writer.close()
    }

    fun load() {
        if(loaded) return
        dirty = false
        val dir = UtilityExpectPlatform.configDir()
        val file = dir.resolve("$id.cfg")
        if(!file.exists()) {
            loaded = true
            save()
            return
        }
        val reader = file.reader(Charset.defaultCharset())
        var value = ""
        var key = ""
        var comment = mutableListOf<String>()
        reader.forEachLine {
            if(it.startsWith("##")) {
                comment += it.substring(2).trimStart()
            }
            if("^\\s+(.*)".toRegex().matches(it)) {
                "^\\s+(.*)".toRegex().find(it)?.let { match -> value += match.groups[1]?.value + "\n" }
            } else {
                if(key.isNotEmpty()) {
                    values[key] = value
                    comments[key] = comment
                    comment = mutableListOf()
                }
                val split = it.split(delimeter.toRegex(), 2)
                if(split.size >= 2) {
                    key = split[0].trim()
                    value = split[1].trim()
                } else comment += ""
            }
        }
        if(key.isNotEmpty()){
            values[key] = value
            comments[key] = comment
        }
        loaded = true
    }
}