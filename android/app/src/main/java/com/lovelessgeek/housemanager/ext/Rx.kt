package com.lovelessgeek.housemanager.ext

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

fun Disposable.disposedBy(disposables: CompositeDisposable) {
    disposables.add(this)
}

fun <T> Observable<T>.preventMultipleEmission(
    duration: Long = 300L
): Observable<T> =
    throttleFirst(duration, TimeUnit.MILLISECONDS)