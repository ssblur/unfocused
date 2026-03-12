package com.ssblur.unfocused

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import com.ssblur.unfocused.biome.TemplatePoolInjects
import com.ssblur.unfocused.command.CommandRegistration.registerCommand
import com.ssblur.unfocused.config.GameRuleConfig
import com.ssblur.unfocused.event.client.ClientScreenRegistrationEvent.registerScreen
import com.ssblur.unfocused.event.common.ServerStartEvent
import com.ssblur.unfocused.menu.SimpleMenuProvider
import com.ssblur.unfocused.menu.UnfocusedBookMenu
import com.ssblur.unfocused.screen.UnfocusedBookScreen
import com.ssblur.unfocused.test.UnfocusedTestMod
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.Component
import net.minecraft.world.flag.FeatureFlagSet
import net.minecraft.world.inventory.MenuType
import org.apache.logging.log4j.LogManager

object Unfocused: ModInitializer("unfocused") {
  var isNeoForge = false
  var isFabric = false
  val LOGGER = LogManager.getLogger(id)!!

  val BOOK_MENU = registerMenu("book") {
    MenuType(::UnfocusedBookMenu, FeatureFlagSet.of())
  }.then {
    registerScreen(it, ::UnfocusedBookScreen)
  }

  fun init() {
    ServerStartEvent.register { server ->
      server.addTickable {
        if (server.tickCount % 20 == 0) {
          GameRuleConfig.BOOL_RULES.map {
            GameRuleConfig.BOOL_RULES[it.key] = server.gameRules.getRule(it.key).get()
          }
          GameRuleConfig.INT_RULES.map {
            GameRuleConfig.INT_RULES[it.key] = server.gameRules.getRule(it.key).get()
          }
        }
      }
    }

    TemplatePoolInjects.init()
    ServerStartEvent.register{
      TemplatePoolInjects.inject(it)
    }

    // This gets packed into Unfocused releases, intentionally.
    // I keep having things break in release that don't break in dev. :\
    if(System.getenv("UNFOCUSED_TEST_MOD_ENABLED") == "true")
      UnfocusedTestMod.init()

    registerCommand { dispatcher, _, _ ->
      dispatcher.register(
        Commands.literal("unfocused").then(
          Commands.literal("open")
            .then(Commands.argument("page", StringArgumentType.greedyString()).executes { openCommand(it) })
            .executes { openCommandEmpty(it) }
        )
      )
      dispatcher.register(
        Commands.literal("unfocused").then(
          Commands.literal("config")
            .requires { s: CommandSourceStack -> s.hasPermission(4) }
            .executes { configCommand(it) }
        )
      )
    }
  }

  fun isModLoaded(id: String) = UtilityExpectPlatform.isModLoaded(id)

  fun openCommand(command: CommandContext<CommandSourceStack>): Int {
    command.source.player?.closeContainer()

    command.source.player?.openMenu(SimpleMenuProvider { i, inventory, _ ->
      val menu = UnfocusedBookMenu(i, inventory)
      menu.location = location(command.getArgument("page", String::class.java))
      menu
    })

    return Command.SINGLE_SUCCESS
  }

  fun openCommandEmpty(command: CommandContext<CommandSourceStack>): Int {
    command.source.player?.closeContainer()
    command.source.player?.sendSystemMessage(Component.translatable("extra.unfocused.no_page"))
    return Command.SINGLE_SUCCESS
  }

  fun configCommand(command: CommandContext<CommandSourceStack>): Int {
    command.source.player?.closeContainer()
    command.source.player?.sendSystemMessage(Component.translatable("extra.unfocused.unimplemented"))

    return Command.SINGLE_SUCCESS
  }
}