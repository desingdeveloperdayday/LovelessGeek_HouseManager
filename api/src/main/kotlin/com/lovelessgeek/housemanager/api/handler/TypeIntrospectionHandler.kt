package com.lovelessgeek.housemanager.api.handler

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.lovelessgeek.housemanager.shared.introspectType
import com.lovelessgeek.housemanager.shared.schema

class TypeIntrospectionHandler :
    RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    override fun handleRequest(
        input: APIGatewayProxyRequestEvent?,
        context: Context?
    ): APIGatewayProxyResponseEvent {
        return input.runWithQueryParam("class") { className ->
            body(schema.introspectType(className))
        }
    }
}