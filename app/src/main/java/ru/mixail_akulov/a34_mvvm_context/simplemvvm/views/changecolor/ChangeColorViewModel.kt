package ru.mixail_akulov.a34_mvvm_context.simplemvvm.views.changecolor

import androidx.lifecycle.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.mixail_akulov.a34_mvvm_context.R
import ru.mixail_akulov.a34_mvvm_context.foundation.model.*
import ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.navigator.Navigator
import ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.resources.Resources
import ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.toasts.Toasts
import ru.mixail_akulov.a34_mvvm_context.foundation.utils.finiteShareIn
import ru.mixail_akulov.a34_mvvm_context.foundation.views.BaseViewModel
import ru.mixail_akulov.a34_mvvm_context.foundation.views.ResultFlow
import ru.mixail_akulov.a34_mvvm_context.foundation.views.ResultMutableStateFlow
import ru.mixail_akulov.a34_mvvm_context.simplemvvm.model.colors.ColorsRepository
import ru.mixail_akulov.a34_mvvm_context.simplemvvm.model.colors.NamedColor
import ru.mixail_akulov.a34_mvvm_context.simplemvvm.views.changecolor.ChangeColorFragment.Screen

class ChangeColorViewModel(
    screen: Screen,
    private val navigator: Navigator,
    private val toasts: Toasts,
    private val resources: Resources,
    private val colorsRepository: ColorsRepository,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel(), ColorsAdapter.Listener {

    // input sources
    private val _availableColors: ResultMutableStateFlow<List<NamedColor>> = MutableStateFlow(PendingResult())
    private val _currentColorId = savedStateHandle.getsStateFlow  ("currentColorId", screen.currentColorId)
    private val _instantSaveInProgress = MutableStateFlow<Progress>(EmptyProgress)
    private val _sampledSaveInProgress = MutableStateFlow<Progress>(EmptyProgress)


    // основной пункт назначения (содержит объединенные значения из _availableColors, _currentColorId и _saveInProgress)
    val viewState: ResultFlow<ViewState> = combine(
        _availableColors,
        _currentColorId,
        _instantSaveInProgress,
        _sampledSaveInProgress,
        ::mergeSources
    )

    // example of converting Flow into LiveData
    // - входящий поток Flow<Result<ViewState>>
    // - Flow<Result<ViewState>> сопоставляется с Flow<String> by using .map() operator
    // - then Flow<String> is converted to LiveData<String> by using .asLiveData()
    val screenTitle: LiveData<String> = viewState
        .map { result ->
            return@map if (result is SuccessResult) {
                val currentColor = result.data.colorsList.first { it.selected }
                resources.getString(R.string.change_color_screen_title, currentColor.namedColor.name)
            } else {
                resources.getString(R.string.change_color_screen_title_simple)
            }
        }.asLiveData()


    init {
        load()
    }

    override fun onColorChosen(namedColor: NamedColor) {
        if (_instantSaveInProgress.value.isInProgress()) return
        _currentColorId.value = namedColor.id
    }

    fun onSavePressed() = viewModelScope.launch {
        try {
            _instantSaveInProgress.value = PercentageProgress.START
            _sampledSaveInProgress.value = PercentageProgress.START

            val currentColorId = _currentColorId.value ?: throw IllegalStateException("Color ID should not be NULL")
            val currentColor = colorsRepository.getById(currentColorId)

            val flow = colorsRepository.setCurrentColor(currentColor).finiteShareIn(this)

            val instantJob = async {
                flow.collect { percentage ->
                    _instantSaveInProgress.value = PercentageProgress(percentage)
                }
            }

            val sampledJob = async {
                flow.sample(200) // emit the most actual progress every 200 ms.
                    .collect { percentage ->
                        _sampledSaveInProgress.value = PercentageProgress(percentage)
                    }
            }

            instantJob.await()
            sampledJob.await()

            navigator.goBack(currentColor)
        } catch (e: Exception) {
            if (e is CancellationException) toasts.toast(resources.getString(R.string.error_happened))
        } finally {
            _instantSaveInProgress.value = EmptyProgress
            _sampledSaveInProgress.value = EmptyProgress
        }
    }


    fun onCancelPressed() {
        navigator.goBack()
    }

    fun tryAgain() {
        load()
    }
    /**
    * Чистый метод преобразования для объединения данных из нескольких входных потоков:
     * - результат выборки списка цветов (Result<List<NamedColor>>)
     * - текущий выбранный цвет в RecyclerView (Long)
     * - флаг, выполняется ли операция сохранения или нет (Boolean)
     * Все приведенные выше значения объединены в один экземпляр [ViewState]:
    * ```
    * Flow<Result<List<NamedColor>>> ---+
    * Flow<Long> -----------------------|--> Flow<Result<ViewState>>
    * Flow<Boolean> --------------------+
    * ```
    */
    private fun mergeSources(colors: Result<List<NamedColor>>, currentColorId: Long,
                             _instantSaveInProgress: Progress, _sampledSaveInProgress: Progress): Result<ViewState> {

        // map Result<List<NamedColor>> to Result<ViewState>
        return colors.map { colorsList ->
            ViewState(
                // map List<NamedColor> to List<NamedColorListItem>
                colorsList = colorsList.map { NamedColorListItem(it, currentColorId == it.id) },
                showSaveButton = !_instantSaveInProgress.isInProgress(),
                showCancelButton = !_instantSaveInProgress.isInProgress(),
                showSaveProgressBar = _instantSaveInProgress.isInProgress(),

                saveProgressPercentage = _instantSaveInProgress.getPercentage(),
                saveProgressPercentageMessage = resources.getString(R.string.percentage_value, _sampledSaveInProgress.getPercentage())
            )
        }
    }

    private fun load() = into(_availableColors) {
        colorsRepository.getAvailableColors()
    }

    data class ViewState(
        val colorsList: List<NamedColorListItem>,
        val showSaveButton: Boolean,
        val showCancelButton: Boolean,
        val showSaveProgressBar: Boolean,

        val saveProgressPercentage: Int,
        val saveProgressPercentageMessage: String
    )
}