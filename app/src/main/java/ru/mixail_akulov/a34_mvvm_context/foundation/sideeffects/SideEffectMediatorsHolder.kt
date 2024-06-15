package ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects

import android.content.Context

/**
 * Контейнер для экземпляров [SideEffectMediator]. Этот держатель находится в [ActivityScopeViewModel].
 */
@Suppress("UNCHECKED_CAST")
class SideEffectMediatorsHolder {

    private val _mediators = mutableMapOf<Class<*>, SideEffectMediator<*>>()
    val mediators: List<SideEffectMediator<*>>
        get() = _mediators.values.toList()

    /**
     * Существует ли [SideEffectMediator] указанного класса или нет.
     */
    fun <T> contains(clazz: Class<T>): Boolean {
        return _mediators.contains(clazz)
    }

    /**
     * Создайте и сохраните [SideEffectMediator], используя указанный [SideEffectPlugin].
     */
    fun <Mediator, Implementation> putWithPlugin(
        applicationContext: Context,
        plugin: SideEffectPlugin<Mediator, Implementation>
    ) {
        _mediators[plugin.mediatorClass] = plugin.createMediator(applicationContext)
    }

    /**
     * Свяжите [SideEffectImplementation] с [SideEffectMediator].
     * Таким образом, посредник может доставлять все вызовы реализации.
     */
    fun <Mediator, Implementation> setTargetWithPlugin(
        plugin: SideEffectPlugin<Mediator, Implementation>,
        sideEffectImplementationsHolder: SideEffectImplementationsHolder,
    ) {
        val intermediateViewService = get(plugin.mediatorClass)
        val target = sideEffectImplementationsHolder.getWithPlugin(plugin)
        if (intermediateViewService is SideEffectMediator<*>) {
            (intermediateViewService as SideEffectMediator<Implementation>).setTarget(target)
        }
    }

    /**
     * Get the [SideEffectMediator] instance by its class.
     */
    fun <T> get(clazz: Class<T>): T {
        return _mediators[clazz] as T
    }

    /**
     * Untie all [SideEffectImplementation] instances from all [SideEffectMediator] instances.
     */
    fun removeTargets() {
        _mediators.values.forEach { it.setTarget(null) }
    }

    /**
     * Clean-up all mediators.
     */
    fun clear() {
        _mediators.values.forEach { it.clear() }
        _mediators.clear()
    }
}