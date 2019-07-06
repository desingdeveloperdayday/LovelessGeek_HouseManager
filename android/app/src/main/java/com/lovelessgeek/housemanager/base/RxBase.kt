package com.lovelessgeek.housemanager.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

interface RxBase {
    val disposables: CompositeDisposable?

    operator fun Disposable.unaryPlus() {
        disposables?.add(this) ?: throw IllegalStateException("Flag rx is not set.")
    }

    fun Disposable.disposedBy() {
        disposables?.add(this) ?: throw IllegalStateException("Flag rx is not set.")
    }
}