package com.lovelessgeek.housemanager.graphql

import com.lovelessgeek.housemanager.data.InMemoryRepository
import kotlinx.coroutines.coroutineScope

val schema = generateSchema(InMemoryRepository())

suspend fun main() = coroutineScope {
    println(schema.execute("{ users { id } }"))
    println(schema.execute("{ users { id username tasks { type name } } }"))
    println(schema.execute("{ user(id: \"2\") { username } }"))
    println(schema.execute("{ user(username: \"tura\") { username } }"))
    println(schema.execute("{ user(id: \"3\", username: \"tura\") { username } }"))
    println(schema.introspectType("asdf"))
}