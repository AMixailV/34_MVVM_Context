package ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.resources.plugin

import android.content.Context
import ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.SideEffectMediator
import ru.mixail_akulov.a34_mvvm_context.foundation.sideeffects.resources.Resources

class ResourcesSideEffectMediator(
    private val appContext: Context
) : SideEffectMediator<Nothing>(), Resources {

    override fun getString(resId: Int, vararg args: Any): String {
        return appContext.getString(resId, *args)
    }

}