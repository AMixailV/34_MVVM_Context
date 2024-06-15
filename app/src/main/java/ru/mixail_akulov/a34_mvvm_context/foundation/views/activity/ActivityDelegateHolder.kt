package ru.mixail_akulov.a34_mvvm_context.foundation.views.activity

/**
 * Если вы по какой-то причине не хотите использовать [BaseActivity]
 * (например, у вас есть 2 или более иерархий действий, вы можете вместо этого использовать этот держатель.
 * Обратите внимание, что вам нужно вызывать методы [delegate] вручную из вашей activity
 * в этом Подробнее см. в разделе [ActivityDelegate].
 */
interface ActivityDelegateHolder {

    val delegate: ActivityDelegate

}