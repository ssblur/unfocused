package com.ssblur.unfocused.screen.widget

import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.extension.SoundEventExtension.play
import com.ssblur.unfocused.screen.renderable.InventoryBackground.Companion.TEXTURE
import com.ssblur.unfocused.screen.renderable.PositionedRenderable
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.events.GuiEventListener
import net.minecraft.client.gui.narration.NarratableEntry
import net.minecraft.sounds.SoundEvents
import kotlin.math.roundToInt

abstract class PositionedWidget(
  x: Int,
  y: Int,
  w: Int,
  h: Int,
  scissor: Boolean = true,
): NarratableEntry, GuiEventListener, PositionedRenderable(x, y, w, h, scissor) {
  override fun narrationPriority() = NarratableEntry.NarrationPriority.HOVERED

  private var focused: Boolean = false
  override fun setFocused(bl: Boolean) {
    focused = bl
  }
  override fun isFocused(): Boolean = focused

  var pageButtons = false
  var pageButtonsSize = 16
  var pageButtonsMargin = 6
  fun overNextPage(mouseX: Double, mouseY: Double) = (pageButtons && pageNumber < pages
      && mouseX.toInt() in (w-(pageButtonsSize+pageButtonsMargin))..(w-pageButtonsMargin)
      && mouseY.toInt() in (h-(pageButtonsSize+pageButtonsMargin))..(h-pageButtonsMargin))
  fun overPrevPage(mouseX: Double, mouseY: Double) = (pageButtons && pageNumber > 1
      && mouseX.toInt() in pageButtonsMargin..(pageButtonsMargin+pageButtonsSize)
      && mouseY.toInt() in (h-(pageButtonsSize+pageButtonsMargin))..(h-pageButtonsMargin))
  val shouldScroll: Boolean
    get() = !pageButtons && maxScroll > h
  val pageNumber: Int
    get() = scroll.roundToInt()/h + 1
  val pages: Int
    get() = maxScroll/h + 1

  open fun nextPage() {
    scroll(h.toDouble())
  }
  open fun prevPage() {
    scroll(-h.toDouble())
  }


  override fun drawOverlay(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, f: Float) {
    super.drawOverlay(guiGraphics, mouseX, mouseY, f)

    if(pageNumber < pages) {
      if(overNextPage(mouseX.toDouble(), mouseY.toDouble())) {
        guiGraphics.blitSprite(
          NEXT_BUTTON_HIGHLIGHT,
          w-(pageButtonsSize+pageButtonsMargin),
          h-(pageButtonsSize+pageButtonsMargin),
          16,
          16
        )
      } else {
        guiGraphics.blitSprite(
          NEXT_BUTTON,
          w-(pageButtonsSize+pageButtonsMargin),
          h-(pageButtonsSize+pageButtonsMargin),
          16,
          16
        )
      }
    }
    if(pageNumber > 1) {
      if(overPrevPage(mouseX.toDouble(), mouseY.toDouble())) {
        guiGraphics.blitSprite(
          PREV_BUTTON_HIGHLIGHT,
          pageButtonsMargin,
          h-(pageButtonsSize+pageButtonsMargin),
          16,
          16
        )
      } else {
        guiGraphics.blitSprite(
          PREV_BUTTON,
          pageButtonsMargin,
          h-(pageButtonsSize+pageButtonsMargin),
          16,
          16
        )
      }
    }
  }

  override fun mouseClicked(d: Double, e: Double, i: Int): Boolean {
    if(!isMouseOver(d, e)) return false

    if(overNextPage(d, e)) {
      SoundEvents.UI_BUTTON_CLICK.play()
      nextPage()
      return true
    } else if(overPrevPage(d, e)) {
      SoundEvents.UI_BUTTON_CLICK.play()
      prevPage()
      return true
    }

    if(i == 0) return leftClick(d - x, e - y + scroll)
    if(i == 1) return rightClick(d - x, e - y + scroll)
    return super.mouseClicked(d, e, i)
  }

  override fun isMouseOver(d: Double, e: Double) = mouseOver(d, e)

  open fun leftClick(x: Double, y: Double) = false
  open fun rightClick(x: Double, y: Double) = false

  override fun mouseScrolled(d: Double, e: Double, f: Double, g: Double): Boolean {
    if(!isMouseOver(d, e)) return false
    if(shouldScroll) scroll(g * -6.0)
    return true
  }

  companion object {
    val PREV_BUTTON = Unfocused.location("minecraft:widget/page_backward")
    val PREV_BUTTON_HIGHLIGHT = Unfocused.location("minecraft:widget/page_backward_highlighted")
    val NEXT_BUTTON = Unfocused.location("minecraft:widget/page_forward")
    val NEXT_BUTTON_HIGHLIGHT = Unfocused.location("minecraft:widget/page_forward_highlighted")
  }
}