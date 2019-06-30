package com.lovelessgeek.housemanager.ext

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
}

fun Disposable.disposedBy(disposables: CompositeDisposable) {
    disposables.add(this)
}