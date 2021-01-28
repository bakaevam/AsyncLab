package ru.anfilek.asyncLab

import android.annotation.SuppressLint
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ru.anfilek.asyncLab.databinding.ActivityMainBinding
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var handlerThread: MyHandlerThread? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var myRunnable: MyRunnable
    private lateinit var th: Thread
    private lateinit var handlerGame: Handler

    companion object {
        const private val FIRST_WIN = 1
        const private val SECOND_WIN = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStart.setOnClickListener { startHandlerThread() }
        binding.btnStop.setOnClickListener { stopHandlerThread() }
        binding.btnAsync.setOnClickListener { startAsync() }
        binding.btnFreeze.setOnClickListener { freeze() }

        handlerGame = Handler()

        handlerGame = @SuppressLint("HandlerLeak")
        object : Handler() {
            @SuppressLint("HandlerLeak")
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    FIRST_WIN -> {
                        Log.v("WINNER", "FIRST")
                        binding.tv.text = "The First thread has won !"
                    }
                    else -> {
                        Log.v("WINNER", "SECOND")
                        binding.tv.text = "The Second thread has won !"
                    }
                }
            }
        }

        testSharedResources()
    }

    private fun startHandlerThread() {
        handlerThread = MyHandlerThread()
        handlerThread?.start()
        myRunnable = MyRunnable(handlerThread!!)
        th = Thread(myRunnable)
        th.start()
    }

    private fun stopHandlerThread() {
        // optional
    }

    private fun startAsync() {
        MyAsyncTask().execute()
    }

    private fun freeze() {
        Thread.sleep(9000)
    }

    // DO NOT DO THIS! NEVER!
    class MyAsyncTask : AsyncTask<String, Int, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
//            tv.text = result
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
        }

        override fun onCancelled(result: String?) {
            super.onCancelled(result)
        }

        override fun onCancelled() {
            super.onCancelled()
        }

        override fun doInBackground(vararg params: String?): String {
            Thread.sleep(3000)
            return "That's all"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        th.stop()
        handlerThread!!.quit()
    }

    fun testSharedResources() {
        var i: AtomicInteger = AtomicInteger(0)
        fun getRandomInt(): Int {
            return (1..6).random()
        }

        var thread1 = Thread()
        var thread2 = Thread()


        thread1 = thread {
            try {
                while (i.toInt() <= 100) {
                    i.getAndAdd(getRandomInt())
                    Log.d("TAG", "thread1: $i")

                    if (i.toInt() >= 100 && thread2.isAlive) {
                        thread2.interrupt()
                        (handlerGame as Handler).sendEmptyMessage(FIRST_WIN)
                        Log.d("TAG", "thread result: $i")
                        break
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        thread2 = thread {
            try {
                while (i.toInt() <= 100) {
                    i.getAndAdd(getRandomInt())
                    Log.d("TAG", "thread2: $i")

                    if (i.toInt() >= 100 && thread1.isAlive) {
                        thread1.interrupt()
                        (handlerGame as Handler).sendEmptyMessage(SECOND_WIN)
                        Log.d("TAG", "thread result: $i")
                        break
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        thread2.join()
        thread1.join()
    }
}


