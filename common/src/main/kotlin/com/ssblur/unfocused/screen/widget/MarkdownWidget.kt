package com.ssblur.unfocused.screen.widget

import com.ssblur.unfocused.helper.MarkdownFormatter
import com.ssblur.unfocused.helper.MarkdownFormatter.asComponent
import com.ssblur.unfocused.mixin.ScreenAccessor
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.ClickEvent.Action
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.world.item.ItemStack
import kotlin.math.ceil
import kotlin.math.max

class MarkdownWidget(
  x: Int,
  y: Int,
  w: Int,
  h: Int,
  text: String = "",
  var shadow: Boolean = false,
  var commandsAllowed: Boolean = false):
  PositionedWidget(x, y, w, h) {
  private var markdownTextInternal: String = ""

  /**
   * The raw text that is used to do Markdown formatting and such.
   * This is what you should set when updating the internal text here.
   */
  var markdownText: String
    get() = markdownTextInternal
    set(value) {
      markdownTextInternal = value
      parsed = MarkdownFormatter.parseMarkdown(markdownText, linkColor = linkColor, commandsAllowed = commandsAllowed)
    }

  init {
    markdownText = text
  }

  var parsed = MarkdownFormatter.parseMarkdown(markdownText, commandsAllowed = commandsAllowed)
    private set

  var color: UInt = 0xff000000u
  fun setColor(r: Int, g: Int, b: Int) {
    color = 0xff000000u or (r and 255 shl 16).toUInt() or (g and 255 shl 8).toUInt() or (b and 255).toUInt()
  }

  var linkColor: UInt = 0x3333ffu
  @Suppress("unused")
  fun setLinkColor(r: Int, g: Int, b: Int) {
    linkColor = (r and 255 shl 16).toUInt() or (g and 255 shl 8).toUInt() or (b and 255).toUInt()
  }

  /**
   * Allows registering a custom handler for page:// links.
   * { String -> Boolean }
   */
  @Suppress("unused")
  var handlePageTurned = { page: Int -> false }

  override fun mouseClicked(event: MouseButtonEvent, bl: Boolean): Boolean {
    hoveredStyle?.let {
      if(it.clickEvent != null)
        if(it.clickEvent?.action() != Action.CHANGE_PAGE) {
          ScreenAccessor.callDefaultHandleGameClickEvent(it.clickEvent!!, Minecraft.getInstance(), parent)
          return true
        } else if(it.clickEvent?.action() == Action.CHANGE_PAGE) {
          val page = (it.clickEvent as ClickEvent.ChangePage).page()
          return handlePageTurned(page)
        }
    }
    return false
  }

  val font: Font = Minecraft.getInstance().font
  val itemCache: MutableMap<MarkdownFormatter.Markdown.Item, ItemStack> = mutableMapOf()
  private var hoveredStyle: Style? = null
  private var hoveredItem: ItemStack? = null
  override fun draw(guiGraphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, f: Float) {
    hoveredStyle = null
    hoveredItem = null
    var y = 0
    val ww = w - 3
    val lineHeight = font.lineHeight
    for(packet in parsed) {
      if(packet.component != null) {
        val lines = font.split(packet.component, ww)
        for(line in lines) {
          guiGraphics.text(font, line, 0, y, color.toInt(), shadow)
          if(mouseY >= y && mouseY < (y + lineHeight)) {
            hoveredStyle = MarkdownFormatter.getStyleAtWidth(line, mouseX, font) ?: hoveredStyle
          }
          y += lineHeight
        }
      } else if(packet.item != null) {
        itemCache[packet.item] = itemCache[packet.item] ?:
          ItemStack(BuiltInRegistries.ITEM.get(packet.item.resource).get())
        itemCache[packet.item]?.let{
          guiGraphics.item(it, 5, y+5)
          guiGraphics.textWithWordWrap(font, it.hoverName, 26, y + 10, ww, color.toInt(), false)
          val oy = y
          y += max(font.wordWrapHeight(it.hoverName, w), 26)
          if(mouseY in oy..y) hoveredItem = it
        }
      } else if(packet.recipe != null) {
        // render recipe
        guiGraphics.textWithWordWrap(font, Component.literal("Recipe: ").append(packet.recipe.resource.toString()), 0, y, ww, 0xffbbbbbbu.toInt())
        y += font.wordWrapHeight(Component.literal("Recipe: ").append(packet.recipe.resource.toString()), ww)
        guiGraphics.textWithWordWrap(font, Component.translatable("extra.unfocused.unimplemented"), 0, y, ww, 0xffbbbbbbu.toInt())
        y += font.wordWrapHeight(Component.translatable("extra.unfocused.unimplemented"), ww)
        guiGraphics.textWithWordWrap(font, Component.translatable("extra.unfocused.unimplemented_2"), 0, y, ww, 0xffbbbbbbu.toInt())
        y += font.wordWrapHeight(Component.translatable("extra.unfocused.unimplemented_2"), ww)
      } else if(packet.image != null) {
        val lh = font.lineHeight * 8
        guiGraphics.blit(
          packet.image.resource,
          0,
          y,
          lh,
          y + lh,
          0f,
          1f,
          0f,
          1f
        )
        y += lh
      } else if(packet.title != null) {
        val lines = font.split(packet.title.component, ww / 2)
        val scale = 1.0f + (1.0f / packet.title.depth)
        val pose = guiGraphics.pose()
        pose.pushMatrix()
        pose.scale(scale)
        for(line in lines) {
          val ey = (y / scale).toInt()
          val lh = ceil(lineHeight * scale).toInt()
          guiGraphics.text(font, line, 0, ey, color.toInt(), shadow)
          if(mouseY >= y && mouseY < (y + lh)) {
            hoveredStyle = MarkdownFormatter.getStyleAtWidth(line, (mouseX * scale).toInt(), font) ?: hoveredStyle
          }
          y += lh
        }
        pose.popMatrix()
      }
    }
    maxScroll = y + font.lineHeight * 2
  }

  override fun drawOverlay(guiGraphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, f: Float) {
    super.drawOverlay(guiGraphics, mouseX, mouseY, f)
//    if(hoveredStyle != null)
//      guiGraphics.renderComponentHoverEffect(font, hoveredStyle, mouseX + x, mouseY - scroll.toInt() + 24)
    // TODO fix hovered component rendering

    // TODO add hovered item tooltip
  }

  override fun updateNarration(narrationElementOutput: NarrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, parsed.asComponent())
  }
}