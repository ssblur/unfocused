package com.ssblur.unfocused.helper

import net.minecraft.client.gui.Font
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation
import org.joml.Vector2i

object MarkdownFormatter {
  data class PositionedResource(
    val resource: ResourceLocation,
    val altText: String,
    // coords are mutable because I expect to have to reposition stuff after knowing how words wrap and such
    // less expensive to mutate than generate a billion objects
    var x: Int, // x value, in characters this line. need to process font width before rendering
    var y: Int, // y value, in lines from the top. need to know line height before rendering
  )
  data class PageHolder(
    val component: Component,
    val images: List<PositionedResource>,
    val recipes: List<PositionedResource>,
    val items: List<PositionedResource>,
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

  fun parseMarkdown(markdown: String, imagesEnabled: Boolean = true): PageHolder {
    var element = Component.literal("")
    val lines = markdown.split("\n").map { it.trim() }

    val images = mutableListOf<PositionedResource>()
    val recipes = mutableListOf<PositionedResource>()
    val items = mutableListOf<PositionedResource>()

    var y = -1
    lines.forEach {
      var x = 0

      var line = it
      var current = ""

      // Could nest but this saves memory and is easy enough to follow so building a flat list instead
      var isBold = false
      var isItalic = false
      var isStrikeThrough = false
      val isTitle = line.startsWith('#')
      if(isTitle) line = line.substring(1)

      if(line.isEmpty()) {
        element = element.append(Component.literal("\n"))
        y++
      }

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
        } else if(imagesEnabled && line.matches("^!\\[(.*?)]\\((.*?)\\)".toRegex())) {
          val match = "^!\\[(.*?)]\\((.*?)\\)(.*)".toRegex().matchEntire(line)!!
          val altText = match.groups[0]!!.value
          val image = match.groups[1]!!.value
          val remainder = match.groups[2]!!.value
          images.add(PositionedResource(ResourceLocation.parse(image), altText, x, y+1))
          x = 0
          y += 6
          element = element.append(Component.literal("\n\n\n\n\n\n\n"))
          line = remainder
          continue
        } else if(imagesEnabled && line.matches("^<recipe (.*?)/?>".toRegex())) {
          val match = "^<recipe.*?href=\"?(.*?)\"?.*?/?>(.*)".toRegex().matchEntire(line)!!
          val recipe = match.groups[0]!!.value
          val remainder = match.groups[1]!!.value
          x = 0
          y += 6
          element = element.append(Component.literal("\n\n\n\n\n\n\n"))

          if(line.matches("^.*?</recipe\\w*>".toRegex())) {
            val match = "^(.*?)</recipe\\w*>(.*)".toRegex().matchEntire(line)!!
            recipes.add(PositionedResource(
              ResourceLocation.parse(recipe),
              match.groups[0]!!.value,
              x,
              y-5
            ))
            line = match.groups[1]!!.value
            continue
          }

          recipes.add(PositionedResource(ResourceLocation.parse(recipe), "", x, y-5))
          line = remainder
          continue
        } else if(line.matches("^<item (.*?)/?>".toRegex())) {
          val match = "^<item.*?href=\"?(.*?)\"?.*?/?>(.*)".toRegex().matchEntire(line)!!
          val item = match.groups[0]!!.value
          val remainder = match.groups[1]!!.value
          x += 3
          element = element.append(Component.literal("   "))

          if(line.matches("^.*?</item\\w*>".toRegex())) {
            val match = "^(.*?)</item\\w*>(.*)".toRegex().matchEntire(line)!!
            recipes.add(PositionedResource(
              ResourceLocation.parse(item),
              match.groups[0]!!.value,
              x-3,
              y
            ))
            line = match.groups[1]!!.value
            continue
          }

          items.add(PositionedResource(ResourceLocation.parse(item), "", x-3, y))
          line = remainder
          continue
        } else if(line.matches("^\\[(.*?)]\\((.*?)\\)".toRegex())) {
          val match = "^\\[(.*?)]\\((.*?)\\)(.*)".toRegex().matchEntire(line)!!
          val text = match.groups[0]!!.value
          val link = match.groups[1]!!.value
          val isExternal = link.startsWith("http://") || link.startsWith("https://")
          val remainder = match.groups[2]!!.value
          x += text.length
          element = element.append(
            Component.literal(text).withStyle(
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
          continue
        }

        current += line[0]
        line = line.substring(1)
        x++
      }
      if(current.isNotEmpty())
        element = append(element, current, isBold, isItalic, isStrikeThrough)
    }

    return PageHolder(element, images, recipes, items)
  }

  fun parseMarkdownWithoutImages(markdown: String) = parseMarkdown(markdown, false).component

  /**
   * Converts character x and y (like from PositionedResource) into a relative actual x and y
   */
  fun getLiteralPosition(component: Component, font: Font, x: Int, y: Int): Vector2i {
    font.width(component.string.split('\n')[y].substring(0, x+1))
    return Vector2i(0, font.lineHeight * y)
  }
}