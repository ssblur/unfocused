package com.ssblur.unfocused.extension

import net.minecraft.client.Minecraft
import net.minecraft.client.resources.sounds.SimpleSoundInstance
import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent

object SoundEventExtension {
  fun Holder<SoundEvent>.play(volume: Float = 1.0f) =
    Minecraft.getInstance().soundManager.play(SimpleSoundInstance.forUI(this, volume))
}