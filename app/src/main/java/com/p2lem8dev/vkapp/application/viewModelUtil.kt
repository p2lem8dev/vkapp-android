package com.p2lem8dev.vkapp.application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

class AppViewModelFactory(private val factory: () -> ViewModel) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return factory() as T
    }
}

inline fun <reified VM : ViewModel> ViewModelStoreOwner.viewModels(
    storeFactory: ViewModelStoreOwner? = null,
    noinline constructor: () -> VM,
): Lazy<VM> = ViewModelLazy(
    viewModelClass = VM::class,
    storeProducer = { (storeFactory ?: this).viewModelStore },
    factoryProducer = { AppViewModelFactory(constructor) },
)