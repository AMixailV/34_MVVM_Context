package ru.mixail_akulov.a34_mvvm_context.foundation.views

import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.mixail_akulov.a34_mvvm_context.foundation.model.ErrorResult
import ru.mixail_akulov.a34_mvvm_context.foundation.utils.Event
import ru.mixail_akulov.a34_mvvm_context.foundation.model.Result
import ru.mixail_akulov.a34_mvvm_context.foundation.model.SuccessResult

// Альтернативные записи для сокращени кода
typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

typealias LiveResult<T> = LiveData<Result<T>>
typealias MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<Result<T>>

typealias ResultFlow<T> = Flow<Result<T>>
typealias ResultMutableStateFlow<T> = MutableStateFlow<Result<T>>


/**
 * Base class for all view-models.
 */

open class BaseViewModel() : ViewModel() {

    private val coroutineContext = SupervisorJob() + Dispatchers.Main.immediate + CoroutineExceptionHandler { _, throwable ->
        // вы можете добавить сюда обработку исключений
    }

    // настраиваемая область, которая сразу же отменяет задания при нажатии кнопки «Назад»
    protected val viewModelScope = CoroutineScope(coroutineContext)

    override fun onCleared() {
        super.onCleared()
        clearViewModelScope()
    }

    /**
     * Переопределите этот метод в дочерних классах, если вы хотите прослушивать результаты с других экранов.
     */
    open fun onResult(result: Any) {

    }

    /**
     * Переопределите этот метод в дочерних классах, если вы хотите контролировать поведение возврата.
     * Верните `true`, если вы хотите прервать закрытие этого экрана
     */
    open fun onBackPressed(): Boolean {
        clearViewModelScope()
        return false
    }

    /**
    * Запустить указанный приостанавливающий [block] и использовать его результат
     * в качестве значения предоставленного [liveResult].
    */
    fun <T> into(liveResult: MutableLiveResult<T>, block: suspend () -> T) {
        viewModelScope.launch {
            try {
                liveResult.postValue(SuccessResult(block()))
            } catch (e: Exception) {
                if (e !is CancellationException) liveResult.postValue(ErrorResult(e))
            }
        }
    }

    /**
     * Запустить указанный приостанавливающий [block] и использовать его результат
     * в качестве значения предоставленного [stateFlow].
     */
    fun <T> into(stateFlow: MutableStateFlow<Result<T>>, block: suspend () -> T) {
        viewModelScope.launch {
            try {
                stateFlow.value = SuccessResult(block())
            } catch (e: Exception) {
                if (e !is CancellationException) stateFlow.value = ErrorResult(e)
            }
        }
    }

    /**
     * Создайте [MutableStateFlow], который отражает состояние значения с указанным ключом,
     * управляемым [SavedStateHandle]. Когда значение обновляется,
     * экземпляр [MutableStateFlow] создает новый элемент с обновленным значением.
     * Когда какое-то новое значение присваивается [MutableStateFlow]
     * через [MutableStateFlow.value], оно записывается в [SavedStateHandle].
     * Так что на самом деле этот метод создает [MutableStateFlow],
     * который работает так же, как [MutableLiveData], возвращаемый [SavedStateHandle.getLiveData]..
     */
    fun <T> SavedStateHandle.getsStateFlow(key: String, initialValue: T): MutableStateFlow<T> {
        val savedStateHandle = this
        val mutableFlow = MutableStateFlow(savedStateHandle[key] ?: initialValue)

        viewModelScope.launch {
            mutableFlow.collect {
                savedStateHandle[key] = it
            }
        }

        viewModelScope.launch {
            savedStateHandle.getLiveData<T>(key).asFlow().collect {
                mutableFlow.value = it
            }
        }

        return mutableFlow
    }

    private fun clearViewModelScope() {
        viewModelScope.cancel()
    }

}

