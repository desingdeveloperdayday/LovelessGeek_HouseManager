package com.lovelessgeek.housemanager.data.db

interface Mapper<Item, Entity> {
    fun toEntity(item: Item): Entity
    fun fromEntity(entity: Entity): Item
}