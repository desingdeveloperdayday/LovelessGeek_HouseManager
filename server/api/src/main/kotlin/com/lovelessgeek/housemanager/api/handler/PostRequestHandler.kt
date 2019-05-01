package com.lovelessgeek.housemanager.api.handler

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.lovelessgeek.housemanager.api.fromJson
import com.lovelessgeek.housemanager.api.request.PostRequestParams
import com.lovelessgeek.housemanager.graphql.schema

class PostRequestHandler :
    RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    override fun handleRequest(
        input: APIGatewayProxyRequestEvent?,
        context: Context?
    ): APIGatewayProxyResponseEvent {
        println("Input: ${input?.body}")
        val body = input?.body?.fromJson<PostRequestParams>()
            ?: return error(422, "body is missing in POST request!")

        val query = body.query

        return body(schema.execute(query))
    }
}
