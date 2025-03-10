package com.ssblur.unfocused.serialization

import com.google.gson.*
import net.minecraft.resources.ResourceLocation
import java.lang.reflect.Type

object ResourceLocationSerDe: JsonSerializer<ResourceLocation>, JsonDeserializer<ResourceLocation> {
  override fun serialize(src: ResourceLocation, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
    return JsonPrimitive(src.toString())
  }

  override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): ResourceLocation {
    return ResourceLocation.parse(json.asString)
  }
}