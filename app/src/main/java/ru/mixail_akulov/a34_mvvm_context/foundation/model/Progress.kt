package ru.mixail_akulov.a34_mvvm_context.foundation.model

/**
 * Представляет состояние выполнения некоторой операции: должен ли отображаться ход выполнения или нет.
 */
sealed class Progress

/**
 * Прогресс не должен отображаться
 */
object EmptyProgress : Progress()

/**
 * Должен отображаться прогресс, а также может указываться процентное значение.
 */
data class PercentageProgress(
    val percentage: Int
) : Progress() {

    companion object {
        val START = PercentageProgress(percentage = 0)
    }
}

// --- extension methods

/**
 * @return идет ли операция или нет
 */
fun Progress.isInProgress() = this !is EmptyProgress

/**
 * @return процент работы, если это возможно; в противном случае [PercentageProgress.START].
 */
fun Progress.getPercentage() = (this as? PercentageProgress)?.percentage ?: PercentageProgress.START.percentage