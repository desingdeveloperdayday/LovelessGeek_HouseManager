package com.lovelessgeek.housemanager.api.handler

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.lovelessgeek.housemanager.shared.toJson
import kotlin.test.Test
import kotlin.test.assertEquals

class PostRequestHandlerTest {

    private val handler = PostRequestHandler()

    @Test
    fun testSuccess() {
        val result = handler.handleRequest(
            APIGatewayProxyRequestEvent()
                .withBody(mapOf("query" to "{ users { id }}").toJson()),
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
                "users" : [ {
                  "id" : "1"
                }, {
                  "id" : "2"
                }, {
                  "id" : "3"
                } ]
              }
            }
            """.trimIndent()
        )
    }
}