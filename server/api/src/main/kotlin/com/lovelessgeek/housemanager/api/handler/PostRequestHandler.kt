package com.lovelessgeek.housemanager.api.handler

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestHandler
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent
import com.github.pgutkowski.kgraphql.ExecutionException
import com.github.pgutkowski.kgraphql.RequestException
import com.lovelessgeek.housemanager.api.fromJson
import com.lovelessgeek.housemanager.api.request.PostRequestParams
import com.lovelessgeek.housemanager.api.toJson
import com.lovelessgeek.housemanager.graphql.schema

class PostRequestHandler :
    RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    override fun handleRequest(
        input: APIGatewayProxyRequestEvent?,
        context: Context?
    ): APIGatewayProxyResponseEvent {
        println("Input: ${input?.body}")
        val body = input?.body?.fromJson<PostRequestParams>()
            ?: return error(400, "body is missing in POST request!")

        val query = body.query
        val variables = body.variables.toJson()

        return try {
            body(schema.execute(query, variables))
        } catch (e: Exception) {
            when (e) {
                is RequestException,
                is ExecutionException,
                is IllegalArgumentException -> {
                    System.err.println(e.message)
                    error(400, e.message ?: "")
                }
                else -> {
                    e.printStackTrace()
                    error(500, e.message ?: "")
                }
            }
        }
    }
}
