package com.lovelessgeek.housemanager.api.handler

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.lovelessgeek.housemanager.shared.fromJson
import com.lovelessgeek.housemanager.shared.schema

class PostRequestHandler :
    RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    override fun handleRequest(
        input: APIGatewayProxyRequestEvent?,
        context: Context?
    ): APIGatewayProxyResponseEvent {
        val body = input?.body?.fromJson<Map<String, String>>()
            ?: return error(422, "body is missing in POST request!")

        val query: String =
            body["query"]?.let(this::parseQuery) ?: return error(
                422,
                "'query' is missing in request body."
            )
        val variables: String? = body["variables"]

        return body(schema.execute(query, variables))
    }

    private fun parseQuery(it: String): String {
        return if (it.startsWith("query"))
            it.substringAfter(" ").trim()
        else
            it
    }
}