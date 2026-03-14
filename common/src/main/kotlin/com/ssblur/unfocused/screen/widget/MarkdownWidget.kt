package com.ssblur.unfocused.screen.widget

import com.ssblur.unfocused.helper.MarkdownFormatter
import com.ssblur.unfocused.helper.MarkdownFormatter.asComponent
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import kotlin.math.max

class MarkdownWidget(x: Int, y: Int, w: Int, h: Int, text: String = "", var shadow: Boolean = true) : PositionedWidget(x, y, w, h) {
  private var markdownTextInternal: String = ""

  /**
   * The raw text that is used to do markdown formatting and such.
   * This is what you should set when updating the internal text here.
   */
  var markdownText: String
    get() = markdownTextInternal
    set(value) {
      markdownTextInternal = value
      parsed = MarkdownFormatter.parseMarkdown(markdownText, linkColor = linkColor)
    }

  init {
    markdownText = text
  }

  var parsed = MarkdownFormatter.parseMarkdown(markdownText)
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

  override fun mouseClicked(d: Double, e: Double, i: Int): Boolean {
    hoveredStyle?.let {
      parent?.handleComponentClicked(it)
      return true
    }
    return false
  }

  val font: Font = Minecraft.getInstance().font
  private var hoveredStyle: Style? = null
  private var hoveredItem: ItemStack? = null
  override fun draw(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, f: Float) {
    hoveredStyle = null
    hoveredItem = null
    var y = 0
    val lineHeight = font.lineHeight
    for(packet in parsed) {
      if(packet.component != null) {
        val lines = font.split(packet.component, w)
        for(line in lines) {
          guiGraphics.drawString(font, line, 0, y, color.toInt(), shadow)
          if(mouseY >= y && mouseY < (y + lineHeight)) {
            hoveredStyle = font.splitter.componentStyleAtWidth(line, mouseX) ?: hoveredStyle
          }
          y += lineHeight
        }
      } else if(packet.item != null) {
        itemCache[packet.item.resource] = itemCache[packet.item.resource] ?:
          ItemStack(BuiltInRegistries.ITEM.get(packet.item.resource))
        itemCache[packet.item.resource]?.let{
          guiGraphics.renderFakeItem(it, 5, y+5)
          guiGraphics.drawWordWrap(font, it.hoverName, 26, y + 10, w, color.toInt())
          val oy = y
          y += max(font.wordWrapHeight(it.hoverName, w), 26)
          if(mouseY in oy..y) hoveredItem = it
        }
      } else if(packet.recipe != null) {
        // render recipe
        guiGraphics.drawWordWrap(font, Component.literal("Recipe: ").append(packet.recipe.resource.toString()), 0, y, w, 0xffbbbbbbu.toInt())
        y += font.wordWrapHeight(Component.literal("Recipe: ").append(packet.recipe.resource.toString()), w)
        guiGraphics.drawWordWrap(font, Component.translatable("extra.unfocused.unimplemented"), 0, y, w, 0xffbbbbbbu.toInt())
        y += font.wordWrapHeight(Component.translatable("extra.unfocused.unimplemented"), w)
        guiGraphics.drawWordWrap(font, Component.translatable("extra.unfocused.unimplemented_2"), 0, y, w, 0xffbbbbbbu.toInt())
        y += font.wordWrapHeight(Component.translatable("extra.unfocused.unimplemented_2"), w)
      } else if(packet.image != null) {
        val lh = font.lineHeight * 8
        guiGraphics.blit(packet.image.resource, 0, y, 0F, 0F, lh, lh, lh, lh)
        y += lh
      }
    }
    maxScroll = y + font.lineHeight * 2
  }

  override fun drawOverlay(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, f: Float) {
    super.drawOverlay(guiGraphics, mouseX, mouseY, f)
    if(hoveredStyle != null) guiGraphics.renderComponentHoverEffect(font, hoveredStyle, mouseX, mouseY)
    if(hoveredItem != null) guiGraphics.renderTooltip(font, hoveredItem!!, mouseX, mouseY)
  }

  override fun updateNarration(narrationElementOutput: NarrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, parsed.asComponent())
  }

  companion object {
    private val itemCache: MutableMap<ResourceLocation, ItemStack> = mutableMapOf()
  }
}