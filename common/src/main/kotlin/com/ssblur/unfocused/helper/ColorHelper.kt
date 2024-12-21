package com.ssblur.unfocused.helper

import net.minecraft.world.item.DyeColor
import java.util.function.Function

@Suppress("unused")
object ColorHelper {
    data class ColorHolder(val dyeColor: DyeColor) {
        val colorName = dyeColor.name
        val nameAllCaps inline get() = colorName.uppercase()
        val nameAllLowerCase inline get() = colorName.lowercase()
        val nameTitleCase
            inline get() = "_[a-z]".toRegex().replace(colorName) {
                it.value.substring(1).uppercase()
            }

        val fireworksColor inline get() = dyeColor.fireworkColor
        val textColor inline get() = dyeColor.textColor
    }

    /**
     * Runs a function once per color, with corresponding color info.
     * Useful for
     */
    fun <T> forEachColor(handler: Function<ColorHolder, T>) = DyeColor.entries.map{
        handler.apply(ColorHolder(it))
    }
}