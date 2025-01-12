package com.ssblur.unfocused.command

import com.mojang.brigadier.CommandDispatcher
import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.commands.CommandBuildContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands

object CommandRegistration: SimpleEvent<CommandRegistration.CommandRegistrationCallback>(retroactive = true) {
    fun interface CommandRegistrationCallback {
        fun callback(
            dispatcher: CommandDispatcher<CommandSourceStack>,
            registry: CommandBuildContext,
            selection: Commands.CommandSelection
        )
    }
}