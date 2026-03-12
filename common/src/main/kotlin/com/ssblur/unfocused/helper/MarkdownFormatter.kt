package com.ssblur.unfocused.helper

import net.minecraft.network.chat.*
import net.minecraft.resources.ResourceLocation

object MarkdownFormatter {
  data class MarkdownRecipe(
    val resource: ResourceLocation,
    val altText: String,
  )
  data class MarkdownItem(
    val resource: ResourceLocation,
    val text: String,
  )
  data class MarkdownImage(
    val resource: ResourceLocation,
    val altText: String
  )
  data class MarkdownPacket(
    val component: Component? = null,
    val recipe: MarkdownRecipe? = null,
    val item: MarkdownItem? = null,
    val image: MarkdownImage? = null,
  )

  fun append(
    component: MutableComponent,
    text: String,
    bold: Boolean,
    italic: Boolean,
    strikethrough: Boolean,
    underline: Boolean,
    title: Boolean,
  ): MutableComponent {
    return component.append(
      Component.literal(text).withStyle(
        Style.EMPTY
          .withBold(bold || title)
          .withItalic(italic)
          .withStrikethrough(strikethrough)
          .withUnderlined(underline || title)
      )
    )
  }

  fun parseMarkdown(markdown: String, imagesEnabled: Boolean = true, linkColor: UInt = 0x3333ffu): List<MarkdownPacket> {
    val elements = mutableListOf<MarkdownPacket>()
    var lastComponent = Component.empty()
    val lines = markdown.split("\n").map { it.trim() }

    lines.forEach {
      var line = it
      var current = ""

      // Could nest but this saves memory and is easy enough to follow so building a flat list instead
      var isBold = false
      var isItalic = false
      var isStrikeThrough = false
      val isUnderline = false
      val isTitle = line.startsWith('#')
      if(isTitle) line = line.substring(1).trimStart()

      if(line.isEmpty()) {
        lastComponent = lastComponent.append("\n")
      }

      while(line.isNotEmpty()) {
        if(line.startsWith("***")) {
          lastComponent = append(lastComponent, current, isBold, isItalic, isStrikeThrough, isUnderline, isTitle)
          current = ""
          isBold = !isBold
          isItalic = !isItalic
          line = line.substring(3)
          continue
        } else if(line.startsWith("**")) {
          lastComponent = append(lastComponent, current, isBold, isItalic, isStrikeThrough, isUnderline, isTitle)
          current = ""
          isBold = !isBold
          line = line.substring(2)
          continue
        } else if(line.startsWith("*")) {
          lastComponent = append(lastComponent, current, isBold, isItalic, isStrikeThrough, isUnderline, isTitle)
          current = ""
          isItalic = !isItalic
          line = line.substring(1)
          continue
        } else if(line.startsWith("~~")) {
          lastComponent = append(lastComponent, current, isBold, isItalic, isStrikeThrough, isUnderline, isTitle)
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
          elements.add(MarkdownPacket(image = MarkdownImage(ResourceLocation.parse(image), altText)))
          line = remainder
          continue
        } else if(imagesEnabled && line.matches("^<recipe (.*?)/?>.*".toRegex())) {
          val match = "^<recipe.*?href=\"?(.*?)\"?.*?/?>(.*)".toRegex().matchEntire(line)!!
          val recipe = match.groups[0]!!.value
          val remainder = match.groups[1]!!.value

          elements.add(MarkdownPacket(component = lastComponent))
          lastComponent = Component.empty()

          if(line.matches("^.*?</recipe\\w*>.*".toRegex())) {
            val match = "^(.*?)</recipe\\w*>(.*)".toRegex().matchEntire(line)!!
            elements.add(MarkdownPacket(
              recipe = MarkdownRecipe(ResourceLocation.parse(recipe), match.groups[0]!!.value)
            ))
            line = match.groups[1]!!.value
            continue
          }
          line = remainder
          continue
        } else if(line.matches("^<item (.*?)/?>.*".toRegex())) {
          val match = "^<item.*?href=\"?(.*?)\"?.*?/?>(.*)".toRegex().matchEntire(line)!!
          val item = match.groups[0]!!.value
          val remainder = match.groups[1]!!.value
          elements.add(MarkdownPacket(component = lastComponent))
          lastComponent = Component.empty()
          if(line.matches("^.*?</item\\w*>.*".toRegex())) {
            val match = "^(.*?)</item\\w*>(.*)".toRegex().matchEntire(line)!!
            elements.add(MarkdownPacket(item = MarkdownItem(ResourceLocation.parse(item), match.groups[0]!!.value)))
            line = match.groups[1]!!.value
            continue
          }
          line = remainder
          continue
        } else if(line.matches("^\\[(.*?)]\\((.*?)\\).*".toRegex())) {
          val match = "^\\[(.*?)]\\((.*?)\\)(.*)".toRegex().matchEntire(line)!!
          val text = match.groups[1]!!.value
          val link = match.groups[2]!!.value

          val isExternal = link.startsWith("http://") || link.startsWith("https://")
          val remainder = match.groups[3]!!.value
          lastComponent = append(lastComponent, current, isBold, isItalic, isStrikeThrough, isUnderline, isTitle)
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
                  if(isExternal) link else "/unfocused open $link"
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
        lastComponent = append(lastComponent, current, isBold, isItalic, isStrikeThrough, isUnderline, isTitle)
    }
    elements.add(MarkdownPacket(component = lastComponent))
    return elements
  }

  fun Iterable<MarkdownPacket>.textOnly() = this.filter { it.component != null }
  fun Iterable<MarkdownPacket>.asComponent(): Component {
    val component = Component.empty()
    for(c in this) {
      if(c.component != null) component.append(c.component)
    }
    return component
  }
  fun parseMarkdownWithoutImages(markdown: String) = parseMarkdown(markdown, false).textOnly()
}