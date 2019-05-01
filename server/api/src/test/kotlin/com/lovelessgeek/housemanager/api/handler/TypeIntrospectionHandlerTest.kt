package com.lovelessgeek.housemanager.api.handler

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import kotlin.test.Test
import kotlin.test.assertEquals

class TypeIntrospectionHandlerTest {

    private val handler = TypeIntrospectionHandler()

    @Test
    fun testSuccess() {
        val result = handler.handleRequest(
            APIGatewayProxyRequestEvent()
                .withQueryStringParameters(mapOf("class" to "User")),
            null
        )

        assertEquals(result.statusCode, 200)
        assertEquals(result.headers["Content-Type"], "application/json")
        assertEquals(
            actual = result.body,
            //language=JSON
            expected = """
{
  "data" : {
    "__type" : {
      "name" : "User",
      "fields" : [ {
        "name" : "id",
        "type" : {
          "name" : null,
          "kind" : "NON_NULL",
          "ofType" : {
            "name" : "String",
            "kind" : "SCALAR"
          }
        }
      }, {
        "name" : "tasks",
        "type" : {
          "name" : null,
          "kind" : "NON_NULL",
          "ofType" : {
            "name" : null,
            "kind" : "LIST"
          }
        }
      }, {
        "name" : "username",
        "type" : {
          "name" : null,
          "kind" : "NON_NULL",
          "ofType" : {
            "name" : "String",
            "kind" : "SCALAR"
          }
        }
      } ]
    }
  }
}
            """.trimIndent()
        )
    }

    @Test
    fun testFailureForUnknownType() {
        val result = handler.handleRequest(
            APIGatewayProxyRequestEvent()
                .withQueryStringParameters(mapOf("class" to "asdf")),
            null
        )

        assertEquals(result.statusCode, 200)
        assertEquals(result.headers["Content-Type"], "application/json")
        assertEquals(
            actual = result.body,
            //language=JSON
            expected = """
{
  "data" : {
    "__type" : null
  }
}
            """.trimIndent()
        )
    }
}