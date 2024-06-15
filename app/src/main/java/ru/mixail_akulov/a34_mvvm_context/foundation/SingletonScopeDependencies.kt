package ru.mixail_akulov.a34_mvvm_context.foundation

import android.content.Context
import androidx.annotation.MainThread

typealias SingletonsFactory = (applicationContext: Context) -> List<Any>

/**
 * Простой DI-контейнер для одноэлементных зависимостей.
 * Он содержит список синглетонов, которые могут быть введены в модели представления с помощью [ViewModelFactory].
 * Список зависимостей создается [SingletonFactory], предоставляемым через вызов [init].
 * Фабричный блок выполняется только один раз при первом запросе синглтона.
 * Вам нужно вызвать метод [init] во всех точках входа вашего приложения и предоставить блок инициализации,
 * который должен возвращать список синглетонов.
 */
object SingletonScopeDependencies {

    private var factory: SingletonsFactory? = null
    private var dependencies: List<Any>? = null

    /**
     * Установите блок фабрики инициализации, который создает и возвращает список всех синглетонов.
     */
    @MainThread
    fun init(factory: SingletonsFactory) {
        if (this.factory != null) return
        this.factory = factory
    }

    /**
     * Получите список всех синглетонов, предоставленных фабрикой, указанной вызовом [init].
     * Метод [init] должен быть вызван раньше. @throws [IllegalStateException],
     * если фабрика не была назначена через [init] ранее.
     */
    @MainThread
    fun getSingletonScopeDependencies(applicationContext: Context): List<Any> {
        val factory = this.factory ?: throw IllegalStateException("Call init() before getting singleton dependencies")
        return dependencies ?: factory(applicationContext).also { this.dependencies = it }
    }

}