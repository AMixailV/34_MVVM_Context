package ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.resources

import androidx.annotation.StringRes

/**
 * Интерфейс для доступа к ресурсам из моделей представления.
 * Перед использованием этой функции вам необходимо добавить [ResourcesPlugin] в свою активность.
 */
interface Resources {

    fun getString(@StringRes resId: Int, vararg args: Any): String

}