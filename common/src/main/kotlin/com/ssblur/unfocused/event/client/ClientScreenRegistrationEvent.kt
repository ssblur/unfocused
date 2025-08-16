package com.ssblur.unfocused.event.client

import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.event.SimpleEvent
import net.minecraft.client.gui.screens.MenuScreens
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.MenuAccess
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

object ClientScreenRegistrationEvent: SimpleEvent<ClientScreenRegistrationEvent.Context<in AbstractContainerMenu>>(true) {
  data class Context<T: AbstractContainerMenu> (
    val menu: MenuType<T>,
    val supplier: MenuScreens.ScreenConstructor<T, *>
  )

  @Suppress("UNCHECKED_CAST", "unused", "unusedreceiverparameter")
  fun <T: AbstractContainerMenu, U> ModInitializer.registerScreen(menu: MenuType<T>, supplier: MenuScreens.ScreenConstructor<T, U>)
    where U: Screen, U: MenuAccess<T> {
    callback(Context(menu, supplier) as Context<in AbstractContainerMenu>)
  }
}