package com.ssblur.unfocused.event.common

import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.server.level.ServerPlayer

object PlayerJoinedEvent: SimpleEvent<ServerPlayer>(false, false)