package com.ssblur.unfocused.event.common

import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.server.MinecraftServer

object ServerStartEvent: SimpleEvent<MinecraftServer>(false, true)