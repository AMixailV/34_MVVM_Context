package ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.dialogs.plugin

import android.content.Context
import ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.SideEffectMediator
import ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.SideEffectPlugin

/**
 * Плагин для отображения диалогов из вью-моделей.
 * Позволяет добавлять [Dialogs] интерфейс для конструктора модели представления.
 */
class DialogsPlugin : SideEffectPlugin<DialogsSideEffectMediator, DialogsSideEffectImpl> {

    override val mediatorClass: Class<DialogsSideEffectMediator>
        get() = DialogsSideEffectMediator::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<DialogsSideEffectImpl> {
        return DialogsSideEffectMediator()
    }

    override fun createImplementation(mediator: DialogsSideEffectMediator): DialogsSideEffectImpl {
        return DialogsSideEffectImpl(mediator.retainedState)
    }
}