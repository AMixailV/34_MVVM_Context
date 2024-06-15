package ru.mixail_akulov.a34_mvvm_context.foundation.utils

import androidx.lifecycle.LiveData

/**
 * Представляет собой «побочный эффект».
 * Используется в [LiveData] как оболочка для событий.
 */
class Event<T>(
    private val value: T
) {
    private var handled: Boolean = false

    fun getValue(): T? {
        if (handled) return null
        handled = true
        return value
    }

}