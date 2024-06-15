package ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.permissions.plugin

enum class PermissionStatus {
    /**
     * Приложение может безопасно использовать функции, требующие разрешения
     */
    GRANTED,

    /**
     * Приложение не имеет разрешения
     */
    DENIED,

    /**
     * У приложения нет разрешения, и пользователь выбрал параметр «Больше не спрашивать» в системном диалоговом окне.
     */
    DENIED_FOREVER
}