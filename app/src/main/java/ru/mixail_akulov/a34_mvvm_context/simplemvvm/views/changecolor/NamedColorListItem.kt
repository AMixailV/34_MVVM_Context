package ru.mixail_akulov.a34_mvvm_context.simplemvvm.views.changecolor

import ru.mixail_akulov.a34_mvvm_context.simplemvvm.model.colors.NamedColor

/**
 * Представляет элемент списка для цвета; его можно выбрать или нет
 */
data class NamedColorListItem(
    val namedColor: NamedColor,
    val selected: Boolean
)