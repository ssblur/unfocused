package com.ssblur.unfocused.test

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.command.CommandRegistration.registerCommand
import com.ssblur.unfocused.event.client.ClientScreenRegistrationEvent.registerScreen
import com.ssblur.unfocused.test.menu.TestMenu
import com.ssblur.unfocused.test.screen.TestScreen
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType
import org.apache.logging.log4j.LogManager

object UnfocusedTestMod: ModInitializer("unfocusedtest") {
  val LOGGER = LogManager.getLogger(id)!!

  val MENU = registerMenu("test_menu") {
    MenuType(::TestMenu, FeatureFlagSet.of())
  }

  fun init() {
    LOGGER.info("Unfocused Test Mod loaded")

    MENU.then {
      registerScreen(it) { container, inventory, component ->
        TestScreen(container, inventory, component ?: Component.empty())
      }
    }

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
    command.source.player?.openMenu(object : MenuProvider {
      override fun getDisplayName(): Component? = Component.literal("Test Menu")

      override fun createMenu(
        i: Int,
        inventory: Inventory,
        player: Player
      ): AbstractContainerMenu {
        return TestMenu(i, inventory)
      }

    })
    return Command.SINGLE_SUCCESS
  }
}