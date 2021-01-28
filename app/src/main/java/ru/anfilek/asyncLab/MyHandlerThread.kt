package ru.anfilek.asyncLab

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import java.util.*

class MyHandlerThread: android.os.HandlerThread(TAG), Handler.Callback {

    companion object {
        private const val TAG = "MyHandlerThread"
    }

    private var handler: Handler? = null
        private set

    override fun onLooperPrepared() {
        handler = Handler(looper, this)
    }

    fun post() {
        handler?.sendEmptyMessage(Random(10).nextInt())
    }

    override fun handleMessage(msg: Message): Boolean {
        Log.d(TAG, "handler message: ${msg.what}: Thread: ${Thread.currentThread().name}")
        return true
    }
}
