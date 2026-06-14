package com.ssblur.unfocused.serialization

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.lang.reflect.ParameterizedType
import java.util.Optional

class OptionalTypeAdapter<T: Any>(val base: TypeAdapter<T>): TypeAdapter<Optional<T>>() {
  override fun write(writer: JsonWriter, value: Optional<T>?) {
    if(value == null || value.isEmpty) writer.nullValue()
    base.write(writer, value!!.get())
  }

  override fun read(reader: JsonReader): Optional<T> {
    if(reader.peek() == JsonToken.NULL) return Optional.empty()
    return Optional.ofNullable(base.read(reader))
  }

  companion object: TypeAdapterFactory {
    override fun <T : Any?> create(
      gson: Gson,
      type: TypeToken<T?>?
    ): TypeAdapter<T?>? {
      if(type == null || type.rawType != Optional::class.java) return null
      val baseClass = (type.type as ParameterizedType).actualTypeArguments.first()
      val base = gson.getAdapter(TypeToken.get(baseClass))
      @Suppress("UNCHECKED_CAST")
      return OptionalTypeAdapter<Any>(base as TypeAdapter<Any>) as TypeAdapter<T?>
    }
  }
}