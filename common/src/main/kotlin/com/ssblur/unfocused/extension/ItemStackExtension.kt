package com.ssblur.unfocused.extension

import net.minecraft.core.Holder
import net.minecraft.core.HolderSet
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import java.util.function.Predicate

@Suppress("unused")
object ItemStackExtension {
  infix fun ItemStack.matches(item: Item): Boolean {
    return this.`is`(item)
  }

  infix fun ItemStack.matches(key: TagKey<Item>): Boolean {
    return this.`is`(key)
  }

  infix fun ItemStack.matches(predicate: Predicate<Holder<Item>>): Boolean {
    return this.`is`(predicate)
  }

  infix fun ItemStack.matches(holder: Holder<Item>): Boolean {
    return this.`is`(holder)
  }

  infix fun ItemStack.matches(holderSet: HolderSet<Item>): Boolean {
    return this.`is`(holderSet)
  }
}