package com.ssblur.unfocused.data

import net.minecraft.resources.ResourceLocation

@Suppress("unused")
fun interface DataLoader<in T> {
  fun load(resource: T, location: ResourceLocation)
}