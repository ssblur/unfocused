package com.ssblur.unfocused.helper

import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Locale.getDefault

object LocalizedMarkdownReader {
  fun getStream(location: ResourceLocation, lang: String? = null): InputStream {
    val namespace = location.namespace
    val path = location.path
    val langPath = lang ?: Minecraft.getInstance().languageManager.selected.lowercase(getDefault())
    val stream = javaClass.getResourceAsStream("/assets/$namespace/unfocused/markdown/$langPath/$path.md")
    if(stream != null) return stream
    return javaClass.getResourceAsStream("/assets/$namespace/unfocused/markdown/en_us/$path.md") ?:
      throw FileNotFoundException("Could not find markdown file at " +
          "/assets/$namespace/unfocused/markdown/$langPath/$path.md " +
          "or " +
          "/assets/$namespace/unfocused/markdown/en_us/$path.md"
      )
  }

  fun read(location: ResourceLocation, lang: String? = null): String {
    val stream = getStream(location, lang)
    val reader = BufferedReader(InputStreamReader(stream))
    val string = reader.readText()
    reader.close()
    stream.close()
    return string
  }
}