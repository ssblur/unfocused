package com.ssblur.unfocused.event.common

import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.server.level.ServerPlayer

@Suppress("unused")
object PlayerJoinedEvent: SimpleEvent<ServerPlayer>(false)