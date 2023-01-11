package ru.kamal.testit.plugin.data.network.moshi

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.adapters.EnumJsonAdapter
import java.lang.reflect.Type

/**
 * EnumAdapterFactory для безопасной обработки енама
 * Использует для EnumJsonAdapter из Moshi, который парсит енам, пришедший в DTO от сервера
 * Если пришло значение, которого нет в енаме, то вернет это null
 * Необходимо добавить адаптер в Moshi, тк поведение по умолчанию при отсутсвии енама, вызывает эксепшен
 */
class EnumAdapterFactory : JsonAdapter.Factory {
    override fun create(type: Type, annotations: MutableSet<out Annotation>, moshi: Moshi): JsonAdapter<*>? {
        return if (Types.getRawType(type).isEnum) {
            EnumJsonAdapter.create(type as Class<out Enum<*>>).withUnknownFallback(null).nullSafe().lenient()
        } else {
            return moshi.nextAdapter<Class<*>>(this, type, annotations)
        }
    }
}