package ru.mixail_akulov.a34_mvvm_context.foundation.views

import java.io.Serializable

/**
 * Базовый класс для определения аргументов экрана.
 * Обратите внимание, что все поля внутри экрана должны быть сериализуемыми.
 */
interface BaseScreen : Serializable {

    companion object {
        const val ARG_SCREEN = "ARG_SCREEN"
    }

}