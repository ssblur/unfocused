package com.ssblur.unfocused.fabric

import com.ssblur.unfocused.Unfocused
import com.ssblur.unfocused.biome.BiomeModifiers
import com.ssblur.unfocused.command.CommandRegistration
import com.ssblur.unfocused.entity.EntityAttributes
import com.ssblur.unfocused.entity.Trades
import com.ssblur.unfocused.event.common.LootTablePopulateEvent
import com.ssblur.unfocused.event.common.PlayerChatEvent
import com.ssblur.unfocused.event.common.ServerStartEvent
import com.ssblur.unfocused.fabric.events.UnfocusedModData
import com.ssblur.unfocused.registry.RegistryTypes
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.loot.v3.LootTableEvents
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.trade.TradeOfferHelper
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
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
    RegistryTypes.MENUS.subscribe{ location, supplier ->
      Registry.register(BuiltInRegistries.MENU, location, supplier.get())
    }
    RegistryTypes.ARMOR.subscribe{ location, supplier ->
      Registry.register(BuiltInRegistries.ARMOR_MATERIAL, location, supplier.get())
    }

    ServerMessageEvents.ALLOW_CHAT_MESSAGE.register { message, sender, params ->
      PlayerChatEvent.Before.callback(
        PlayerChatEvent.PlayerChatMessage(
          sender,
          message.decoratedContent(),
          PlayerChatEvent.Before
        )
      )
      !PlayerChatEvent.Before.isCancelled()
    }
    ServerMessageEvents.CHAT_MESSAGE.register { message, sender, params ->
      PlayerChatEvent.After.callback(
        PlayerChatEvent.PlayerChatMessage(
          sender,
          message.decoratedContent(),
          PlayerChatEvent.After
        )
      )
    }


    ServerLifecycleEvents.SERVER_STARTED.register(ServerStartEvent::callback)

    LootTableEvents.MODIFY.register { key, builder, source, provider ->
      val pools = mutableListOf<LootPool.Builder>()
      LootTablePopulateEvent.callback(LootTablePopulateEvent.LootTableContext(key, source.isBuiltin, pools))
      pools.forEach { builder.withPool(it) }
    }

    EntityAttributes.register { (type, builder) ->
      FabricDefaultAttributeRegistry.register(type.get(), builder.get())
    }

    BiomeModifiers.featureEvent.register{ (_, modification) ->
      BiomeModifications.addFeature(
        { modification.isValid(it.biomeRegistryEntry) },
        modification.step,
        ResourceKey.create(Registries.PLACED_FEATURE, modification.feature)
      )
    }
    BiomeModifiers.carverEvent.register{ (_, modification) ->
      BiomeModifications.addCarver(
        { modification.isValid(it.biomeRegistryEntry) },
        modification.step,
        ResourceKey.create(Registries.CONFIGURED_CARVER, modification.carver)
      )
    }
    BiomeModifiers.spawnEvent.register{ (_, modification) ->
      for (entity in modification.spawners)
        BiomeModifications.addSpawn(
          { modification.isValid(it.biomeRegistryEntry) },
          BuiltInRegistries.ENTITY_TYPE.get(entity.type).category,
          BuiltInRegistries.ENTITY_TYPE.get(entity.type),
          entity.weight,
          entity.minCount,
          entity.maxCount
        )
    }

    Trades.register { trade ->
      if (trade.profession != null)
        TradeOfferHelper.registerVillagerOffers(trade.profession, trade.rarity) { list -> list.add(trade.trade) }
      else
        TradeOfferHelper.registerWanderingTraderOffers(trade.rarity) { list -> list.add(trade.trade) }
    }

    CommandRegistration.register {
      CommandRegistrationCallback.EVENT.register { dispatcher, access, environment ->
        it.callback(dispatcher, access, environment)
      }
    }

    UnfocusedModNetworking.init()
    UnfocusedModData.init()
  }
}