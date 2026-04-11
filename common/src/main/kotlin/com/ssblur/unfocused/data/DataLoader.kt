package com.ssblur.unfocused.data

import net.minecraft.resources.Identifier

@Suppress("unused")
fun interface DataLoader<in T> {
  fun load(resource: T, location: Identifier)
}