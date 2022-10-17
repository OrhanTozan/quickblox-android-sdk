package com.quickblox.sample.chat.kotlin

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.quickblox.auth.session.QBSettings
import com.quickblox.sample.chat.kotlin.managers.BackgroundListener
import com.quickblox.sample.chat.kotlin.utils.ActivityLifecycle

// chat settings
const val USER_DEFAULT_PASSWORD = "quickblox"
const val CHAT_PORT = 5223
const val SOCKET_TIMEOUT = 300
const val KEEP_ALIVE: Boolean = true
const val USE_TLS: Boolean = true
const val AUTO_JOIN: Boolean = false
const val AUTO_MARK_DELIVERED: Boolean = true
const val RECONNECTION_ALLOWED: Boolean = true
const val ALLOW_LISTEN_NETWORK: Boolean = true

// app credentials
private const val APPLICATION_ID = "86961"
private const val AUTH_KEY = "vaKBKD8VhOWxe74"
private const val AUTH_SECRET = "kHwgXA7qzh-OQxX"
private const val ACCOUNT_KEY = "XGpsVL5JZzHoMGW5ZmY4"

// chat credentials range
private const val MAX_PORT_VALUE = 65535
private const val MIN_PORT_VALUE = 1000
private const val MIN_SOCKET_TIMEOUT = 300
private const val MAX_SOCKET_TIMEOUT = 60000

class App : Application() {
    companion object {
        private lateinit var instance: App

        fun getInstance(): App = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        registerActivityLifecycleCallbacks(ActivityLifecycle)
        checkAppCredentials()
        checkChatSettings()
        initCredentials()
        ProcessLifecycleOwner.get().lifecycle.addObserver(BackgroundListener())
    }

    private fun checkAppCredentials() {
        if (APPLICATION_ID.isEmpty() || AUTH_KEY.isEmpty() || AUTH_SECRET.isEmpty() || ACCOUNT_KEY.isEmpty()) {
            throw AssertionError(getString(R.string.error_qb_credentials_empty))
        }
    }

    private fun checkChatSettings() {
        if (USER_DEFAULT_PASSWORD.isEmpty() || CHAT_PORT !in MIN_PORT_VALUE..MAX_PORT_VALUE
            || SOCKET_TIMEOUT !in MIN_SOCKET_TIMEOUT..MAX_SOCKET_TIMEOUT
        ) {
            throw AssertionError(getString(R.string.error_chat_credentails_empty))
        }
    }

    private fun initCredentials() {
        QBSettings.getInstance().init(applicationContext, APPLICATION_ID, AUTH_KEY, AUTH_SECRET)
        QBSettings.getInstance().accountKey = ACCOUNT_KEY

        // uncomment and put your Api and Chat servers endpoints if you want to point the sample
        // against your own server.
        //
        // QBSettings.getInstance().setEndpoints("https://your_api_endpoint.com", "your_chat_endpoint", ServiceZone.PRODUCTION);
        // QBSettings.getInstance().zone = ServiceZone.PRODUCTION
    }
}