package grade.xyj.com.util.extend

import com.google.gson.Gson
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ParameterizedTypeImpl(private val clz: Class<*>) : ParameterizedType {
    override fun getRawType(): Type = List::class.java
    override fun getOwnerType(): Type? = null
    override fun getActualTypeArguments(): Array<Type> = arrayOf(clz)
}

inline fun <reified T> String.toBeanList(): List<T> = Gson().fromJson<List<T>>(this, ParameterizedTypeImpl(T::class.java))

inline fun <reified T> String.toBean(): T = Gson().fromJson(this, T::class.java)

fun Any.toJson(): String = Gson().toJson(this)
