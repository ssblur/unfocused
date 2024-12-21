package com.ssblur.unfocused.data

import net.minecraft.resources.ResourceLocation

@Suppress("unused")
interface DataLoader<T> {
    fun load(resource: T, location: ResourceLocation)
}