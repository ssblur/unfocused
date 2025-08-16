package com.ssblur.unfocused.helper

import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation
import java.net.URI

object MarkdownFormatter {
  data class PageHolder(
    val component: Component,
    val images: List<ResourceLocation>,
    val recipes: List<ResourceLocation>,
    val items: List<ResourceLocation>,
  )

  fun append(base: MutableComponent, text: String, bold: Boolean, italic: Boolean, strikethrough: Boolean): MutableComponent {
    return base.append(
      Component.literal(text).withStyle(
        Style.EMPTY
          .withBold(bold)
          .withItalic(italic)
          .withStrikethrough(strikethrough)
      )
    )
  }

  fun parseMarkdown(markdown: String): PageHolder {
    var element = Component.literal("")
    val lines = markdown.split("\n").map { it.trim() }
    lines.forEach {
      var line = it
      var current = ""

      // Could nest but this saves memory and is easy enough to follow so building a flat list instead
      var isBold = false
      var isItalic = false
      var isStrikeThrough = false
      val isTitle = line.startsWith('#')
      if(isTitle) line = line.substring(1)

      if(line.isEmpty())
        element = element.append(Component.literal("\n"))

      while(line.isNotEmpty()) {
        if(line.startsWith("***")) {
          element = append(element, current, isBold, isItalic, isStrikeThrough)
          current = ""
          isBold = !isBold
          isItalic = !isItalic
          line = line.substring(3)
          continue
        } else if(line.startsWith("**")) {
          element = append(element, current, isBold, isItalic, isStrikeThrough)
          current = ""
          isBold = !isBold
          line = line.substring(2)
          continue
        } else if(line.startsWith("*")) {
          element = append(element, current, isBold, isItalic, isStrikeThrough)
          current = ""
          isItalic = !isItalic
          line = line.substring(1)
          continue
        } else if(line.startsWith("~~")) {
          element = append(element, current, isBold, isItalic, isStrikeThrough)
          current = ""
          isStrikeThrough = !isStrikeThrough
          line = line.substring(2)
          continue
        } else if(line.matches("^\\[(.*?)]\\((.*?)\\)".toRegex())) {
          val match = "^\\[(.*?)]\\((.*?)\\)(.*)".toRegex().matchEntire(line)!!
          val text = match.groups[0]!!.value
          val link = match.groups[1]!!.value
          val isExternal = link.startsWith("http://") || link.startsWith("https://")
          val remainder = match.groups[2]!!.value
          element = element.append(
            Component.literal(text).withStyle(
              Style.EMPTY
                .withBold(isBold)
                .withItalic(isItalic)
                .withStrikethrough(isStrikeThrough)
                .withUnderlined(true)
                .withClickEvent(
                  if(isExternal)
                    ClickEvent.OpenUrl(URI(link))
                        else
                    ClickEvent.RunCommand("unfocused open $link")
                )
            )
          )
          line = remainder
          continue
        }
        // TODO: recognize images and item / recipe embeds

        current += line[0]
        line = line.substring(1)
      }
      if(current.isNotEmpty())
        element = append(element, current, isBold, isItalic, isStrikeThrough)
    }

    return PageHolder(element, listOf(), listOf(), listOf())
  }

  fun parseMarkdownWithoutImages(markdown: String) = parseMarkdown(markdown).component
}