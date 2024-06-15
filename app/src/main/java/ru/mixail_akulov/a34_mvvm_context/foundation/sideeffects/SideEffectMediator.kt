package ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects

import ru.mixail_akulov.a34_mvvm_context.foundation.utils.MainThreadExecutor
import ru.mixail_akulov.a34_mvvm_context.foundation.utils.ResourceActions
import java.util.concurrent.Executor

/**
 * Базовый класс для всех медиаторов побочных эффектов.
 * Эти посредники живут в [ActivityScopeViewModel].
 * Посредник должен делегировать всю логику, связанную с пользовательским интерфейсом, реализациям через поле [target].
 */
open class SideEffectMediator<Implementation>(
    executor: Executor = MainThreadExecutor()
) {

    protected val target = ResourceActions<Implementation>(executor)

    /**
     * Назначить/Отменить назначение целевой реализации для этого поставщика.
     */
    fun setTarget(target: Implementation?) {
        this.target.resource = target
    }

    fun clear() {
        target.clear()
    }
}