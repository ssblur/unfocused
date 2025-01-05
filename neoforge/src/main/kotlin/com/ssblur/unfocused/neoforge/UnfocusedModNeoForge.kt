package com.ssblur.unfocused.neoforge

import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.neoforge.events.UnfocusedModData
import com.ssblur.unfocused.neoforge.events.UnfocusedModEvents
import com.ssblur.unfocused.neoforge.registry.ProxyRegistry
import com.ssblur.unfocused.registry.RegistryTypes
import net.minecraft.core.registries.Registries
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.common.NeoForge

@Mod("unfocused")
class UnfocusedModNeoForge(bus: IEventBus) {
    init {
        Unfocused.isNeoForge = true

        NeoForge.EVENT_BUS.addListener(UnfocusedModData::event)
        UnfocusedModEvents.register(bus)

        ProxyRegistry(Registries.BLOCK, RegistryTypes.BLOCK).register(bus)
        ProxyRegistry(Registries.ITEM, RegistryTypes.ITEM).register(bus)
        ProxyRegistry(Registries.MOB_EFFECT, RegistryTypes.EFFECTS).register(bus)
        ProxyRegistry(Registries.BLOCK_ENTITY_TYPE, RegistryTypes.BLOCK_ENTITIES).register(bus)
        ProxyRegistry(Registries.DATA_COMPONENT_TYPE, RegistryTypes.DATA_COMPONENTS).register(bus)
        ProxyRegistry(Registries.ENTITY_TYPE, RegistryTypes.ENTITIES).register(bus)
        ProxyRegistry(Registries.FEATURE, RegistryTypes.FEATURES).register(bus)
        ProxyRegistry(Registries.LOOT_CONDITION_TYPE, RegistryTypes.LOOT_CONDITION_TYPES).register(bus)
        ProxyRegistry(Registries.LOOT_FUNCTION_TYPE, RegistryTypes.LOOT_FUNCTION_TYPES).register(bus)

        Unfocused.init()
    }
}