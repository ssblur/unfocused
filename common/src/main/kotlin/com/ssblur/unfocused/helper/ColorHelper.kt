package com.ssblur.unfocused.helper

import com.ssblur.unfocused.UtilityExpectPlatform
import com.ssblur.unfocused.registry.RegistrySupplier
import net.minecraft.client.color.block.BlockColor
import net.minecraft.client.color.item.ItemColor
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.block.Block
import java.util.function.Function
import java.util.function.Supplier

@Suppress("unused", "unchecked_cast")
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

    fun registerColor(color: ItemColor, vararg items: Supplier<ItemLike>) = UtilityExpectPlatform.registerColor(color, *items)
    fun registerColor(color: BlockColor, vararg blocks: Supplier<Block>) = UtilityExpectPlatform.registerColor(color, *blocks)

    fun RegistrySupplier<Item>.registerColor(color: ItemColor) = registerColor(color, this as RegistrySupplier<ItemLike>)
    fun RegistrySupplier<Block>.registerColor(color: BlockColor) = registerColor(color, this)
    fun Item.registerColor(color: ItemColor) = registerColor(color, { this })
    fun Block.registerColor(color: BlockColor) = registerColor(color, { this })
}