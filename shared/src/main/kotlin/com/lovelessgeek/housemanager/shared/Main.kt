package com.lovelessgeek.housemanager.shared

import com.github.pgutkowski.kgraphql.KGraphQL
import com.lovelessgeek.housemanager.shared.converters.LocalDateTimeConverter
import kotlinx.coroutines.coroutineScope
import java.time.LocalDateTime

suspend fun getUsers(): List<User> {
    return users
}

suspend fun getUserById(id: String): User? {
    return users.find { it.id == id }
}

suspend fun getTasks(): List<Task> {
    return tasks
}

suspend fun getTaskById(id: String): Task? {
    return tasks.find { it.id == id }
}

val schema = KGraphQL.schema {
    // Configuration for this getSchema
    configure {
        useDefaultPrettyPrinter = true
    }

    // List of supported types
    type<User>()
    type<InstantTask>()
    enum<TaskType>()

    // Custom scalar types
    longScalar<LocalDateTime> {
        serialize = LocalDateTimeConverter.converter
        deserialize = LocalDateTimeConverter.inverter
    }

    // Available queries
    query("user") {
        suspendResolver { id: String?, username: String? ->
            // 이렇게 받는게 가능하긴 한데 로직을 잘 구현하는 것이 관건이겠음.
            users.find { it.id == id || it.username == username }
        }
    }

    query("users") {
        suspendResolver { -> getUsers() }
    }

    query("task") {
        suspendResolver { id: String ->
            getTaskById(id)
        }
    }

    query("tasks") {
        suspendResolver { -> getTasks() }
    }
}

suspend fun main() = coroutineScope {
    println(schema.execute("{ users { id } }"))
    println(schema.execute("{ users { id username tasks { type name } } }"))
    println(schema.execute("{ user(id: \"2\") { username } }"))
    println(schema.execute("{ user(username: \"tura\") { username } }"))
    println(schema.execute("{ user(id: \"3\", username: \"tura\") { username } }"))
    println(schema.execute(askSchema("User")))
}

private fun askSchema(name: String): String {
    return """{
          __type(name: "$name") {
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
}
