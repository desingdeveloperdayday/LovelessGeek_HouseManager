package com.lovelessgeek.housemanager.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable

abstract class BaseFragment(
    rx: Boolean = false
) : Fragment(), RxBase {

    private val compositeDisposable: CompositeDisposable? =
        if (rx) CompositeDisposable() else null

    override val disposables: CompositeDisposable?
        get() = compositeDisposable

    abstract val layoutId: Int
        @LayoutRes get

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables?.dispose()
    }
}