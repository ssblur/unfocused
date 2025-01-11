package com.ssblur.unfocused.fabric

import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.entity.EntityAttributes
import com.ssblur.unfocused.event.common.LootTablePopulateEvent
import com.ssblur.unfocused.event.common.PlayerChatEvent
import com.ssblur.unfocused.event.common.ServerStartEvent
import com.ssblur.unfocused.fabric.biome.BiomeModifiers
import com.ssblur.unfocused.fabric.events.UnfocusedModData
import com.ssblur.unfocused.registry.RegistryTypes
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.loot.v3.LootTableEvents
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.level.levelgen.GenerationStep
import net.minecraft.world.level.storage.loot.LootPool

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
        RegistryTypes.PARTICLE_TYPES.subscribe { location, supplier ->
            Registry.register(BuiltInRegistries.PARTICLE_TYPE, location, supplier.get())
        }

        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register{ message, sender, params ->
            PlayerChatEvent.Before.callback(PlayerChatEvent.PlayerChatMessage(sender, message.decoratedContent(), PlayerChatEvent.Before))
            !PlayerChatEvent.Before.isCancelled()
        }
        ServerMessageEvents.CHAT_MESSAGE.register{ message, sender, params ->
            PlayerChatEvent.After.callback(PlayerChatEvent.PlayerChatMessage(sender, message.decoratedContent(), PlayerChatEvent.After))
        }

        ServerLifecycleEvents.SERVER_STARTED.register(ServerStartEvent::callback)

        LootTableEvents.MODIFY.register{ key, builder, source, provider ->
            val pools = mutableListOf<LootPool.Builder>()
            LootTablePopulateEvent.callback(LootTablePopulateEvent.LootTableContext(key, source.isBuiltin, pools))
            pools.forEach { builder.withPool(it) }
        }

        EntityAttributes.register{ (type, builder) ->
            FabricDefaultAttributeRegistry.register(type.get(), builder.get())
        }

        BiomeModifiers.event.register{ (_, modification) ->
            when (modification.type) {
                "unfocused:add_feature" -> {
                    BiomeModifications.addFeature(
                        {
                            if(modification.biomes.startsWith("#"))
                                it.hasTag(TagKey.create(Registries.BIOME, ResourceLocation.parse(modification.biomes.substring(1))))
                            it.biomeKey.location().equals(modification.biomes)
                        },
                        GenerationStep.Decoration.entries.first { it.getName() == modification.step!! },
                        ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.parse(modification.feature!!))
                    )
                }
                "unfocused:remove_feature" -> {
                    // todo
                }
                "unfocused:add_spawn" -> {
                    for(entity in modification.spawners!!)
                        BiomeModifications.addSpawn(
                            {
                                if(modification.biomes.startsWith("#"))
                                    it.hasTag(TagKey.create(Registries.BIOME, ResourceLocation.parse(modification.biomes.substring(1))))
                                it.biomeKey.location().equals(modification.biomes)
                            },
                            BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(entity.type)).category,
                            BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(entity.type)),
                            entity.weight,
                            entity.minCount,
                            entity.maxCount
                        )
                }
                "unfocused:remove_spawn" -> {
                    // todo
                }
                "unfocused:add_carver" -> {
                    BiomeModifications.addCarver(
                        {
                            if(modification.biomes.startsWith("#"))
                                it.hasTag(TagKey.create(Registries.BIOME, ResourceLocation.parse(modification.biomes.substring(1))))
                            it.biomeKey.location().equals(modification.biomes)
                        },
                        GenerationStep.Carving.entries.first { it.getName() == modification.step!! },
                        ResourceKey.create(Registries.CONFIGURED_CARVER, ResourceLocation.parse(modification.carvers!!))
                    )
                }
                "unfocused:remove_carver" -> {
                    // todo
                }
            }
        }

        UnfocusedModNetworking.init()
        UnfocusedModData.init()
    }
}