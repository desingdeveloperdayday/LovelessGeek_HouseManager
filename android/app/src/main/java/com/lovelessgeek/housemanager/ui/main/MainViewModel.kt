package com.lovelessgeek.housemanager.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.lovelessgeek.housemanager.base.event.SimpleEvent

class MainViewModel : ViewModel() {

    private val _backToLogin = MutableLiveData<SimpleEvent>()
    val backToLogin: LiveData<SimpleEvent>
        get() = _backToLogin

    private var mFirebaseUser: FirebaseUser?

    init {
        mFirebaseUser = FirebaseAuth.getInstance().currentUser
            ?: run {
                _backToLogin.value = SimpleEvent()
                null
            }
    }
}