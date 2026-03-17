package com.ssblur.unfocused.screen.widget

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.narration.NarratedElementType
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import org.joml.Vector2i
import kotlin.math.floor

class TextEntryWidget(x: Int, y: Int, w: Int, h: Int, scissor: Boolean = true) : PositionedWidget(x, y, w, h, scissor) {
  var canScroll: Boolean = true

  private var textInternal: String = ""
  private var textComponent: Component = Component.empty()
  var color: UInt = 0x000000u
  var cursorColor: UInt = 0x333333u
  var textStyle: Style = Style.EMPTY
  var text: String
    get() = "$textInternal "
    set(value) {
      textInternal = if(value.endsWith(' '))
        value.dropLast(1)
      else
        value
      textComponent = Component.literal(textInternal)
      if(canScroll)
        maxScroll = font.lineHeight * font.splitter.splitLines(textComponent, w, textStyle).size
      onTextUpdate(this, textInternal)
    }
  var onTextUpdate: (widget: TextEntryWidget, text: String) -> Unit = {widget, text -> }

  private var cursorIndexInternal = 0
  private var cursorRenderPos: Vector2i = Vector2i(0, 0)
  var cursorIndex: Int
    get() = cursorIndexInternal
    set(value) {
      val index = value.coerceIn(0..textInternal.length)
      cursorIndexInternal = index
      if(index == 0) {
        cursorRenderPos = Vector2i(0, 0)
        return
      }
      val lines = font.splitter.splitLines(Component.literal(textInternal), w, textStyle)
      var y = 0
      var i = 0
      for(line in lines) {
        if((i + line.string.length) < index) {
          i += line.string.length
          y += font.lineHeight
        } else {
          var li = 0
          for(ignored in line.string) {
            li++
            i++
            if(i == index) {
              val x = font.splitter.stringWidth(line.string.substring(0, li)).toInt()
              cursorRenderPos = Vector2i(x, y)
              return
            }
          }
        }
      }

      if(canScroll) {
        // TODO scroll until the cursor is visible
      }
    }
  val safeCursorIndex: Int
    get() = cursorIndex.coerceIn(0..text.length)

  var font: Font = Minecraft.getInstance().font

  data class CursorPos(val index: Int, val screenX: Int, val screenY: Int)
  val mouseXOffset = 4
  fun getCursorIndexAt(mouseX: Int, mouseY: Int): CursorPos {
    if(!canScroll) {
      val x = font.splitter.plainIndexAtWidth(text, mouseX + mouseXOffset, textStyle)
      val cursorX = font.width(text.substring(0, x))
      return CursorPos(x, cursorX, 0)
    }

    val lineY = floor((mouseY / font.lineHeight).toDouble()).toInt()
    val lines = font.splitter.splitLines(Component.literal(text), w, textStyle)
    if(lines.isEmpty()) {
      return CursorPos(0, 0, 0)
    } else if(lineY >= lines.size) {
      val lineX = font.splitter.stringWidth(lines.last().string.dropLast(1))
      return CursorPos(text.length, lineX.toInt(), (lines.size - 1) * font.lineHeight)
    }
    var lineX = font.splitter.plainIndexAtWidth(lines[lineY].string, mouseX + mouseXOffset, textStyle)
    var index = lines.mapIndexed { i, s -> if(i < lineY) s.string.length + 1 else 0 }.sum() + lineX
    if(index >= textInternal.length) {
      index--
      lineX--
    }
    val cursorX = font.width(lines[lineY].string.substring(0, lineX))
    return CursorPos(index, cursorX, lineY * font.lineHeight)
  }

  override fun updateNarration(narrationElementOutput: NarrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, textComponent)
  }

  private val blinkSpeed = 12
  private val blinkDelay = blinkSpeed * 2
  override fun draw(
    guiGraphics: GuiGraphics,
    mouseX: Int,
    mouseY: Int,
    f: Float
  ) {
    if(canScroll) {
      guiGraphics.drawWordWrap(font, textComponent, 0, 0, w, color.toInt())
    } else {
      guiGraphics.drawString(font, textComponent, 0, 0, color.toInt())
    }
    if (isFocused && Minecraft.getInstance().gui.guiTicks % blinkDelay > blinkSpeed)
      guiGraphics.drawString(font, "|", cursorRenderPos.x, cursorRenderPos.y, cursorColor.toInt())
  }

  override fun keyPressed(key: Int, j: Int, modifier: Int): Boolean {
    if(isFocused) {
      when(key) {
        69 -> return true // e
        263 -> { // Left
          if(modifier == 2) cursorIndex = prevWhitespace()
          else cursorIndex--
          return true
        }
        262 -> { // Right
          if(modifier == 2) cursorIndex = nextWhitespace()
          else cursorIndex++
          return true
        }
        265 -> { // Up
          if(canScroll) cursorUp()
          return true
        }
        264 -> { // Down
          if(canScroll) cursorDown()
          return true
        }
        268 -> { // Home
          cursorLineStart()
          return true
        }
        269 -> { // End
          cursorLineEnd()
          return true
        }
        259 -> { // Backspace
          text = text.substring(0, safeCursorIndex - 1) + text.substring(safeCursorIndex)
          cursorIndex--
          return true
        }
        261 -> { // Delete
          try {
            text = text.substring(0, safeCursorIndex) + text.substring(safeCursorIndex + 1)
            cursorIndex = cursorIndex // Resets cursor screen position
          } catch(_: StringIndexOutOfBoundsException) {}
          return true
        }
        260 -> // Insert
          if(modifier == 1) // +Shift
            paste()
        86 -> // V
          if(modifier == 2) // +Ctrl
            paste()
      }
    }
    return super.keyPressed(key, j, modifier)
  }

  override fun keyReleased(i: Int, j: Int, k: Int): Boolean {
    return super.keyReleased(i, j, k)
  }

  override fun charTyped(c: Char, i: Int): Boolean {
    if(isFocused) {
      text = text.substring(0, safeCursorIndex) + c + text.substring(safeCursorIndex)
      cursorIndex++
      return true
    }
    return super.charTyped(c, i)
  }

  override fun leftClick(x: Double, y: Double): Boolean {
    val clickPos = getCursorIndexAt(x.toInt(), y.toInt())
    cursorIndexInternal = clickPos.index
    cursorRenderPos = Vector2i(clickPos.screenX, clickPos.screenY)
    return true
  }

  override fun rightClick(x: Double, y: Double): Boolean {
    return super.rightClick(x, y)
  }

  private fun nextWhitespace(): Int {
    var i = cursorIndex.plus(1).coerceIn(0..textInternal.length.minus(1))
    while(i < textInternal.length && !textInternal[i].isWhitespace()) i++
    return i
  }

  private fun prevWhitespace(): Int {
    var i = cursorIndex.minus(1).coerceIn(0..textInternal.length.minus(1))
    while(i > 0 && !textInternal[i].isWhitespace()) i--
    return i
  }

  private fun cursorDown() {
    val line = cursorRenderPos.y / font.lineHeight
    val lines = font.splitter.splitLines(Component.literal(text), w, textStyle)
    if(line >= lines.size - 1) {
      cursorIndex = textInternal.length
      return
    }
    var index = lines.mapIndexed { i, s -> if(i <= line) s.string.length + 1 else 0 }.sum()
    index += font.splitter.plainIndexAtWidth(lines[line + 1].string, cursorRenderPos.x, textStyle)
    cursorIndex = index
  }

  private fun cursorUp() {
    val line = cursorRenderPos.y / font.lineHeight
    val lines = font.splitter.splitLines(Component.literal(text), w, textStyle)
    if(line <= 0) {
      cursorIndex = 0
      return
    }
    var index = lines.mapIndexed { i, s -> if(i < line - 1) s.string.length + 1 else 0 }.sum()
    index += font.splitter.plainIndexAtWidth(lines[line - 1].string, cursorRenderPos.x, textStyle)
    cursorIndex = index
  }

  private fun cursorLineStart() {
    val line = cursorRenderPos.y / font.lineHeight
    val lines = font.splitter.splitLines(Component.literal(text), w, textStyle)
    cursorIndex = lines.mapIndexed { i, s -> if(i < line) s.string.length + 1 else 0 }.sum() - 1
  }

  private fun cursorLineEnd() {
    val line = cursorRenderPos.y / font.lineHeight
    val lines = font.splitter.splitLines(Component.literal(text), w, textStyle)
    cursorIndex = lines.mapIndexed { i, s -> if(i <= line) s.string.length + 1 else 0 }.sum() - 2
  }

  private fun paste() {
    val clipboard = Minecraft.getInstance().keyboardHandler.clipboard
    text = text.substring(0, safeCursorIndex) + clipboard + text.substring(safeCursorIndex)
    cursorIndex += clipboard.length
  }
}