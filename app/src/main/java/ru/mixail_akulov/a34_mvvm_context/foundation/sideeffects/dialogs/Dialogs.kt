package ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.dialogs

import ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.dialogs.plugin.DialogConfig

/**
Интерфейс побочных эффектов для управления диалогами из модели представления.
Перед использованием этой функции вам необходимо добавить [DialogsPlugin] в свою активность.
ПРЕДУПРЕЖДАТЬ! Обратите внимание, диалоги не сохраняются после закрытия приложения.
 */
interface Dialogs {

    /**
     * Показать диалоговое окно предупреждения пользователю и дождаться выбора пользователя.
     */
    suspend fun show(dialogConfig: DialogConfig): Boolean

}