package com.ssblur.unfocused.tab

import com.ssblur.unfocused.ModInitializer
import com.ssblur.unfocused.UtilityExpectPlatform
import com.ssblur.unfocused.event.SimpleEvent
import com.ssblur.unfocused.registry.RegistrySupplier
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import java.util.function.Supplier

@Suppress("unused")
object CreativeTabs {
    private val acceptor: SimpleEvent<CreativeTabEntry> = SimpleEvent(retroactive = true)
    data class CreativeTabEntry(val id: ResourceLocation, val item: ItemLike?, val stack: ItemStack?)
    fun ModInitializer.registerCreativeTab(id: String, supplier: Supplier<CreativeModeTab>): RegistrySupplier<CreativeModeTab> {
        return CREATIVE_TABS.register(id, supplier)
    }

    fun ModInitializer.registerCreativeTab(id: String, name: String, icon: Supplier<ItemLike>): RegistrySupplier<CreativeModeTab> {
        return registerCreativeTab(id){
            UtilityExpectPlatform.creativeTabBuilder()
                .title(Component.translatable(name))
                .icon{ ItemStack(icon.get().asItem()) }
                .displayItems { itemDisplayParameters, output ->
                    acceptor.register{
                        if(it.id == location(id)) {
                            assert((it.stack != null) xor (it.item != null))
                            output.accept(it.stack ?: ItemStack(it.item!!))
                        }
                    }
                }
                .build()
        }
    }

    fun Item.tab(supplier: RegistrySupplier<CreativeModeTab>): Item {
        acceptor.callback(CreativeTabEntry(supplier.location!!, this, null))
        return this
    }

    fun Item.tab(location: ResourceLocation): Item {
        acceptor.callback(CreativeTabEntry(location, this, null))
        return this
    }

    fun RegistrySupplier<Item>.tab(supplier: RegistrySupplier<CreativeModeTab>): RegistrySupplier<Item> {
        this.wait{
            acceptor.callback(CreativeTabEntry(supplier.location!!, this.value, null))
        }
        return this
    }

    fun RegistrySupplier<Item>.tab(location: ResourceLocation): RegistrySupplier<Item> {
        this.wait{
            acceptor.callback(CreativeTabEntry(location, this.value, null))
        }
        return this
    }

    fun RegistrySupplier<CreativeModeTab>.add(item: ItemLike): RegistrySupplier<CreativeModeTab> {
        acceptor.callback(CreativeTabEntry(location!!, item, null))
        return this
    }

    fun RegistrySupplier<CreativeModeTab>.add(item: ItemStack): RegistrySupplier<CreativeModeTab> {
        acceptor.callback(CreativeTabEntry(location!!, null, item))
        return this
    }
}