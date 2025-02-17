package com.ssblur.unfocused.event.client

import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.event.SimpleEvent
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.MenuAccess
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.MenuType

@Environment(EnvType.CLIENT)
object ClientScreenRegistrationEvent: SimpleEvent<ClientScreenRegistrationEvent.Context<in AbstractContainerMenu>>(true) {
  fun interface ScreenConstructor<T: AbstractContainerMenu, U> where U: Screen, U: MenuAccess<T> {
    fun create(abstractContainerMenu: T, inventory: Inventory?, component: Component?): U
  }

  data class Context<T: AbstractContainerMenu> (
    val menu: MenuType<T>,
    val supplier: ScreenConstructor<T, *>
  )

  @Suppress("UNCHECKED_CAST", "unused", "unusedreceiverparameter")
  fun <T: AbstractContainerMenu, U> ModInitializer.registerScreen(menu: MenuType<T>, supplier: ScreenConstructor<T, U>)
    where U: Screen, U: MenuAccess<T> {
    ClientScreenRegistrationEvent.callback(Context(menu, supplier) as Context<in AbstractContainerMenu>)
  }
}