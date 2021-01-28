package ru.anfilek.asyncLab

class MyRunnable(private var myHandlerThread: MyHandlerThread): Runnable {
    override fun run() {
        myHandlerThread.post()

        try {
            Thread.sleep(1000)
        } catch (ex: InterruptedException) {
            ex.printStackTrace()
        }
    }
}