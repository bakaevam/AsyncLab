package ru.anfilek.asyncLab

import android.util.Log
import kotlin.concurrent.thread

fun testSharedResources() {
//    val i : AtomicInteger = AtomicInteger(0)
    var i = 0

    val thread1 = thread {
        for (k in 0..1_000) {
            i++
            Log.d("TAG", "thread1: $k")
        }
    }

    thread {
        for (k in 0..1_000) {
            i++
            Log.d("TAG", "thread2: $k")
        }
    }.join()
    thread1.join()

    Log.d("TAG", "thread result: $i")
}