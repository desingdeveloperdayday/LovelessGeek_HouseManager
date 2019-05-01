package com.lovelessgeek.housemanager.graphql

import com.github.pgutkowski.kgraphql.schema.Schema

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