package com.ssblur.unfocused.fabric

import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.entity.EntityAttributes
import com.ssblur.unfocused.event.common.PlayerChatEvent
import com.ssblur.unfocused.event.common.ServerStartEvent
import com.ssblur.unfocused.fabric.events.UnfocusedModData
import com.ssblur.unfocused.registry.RegistryTypes
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
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
        RegistryTypes.LOOT_FUNCTION_TYPES.subscribe { location, supplier ->
            Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, location, supplier.get())
        }
        RegistryTypes.LOOT_CONDITION_TYPES.subscribe { location, supplier ->
            Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, location, supplier.get())
        }
        RegistryTypes.TRIGGER_TYPES.subscribe { location, supplier ->
            Registry.register(BuiltInRegistries.TRIGGER_TYPES, location, supplier.get())
        }
        RegistryTypes.RECIPE_SERIALIZERS.subscribe { location, supplier ->
            Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, location, supplier.get())
        }
        RegistryTypes.CREATIVE_TABS.subscribe { location, supplier ->
            Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, location, supplier.get())
        }

        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register{ message, sender, params ->
            PlayerChatEvent.Before.callback(PlayerChatEvent.PlayerChatMessage(sender, message.decoratedContent(), PlayerChatEvent.Before))
            !PlayerChatEvent.Before.isCancelled()
        }
        ServerMessageEvents.CHAT_MESSAGE.register{ message, sender, params ->
            PlayerChatEvent.After.callback(PlayerChatEvent.PlayerChatMessage(sender, message.decoratedContent(), PlayerChatEvent.After))
        }

        ServerLifecycleEvents.SERVER_STARTED.register{
            ServerStartEvent.callback(it)
        }

        EntityAttributes.register{ (type, builder) ->
            FabricDefaultAttributeRegistry.register(type.get(), builder.get())
        }

        UnfocusedModNetworking.init()
        UnfocusedModData.init()
    }
}