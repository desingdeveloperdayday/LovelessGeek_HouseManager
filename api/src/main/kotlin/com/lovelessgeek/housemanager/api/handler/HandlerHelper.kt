package com.lovelessgeek.housemanager.api.handler

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.lovelessgeek.housemanager.api.response.ErrorBody
import com.lovelessgeek.housemanager.shared.toJson

fun error(statusCode: Int, message: String): APIGatewayProxyResponseEvent {
    return APIGatewayProxyResponseEvent()
        .withStatusCode(statusCode)
        .withHeaders(headers())
        .withBody(ErrorBody(statusCode, message).toJson())
}

fun body(content: String): APIGatewayProxyResponseEvent {
    return APIGatewayProxyResponseEvent()
        .withStatusCode(200)
        .withHeaders(headers())
        .withBody(content)
}

private fun headers() = mapOf(
    "Content-Type" to "application/json"
)