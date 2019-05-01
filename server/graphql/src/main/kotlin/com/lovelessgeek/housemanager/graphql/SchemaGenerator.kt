package com.lovelessgeek.housemanager.graphql

import com.github.pgutkowski.kgraphql.KGraphQL
import com.github.pgutkowski.kgraphql.schema.Schema
import com.lovelessgeek.housemanager.data.Repository
import com.lovelessgeek.housemanager.shared.converters.LocalDateTimeConverter
import com.lovelessgeek.housemanager.shared.models.InstantTask
import com.lovelessgeek.housemanager.shared.models.TaskType
import com.lovelessgeek.housemanager.shared.models.User
import java.time.LocalDateTime

internal fun generateSchema(repository: Repository): Schema {
    return KGraphQL.schema {
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
            suspendResolver { id: String ->
                // 이렇게 받는게 가능하긴 한데 로직을 잘 구현하는 것이 관건이겠음.
                repository.getUserById(id)
            }
        }

        query("users") {
            suspendResolver { -> repository.getUsers() }
        }

        query("task") {
            suspendResolver { id: String ->
                repository.getTaskById(id)
            }
        }

        query("tasks") {
            suspendResolver { -> repository.getTasks() }
        }
    }
}
