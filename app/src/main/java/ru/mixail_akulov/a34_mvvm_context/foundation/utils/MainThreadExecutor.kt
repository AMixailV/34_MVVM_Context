package ru.mixail_akulov.a34_mvvm_context.foundation.utils

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor

/**
 * MainThreadExecutor запускает экземпляры [Runnable]:
 * - если текущий поток является основным потоком -> [Runnable] выполняется немедленно
 * - если текущий поток не является основным потоком -> [Runnable] выполняется [Handler] в основном потоке
 */
class MainThreadExecutor : Executor {

    private val handler = Handler(Looper.getMainLooper())

    override fun execute(command: Runnable) {
        if (Looper.getMainLooper().thread.id == Thread.currentThread().id) {
            command.run()
        } else {
            handler.post(command)
        }
    }

}