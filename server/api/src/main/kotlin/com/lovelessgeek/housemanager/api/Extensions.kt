package com.lovelessgeek.housemanager.api

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
