package com.ssblur.unfocused.screen.widget

import com.ssblur.unfocused.helper.MarkdownFormatter
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.FormattedText

class MarkdownWidget(x: Int, y: Int, w: Int, h: Int) : PositionedWidget(x, y, w, h) {
  private var markdownTextInternal: String = ""

  /**
   * The raw text that is used to do markdown formatting and such.
   * This is what you should set when updating the internal text here.
   * @see formattedText for pre-formatted, ready-to-render text output generated based on tihs
   */
  var markdownText: String = ""
    fun get() = markdownTextInternal
    fun set(value: String) {
      markdownTextInternal = value
      formattedText = MarkdownFormatter.markdownToFormattedText(markdownText)
    }

  var formattedText: FormattedText = MarkdownFormatter.markdownToFormattedText(markdownText)
    private set

  override fun draw(guiGraphics: GuiGraphics) {
    val font = Minecraft.getInstance().font
    guiGraphics.drawWordWrap(font, formattedText, 0, 0, 0xff000000u.toInt(), w)
  }

  override fun updateNarration(narrationElementOutput: NarrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, Component.literal(plainText()))
  }

  fun plainText(): String = formattedText.string
}