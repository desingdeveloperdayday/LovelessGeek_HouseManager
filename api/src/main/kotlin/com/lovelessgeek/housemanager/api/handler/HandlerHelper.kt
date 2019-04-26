package com.lovelessgeek.housemanager.api.handler

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.lovelessgeek.housemanager.api.response.ErrorBody
import com.lovelessgeek.housemanager.shared.toJson

fun APIGatewayProxyRequestEvent?.runWithQueryParam(
    queryParam: String,
    block: (String) -> APIGatewayProxyResponseEvent
): APIGatewayProxyResponseEvent {
    return this?.queryStringParameters?.get(queryParam)?.let(block) ?: error(
        422,
        "query parameter '$queryParam' is missing."
    )
}

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