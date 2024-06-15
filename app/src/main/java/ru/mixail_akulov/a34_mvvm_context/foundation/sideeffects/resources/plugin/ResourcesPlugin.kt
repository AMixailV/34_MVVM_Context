package ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.resources.plugin

import android.content.Context
import ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.SideEffectMediator
import ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.SideEffectPlugin

/**
 * Плагин для доступа к ресурсам приложения из моделей представления.
 * Позволяет добавить интерфейс [Resources] в конструктор модели представления.
 */
class ResourcesPlugin : SideEffectPlugin<ResourcesSideEffectMediator, Nothing> {

    override val mediatorClass: Class<ResourcesSideEffectMediator>
        get() = ResourcesSideEffectMediator::class.java

    override fun createMediator(applicationContext: Context): SideEffectMediator<Nothing> {
        return ResourcesSideEffectMediator(applicationContext)
    }

}