package com.ssblur.unfocused.helper

import net.minecraft.network.chat.*
import net.minecraft.resources.ResourceLocation

object MarkdownFormatter {
  object Markdown {
    data class Recipe(
      val resource: ResourceLocation,
      val altText: String,
    )
    data class Item(
      val resource: ResourceLocation,
      val text: String,
      val data: String?,
    )
    data class Image(
      val resource: ResourceLocation,
      val altText: String
    )
    data class Title(
      val component: Component,
      val depth: Int = 1
    )
  }
  data class MarkdownPacket(
    val component: Component? = null,
    val recipe: Markdown.Recipe? = null,
    val item: Markdown.Item? = null,
    val image: Markdown.Image? = null,
    val title: Markdown.Title? = null,
  )

  fun append(
    component: MutableComponent,
    text: String,
    bold: Boolean,
    italic: Boolean,
    strikethrough: Boolean,
    underline: Boolean,
  ): MutableComponent {
    return component.append(
      Component.literal(text).withStyle(
        Style.EMPTY
          .withBold(bold)
          .withItalic(italic)
          .withStrikethrough(strikethrough)
          .withUnderlined(underline)
      )
    )
  }

  fun parseMarkdown(
    markdown: String,
    imagesEnabled: Boolean = true,
    linkColor: UInt = 0x3333ffu,
    commandsAllowed: Boolean = false,
  ): List<MarkdownPacket> {
    val elements = mutableListOf<MarkdownPacket>()
    var lastComponent = Component.empty()
    val lines = markdown.split("\n").map { it.trimStart() }

    lines.forEach {
      var line = it
      var current = ""

      // Could nest but this saves memory and is easy enough to follow so building a flat list instead
      var isBold = false
      var isItalic = false
      var isStrikeThrough = false
      val isUnderline = false
      var titleDepth = 0
      for(i in line.trimStart()) {
        if(i == '#') titleDepth++
        else break
      }
      val isTitle = titleDepth > 0
      if(isTitle) {
        line = line.trimStart('#', ' ', '\t')
        elements.add(MarkdownPacket(component = lastComponent))
        lastComponent = Component.empty()
      }
      val isList = line.trimStart().startsWith("* ") || line.trimStart().startsWith("- ")
      if(isList) {
        line = line.trimStart().substring(1).trimStart()
        lastComponent = lastComponent.append("\n  • ")
      }

      if(line.trim().isEmpty()) {
        lastComponent = lastComponent.append(Component.literal("\n\n"))
      } else {
        line += " "
      }

      while(line.isNotEmpty()) {
        if(line.startsWith("***")) {
          lastComponent = append(lastComponent, current, isBold, isItalic, isStrikeThrough, isUnderline)
          current = ""
          isBold = !isBold
          isItalic = !isItalic
          line = line.substring(3)
          continue
        } else if(line.startsWith("**")) {
          lastComponent = append(lastComponent, current, isBold, isItalic, isStrikeThrough, isUnderline)
          current = ""
          isBold = !isBold
          line = line.substring(2)
          continue
        } else if(line.startsWith("*")) {
          lastComponent = append(lastComponent, current, isBold, isItalic, isStrikeThrough, isUnderline)
          current = ""
          isItalic = !isItalic
          line = line.substring(1)
          continue
        } else if(line.startsWith("~~")) {
          lastComponent = append(lastComponent, current, isBold, isItalic, isStrikeThrough, isUnderline)
          current = ""
          isStrikeThrough = !isStrikeThrough
          line = line.substring(2)
          continue
        } else if(imagesEnabled && line.matches("^!\\[(.*?)]\\((.*?)\\).*".toRegex())) {
          val match = "^!\\[(.*?)]\\((.*?)\\)(.*)".toRegex().matchEntire(line)!!
          val altText = match.groups[1]!!.value
          val image = match.groups[2]!!.value
          val remainder = match.groups[3]!!.value
          elements.add(MarkdownPacket(component = lastComponent))
          lastComponent = Component.empty()
          elements.add(MarkdownPacket(image = Markdown.Image(ResourceLocation.parse(image), altText)))
          line = remainder
          continue
        } else if(imagesEnabled && line.matches("^<recipe (.*?)/?>.*".toRegex())) {
          val match = "^<recipe.*?href=\"(.*?)\".*?/?>(.*)".toRegex().matchEntire(line)!!
          val recipe = match.groups[1]!!.value
          val remainder = match.groups[2]!!.value

          elements.add(MarkdownPacket(component = lastComponent))
          lastComponent = Component.empty()

          if(line.matches("^.*?</recipe\\w*>.*".toRegex())) {
            val match = "^(.*?)</recipe\\w*>(.*)".toRegex().matchEntire(remainder)!!
            elements.add(MarkdownPacket(
              recipe = Markdown.Recipe(ResourceLocation.parse(recipe), match.groups[1]!!.value)
            ))
            line = match.groups[2]!!.value
            continue
          }
          line = remainder
          continue
        } else if(line.matches("^<item (.*?)/?>.*".toRegex())) {
          val match = "^<item.*?href=\"(.*?)\".*?/?>(.*)".toRegex().matchEntire(line)!!
          var item = match.groups[1]!!.value
          val remainder = match.groups[2]!!.value
          val data: String?
          if(item.contains('{')) {
            val match = "^(.*?)(\\{.*})".toRegex().matchEntire(item)!!
            item = match.groups[1]!!.value
            data = match.groups[2]!!.value
          } else {
            data = null
          }
          elements.add(MarkdownPacket(component = lastComponent))
          lastComponent = Component.empty()
          if(line.matches("^.*?</item\\w*>.*".toRegex())) {
            val match = "^(.*?)</item\\w*>(.*)".toRegex().matchEntire(remainder)!!
            elements.add(MarkdownPacket(item = Markdown.Item(ResourceLocation.parse(item), match.groups[1]!!.value, data)))
            line = match.groups[2]!!.value
            continue
          }
          line = remainder
          continue
        } else if(line.matches("^\\[(.*?)]\\((.*?)\\).*".toRegex())) {
          val match = "^\\[(.*?)]\\((.*?)\\)(.*)".toRegex().matchEntire(line)!!
          val text = match.groups[1]!!.value
          val link = match.groups[2]!!.value

          val isExternal = link.startsWith("http://") || link.startsWith("https://")
          val command = if(commandsAllowed && link.startsWith("cmd://"))
              link.substring(5)
            else
              "/unfocused open $link"
          val remainder = match.groups[3]!!.value
          lastComponent = append(lastComponent, current, isBold, isItalic, isStrikeThrough, isUnderline)
          current = ""
          lastComponent = lastComponent.append(Component.literal(text).withStyle(
              Style.EMPTY
                .withBold(isBold)
                .withItalic(isItalic)
                .withStrikethrough(isStrikeThrough)
                .withUnderlined(true)
                .withColor(linkColor.toInt())
                .withClickEvent(ClickEvent(
                  if(isExternal) ClickEvent.Action.OPEN_URL else ClickEvent.Action.RUN_COMMAND,
                  if(isExternal) link else command
                ))
                .withHoverEvent(
                  if(isExternal)
                    HoverEvent(
                      HoverEvent.Action.SHOW_TEXT,
                      Component.translatable("extra.unfocused.go_to_external", link)
                    )
                  else
                    HoverEvent(
                      HoverEvent.Action.SHOW_TEXT,
                      Component.translatable("extra.unfocused.go_to")
                    )
                )
            )
          )
          line = remainder
          continue
        }

        current += line[0]
        line = line.substring(1)
      }
      if(current.isNotEmpty())
        lastComponent = append(lastComponent, current, isBold, isItalic, isStrikeThrough, isUnderline)

      if(isTitle) {
        elements.add(MarkdownPacket(title = Markdown.Title(lastComponent, titleDepth)))
        lastComponent = Component.empty()
      }
    }
    elements.add(MarkdownPacket(component = lastComponent))
    return elements
  }

  @Suppress("unused")
  fun Iterable<MarkdownPacket>.textOnly() = this.filter { it.component != null }
  fun Iterable<MarkdownPacket>.asComponent(): Component {
    val component = Component.empty()
    for(c in this) {
      if(c.component != null) component.append(c.component)
    }
    return component
  }
}