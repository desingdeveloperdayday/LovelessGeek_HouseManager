package com.lovelessgeek.housemanager.api.handler

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import kotlin.test.Test
import kotlin.test.assertEquals

class GetRequestHandlerTest {

    private val handler = GetRequestHandler()

    @Test
    fun testSuccess() {
        val result = handler.handleRequest(
            APIGatewayProxyRequestEvent()
                .withQueryStringParameters(mapOf("query" to "{ users { id }}")),
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