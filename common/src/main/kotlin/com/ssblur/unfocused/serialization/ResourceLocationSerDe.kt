package com.ssblur.unfocused.serialization

import com.google.gson.*
import net.minecraft.resources.Identifier
import java.lang.reflect.Type

object IdentifierSerDe: JsonSerializer<Identifier>, JsonDeserializer<Identifier> {
  override fun serialize(src: Identifier, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
    return JsonPrimitive(src.toString())
  }

  override fun deserialize(json: JsonElement, typeOfT: Type?, context: JsonDeserializationContext?): Identifier {
    return Identifier.parse(json.asString)
  }
}