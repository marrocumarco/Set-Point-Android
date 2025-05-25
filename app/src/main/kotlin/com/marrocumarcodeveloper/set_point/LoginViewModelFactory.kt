package com.marrocumarcodeveloper.set_point

import com.marrocumarcodeveloper.set_point.presentation.view_models.MainActivityViewModel
import com.marrocumarcodeveloper.set_point.presentation.view_models.SettingsViewModel
import com.marrocumarcodeveloper.set_point.use_case.MatchUseCase
import com.marrocumarcodeveloper.set_point.use_case.SettingsUseCase

fun interface Factory<T> {
    fun create(): T
}

internal class MainActivityViewModelFactory(private val useCase: MatchUseCase) : Factory<MainActivityViewModel> {
    override fun create(): MainActivityViewModel {
        return MainActivityViewModel(useCase)
    }
}

internal class SettingsViewModelFactory(private val useCase: SettingsUseCase) : Factory<SettingsViewModel> {
    override fun create(): SettingsViewModel {
        return SettingsViewModel(useCase)
    }
}