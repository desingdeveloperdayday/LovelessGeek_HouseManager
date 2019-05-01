package com.lovelessgeek.housemanager.api.request

data class PostRequestParams(
    val query: String,
    val variables: Map<String, Any> = mutableMapOf()
)