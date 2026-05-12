package com.ssblur.unfocused.helper

import net.minecraft.client.Minecraft
import net.minecraft.resources.Identifier
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.jvm.optionals.getOrNull

object LocalizedMarkdownReader {
  fun getStream(location: Identifier, lang: String? = null): InputStream {
    val l = lang ?: Minecraft.getInstance().languageManager.selected
    val manager = Minecraft.getInstance().resourceManager
    return manager.getResource(
      location.withPrefix("unfocused/markdown/$l/").withSuffix(".md")
    ).getOrNull()?.open() ?: manager.getResource(
      location.withPrefix("unfocused/markdown/en_us/").withSuffix(".md")
    ).getOrNull()?.open() ?: InputStream.nullInputStream()
  }

  fun read(location: Identifier, lang: String? = null): String {
    getStream(location, lang).use { stream ->
      BufferedReader(InputStreamReader(stream)).use { reader ->
        return reader.readText()
      }
    }
  }
}