package ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.permissions

import ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.permissions.plugin.PermissionStatus

/**
 * Интерфейс побочных эффектов для управления разрешениями из модели представления.
 * Вам необходимо добавить [PermissionsPlugin] в свою активность, прежде чем использовать эту функцию.
 *
 * WARNING! Обратите внимание, что такое использование запросов разрешений
 * не позволяет обрабатывать ответы после закрытия приложения.
 */
interface Permissions {

    /**
     * Имеет ли приложение указанное разрешение или нет.
     */
    fun hasPermissions(permission: String): Boolean

    /**
     * Запросить указанное разрешение.
     * See [PermissionStatus]
     */
    suspend fun requestPermission(permission: String): PermissionStatus

}