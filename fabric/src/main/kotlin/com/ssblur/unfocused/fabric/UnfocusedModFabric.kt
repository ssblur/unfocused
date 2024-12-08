package com.ssblur.unfocused.fabric

import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.registry.RegistryTypes
import net.fabricmc.api.ModInitializer
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries

class UnfocusedModFabric: ModInitializer {
    override fun onInitialize() {
        Unfocused.isFabric = true

        Unfocused.init()
        RegistryTypes.BLOCK.subscribe { location, supplier ->
            Registry.register(BuiltInRegistries.BLOCK, location, supplier.get())
        }
        RegistryTypes.ITEM.subscribe { location, supplier ->
            Registry.register(BuiltInRegistries.ITEM, location, supplier.get())
        }
        RegistryTypes.EFFECTS.subscribe { location, supplier ->
            Registry.register(BuiltInRegistries.MOB_EFFECT, location, supplier.get())
        }
        RegistryTypes.BLOCK_ENTITIES.subscribe { location, supplier ->
            Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, location, supplier.get())
        }
        RegistryTypes.DATA_COMPONENTS.subscribe { location, supplier ->
            Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, location, supplier.get())
        }
        RegistryTypes.ENTITIES.subscribe { location, supplier ->
            Registry.register(BuiltInRegistries.ENTITY_TYPE, location, supplier.get())
        }
        RegistryTypes.FEATURES.subscribe { location, supplier ->
            Registry.register(BuiltInRegistries.FEATURE, location, supplier.get())
        }

        UnfocusedModNetworking.init()
    }
}