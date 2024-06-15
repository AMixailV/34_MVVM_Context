package ru.mixail_akulov.a34_mvvm_context.simplemvvm

import ru.mixail_akulov.a34_mvvm_context.foundation.SingletonScopeDependencies
import ru.mixail_akulov.a34_mvvm_context.foundation.model.coroutines.IoDispatcher
import ru.mixail_akulov.a34_mvvm_context.foundation.model.coroutines.WorkerDispatcher
import ru.mixail_akulov.a34_mvvm_context.simplemvvm.model.colors.InMemoryColorsRepository

object Initializer {

    // Поместите здесь свои одноэлементные зависимости области видимости
    fun initDependencies() = SingletonScopeDependencies.init { applicationContext ->
        // этот блок кода выполняется только один раз при первом запросе

        // классы держателей используются, потому что у нас есть 2 диспетчера одного типа
        val ioDispatcher = IoDispatcher() // for IO operations
        val workerDispatcher = WorkerDispatcher() // for CPU-intensive operations

        return@init listOf(
            ioDispatcher,
            workerDispatcher,

            InMemoryColorsRepository(ioDispatcher)
        )
    }
}