package serialization

import com.google.gson.GsonBuilder
import com.google.gson.ToNumberPolicy
import net.minecraft.resources.ResourceLocation
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredMemberProperties

object GsonBuilder {
  fun builder(): GsonBuilder {
    return GsonBuilder()
      .disableHtmlEscaping()
      .setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
      .registerTypeAdapter(ResourceLocation::class.java, ResourceLocationSerDe)
  }

  fun streamBuilder(type: KClass<*>): GsonBuilder {
    return builder()
      .setFieldNamingStrategy {
        if (type.declaredMemberProperties.indexOfFirst { f -> f.name == it.name } >= 0)
          type.declaredMemberProperties.indexOfFirst { f -> f.name == it.name }.toString()
        else it.name
      }
  }
}