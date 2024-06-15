package ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.navigator.plugin

import ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.navigator.Navigator
import ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.SideEffectMediator
import ru.mixail_akulov.a34_mvvm_context.foundation.views.BaseScreen

class NavigatorSideEffectMediator : SideEffectMediator<Navigator>(), Navigator {

    override fun launch(screen: BaseScreen) = target {
        it.launch(screen)
    }

    override fun goBack(result: Any?) = target {
        it.goBack(result)
    }

}