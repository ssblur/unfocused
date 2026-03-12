package com.ssblur.unfocused.test

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.command.CommandRegistration.registerCommand
import com.ssblur.unfocused.event.client.ClientScreenRegistrationEvent.registerScreen
import com.ssblur.unfocused.menu.SimpleMenuProvider
import com.ssblur.unfocused.test.menu.TestMenu
import com.ssblur.unfocused.test.screen.TestScreen
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.inventory.MenuType
import org.apache.logging.log4j.LogManager

object UnfocusedTestMod: ModInitializer("unfocusedtest") {
  val LOGGER = LogManager.getLogger(id)!!

  val MENU = registerMenu("test_menu") {
    MenuType(::TestMenu, FeatureFlagSet.of())
  }.then {
    registerScreen(it, ::TestScreen)
  }

  fun init() {
    LOGGER.info("Unfocused Test Mod loaded")

    registerCommand { dispatcher, registry, selection ->
      dispatcher.register(
        Commands.literal("unfocused_test").then(
          Commands.literal("menu")
            .requires { s: CommandSourceStack -> s.hasPermission(4) }
            .executes { testMenuCommand(it) }
        )
      )
    }
  }

  fun testMenuCommand(command: CommandContext<CommandSourceStack>): Int {
    command.source.player?.openMenu(SimpleMenuProvider { i, inventory, _ ->
        TestMenu(i, inventory)
    })
    return Command.SINGLE_SUCCESS
  }
}