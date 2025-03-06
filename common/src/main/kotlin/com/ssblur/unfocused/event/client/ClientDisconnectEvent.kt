package com.ssblur.unfocused.event.client

import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.client.player.LocalPlayer

object ClientDisconnectEvent: SimpleEvent<LocalPlayer?>(false) {}