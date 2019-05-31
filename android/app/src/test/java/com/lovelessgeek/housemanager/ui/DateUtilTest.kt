package com.lovelessgeek.housemanager.ui

import io.kotlintest.shouldBe
import io.kotlintest.specs.FunSpec

class DateUtilTest : FunSpec() {

    init {
        test("현재 시간보다 나중의 시간이 들어왔을 때는 isOverDue()가 true를 반환해야 한다") {
            val today = makeTime(month = 1, day = 10)
            val justAfter = makeTime(month = 1, day = 10, hour = 1)
            val tomorrow = makeTime(month = 1, day = 11)
            val monthLater = makeTime(month = 2, day = 10)

            today.isOverDue(tomorrow) shouldBe true
            today.isOverDue(justAfter) shouldBe true
            today.isOverDue(monthLater) shouldBe true
        }

        test("하루 이내라면 isOneDayLeft가 true를 반환해야 한다") {
            val today = makeTime(day = 15)
            val hourAfter = makeTime(day = 15, hour = 1)
            val twoDayAfter = makeTime(day = 17)

            hourAfter.lessThanOneDayLeft(today) shouldBe true
            twoDayAfter.lessThanOneDayLeft(today) shouldBe false
        }
    }
}