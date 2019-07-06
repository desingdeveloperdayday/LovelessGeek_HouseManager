package com.lovelessgeek.housemanager.base

import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec

class SimpleItemCallbackTest : FunSpec() {

    data class Dummy(
        val id: Int,
        val name: String,
        val rate: Float
    )

    val dataSet = listOf(
        Dummy(1, "android", 0.82f) to Dummy(1, "android", 0.82f),
        Dummy(1, "android", 0.82f) to Dummy(1, "ios", 0.54f),
        Dummy(1, "android", 0.82f) to Dummy(2, "ios", 0.54f)
    )

    init {
        test("keySelector로 id를 지정하면 id가 같은 경우에는 항상 areItemsTheSame()이 true를 반환해야 한다") {
            val itemCallback = SimpleItemCallback<Dummy> { id }

            val expected = listOf(true, true, false)

            dataSet.map { (first, second) ->
                itemCallback.areItemsTheSame(first, second)
            } shouldBe expected
        }

        test("keySelector로 id가 아닌 다른 값을 지정해도 해당 값이 같은 경우에는 항상 areItemsTheSame()이 true를 반환해야 한다") {
            val itemCallback = SimpleItemCallback<Dummy> { name }

            val expected = listOf(true, false, false)

            dataSet.map { (first, second) ->
                itemCallback.areItemsTheSame(first, second)
            } shouldBe expected
        }

        test("keySelector로 지정한 값이 같아도 다른 값이 다르면 areContentsTheSame()이 false를 반환해야 한다") {
            val itemCallback = SimpleItemCallback<Dummy> { id }

            val expected = listOf(true, false, false)

            dataSet.map { (first, second) ->
                itemCallback.areContentsTheSame(first, second)
            } shouldBe expected
        }
    }
}