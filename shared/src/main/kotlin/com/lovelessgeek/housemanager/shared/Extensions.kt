package com.lovelessgeek.housemanager.shared

import com.github.pgutkowski.kgraphql.schema.Schema
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

val gsonBuilder = GsonBuilder()
    .setLenient()
    .serializeNulls()

val gson = gsonBuilder.create()

fun Any?.toJson(): String =
    gson.toJson(this)

inline fun <reified T> String.fromJson(): T =
    gson.fromJson(this, T::class.java)

inline fun <reified T> String.frmoJsonList(): T =
    gson.fromJson(this, object : TypeToken<T>() {}.type)

fun Schema.introspectType(className: String): String {
    return execute(
        """
{
  __type(name: "$className") {
    name
    fields {
      name
      type {
        name
        kind
        ofType {
          name
          kind
        }
      }
    }
  }
}
    """.trimIndent()
    )
}