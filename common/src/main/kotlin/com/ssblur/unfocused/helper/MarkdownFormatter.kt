package com.ssblur.unfocused.helper

import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.FormattedText
import net.minecraft.network.chat.Style

object MarkdownFormatter {
  fun markdownToFormattedText(markdown: String): FormattedText {
    val elements = mutableListOf<FormattedText>()
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
        elements.add(FormattedText.of("\n"))

      while(line.isNotEmpty()) {
        var changed = false
        if(line.startsWith("***")) {
          isBold = !isBold
          isItalic = !isItalic
          changed = true
          line = line.substring(3)
        } else if(line.startsWith("**")) {
          isBold = !isBold
          changed = true
          line = line.substring(2)
        } else if(line.startsWith("*")) {
          isItalic = !isItalic
          changed = true
          line = line.substring(1)
        } else if(line.startsWith("~~")) {
          isStrikeThrough = !isStrikeThrough
          changed = true
          line = line.substring(2)
        } else if(line.matches("^\\[(.*?)]\\((.*?)\\)".toRegex())) {
          val match = "^\\[(.*?)]\\((.*?)\\)(.*)".toRegex().matchEntire(line)!!
          val text = match.groups[0]!!.value
          val link = match.groups[1]!!.value
          val isExternal = link.startsWith("http://") || link.startsWith("https://")
          val remainder = match.groups[2]!!.value
          elements.add(
            FormattedText.of(
              text,
              Style.EMPTY
                .withBold(isBold)
                .withItalic(isItalic)
                .withStrikethrough(isStrikeThrough)
                .withUnderlined(true)
                .withClickEvent(ClickEvent(
                  if(isExternal) ClickEvent.Action.OPEN_URL else ClickEvent.Action.RUN_COMMAND,
                  if(isExternal) link else "unfocused open $link"
                ))
            )
          )
          line = remainder
        }

        if(changed) {
          elements.add(
            FormattedText.of(
              current,
              Style.EMPTY
                .withBold(isBold)
                .withItalic(isItalic)
                .withStrikethrough(isStrikeThrough)
            )
          )
          current = ""
        } else {
          current += line[0]
          line = line.substring(1)
        }
      }
      if(line.isNotEmpty())
        elements.add(
          FormattedText.of(
            current,
            Style.EMPTY
              .withBold(isBold)
              .withItalic(isItalic)
              .withStrikethrough(isStrikeThrough)
          )
        )
    }
    return FormattedText.composite(elements)
  }
}