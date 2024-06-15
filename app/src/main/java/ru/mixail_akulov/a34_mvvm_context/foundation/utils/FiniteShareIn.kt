package ru.mixail_akulov.a34_mvvm_context.foundation.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

/**
 * Преобразуйте этот поток в горячий общий поток (например, с помощью оператора [shareIn]),
 * но который является конечным и который также распространяет исключения из исходного потока.
 */
fun <T> Flow<T>.finiteShareIn(coroutineScope: CoroutineScope): Flow<T> {
    return this
        .map<T, Element<T>> { item -> ItemElement(item) }
        .onCompletion {
            emit(CompletedElement())
        }
        .catch { exception ->
            emit(ErrorElement(exception))
        }
        .shareIn(coroutineScope, SharingStarted.Eagerly, 1)
        .map {
            if (it is ErrorElement) throw it.error
            return@map it
        }
        .takeWhile { it is ItemElement }
        .map { (it as ItemElement).item }
}

// --- вспомогательные классы для материализации исходных потоков

private sealed class Element<T>

private class ItemElement<T>(
    val item: T
) : Element<T>()

private class ErrorElement<T>(
    val error: Throwable
) : Element<T>()

private class CompletedElement<T> : Element<T>()