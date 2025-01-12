package com.marrocumarcodeveloper.set_point

import com.marrocumarcodeveloper.set_point.business_logic.Match
import com.marrocumarcodeveloper.set_point.business_logic.MatchImpl
import com.marrocumarcodeveloper.set_point.business_logic.Settings
import com.marrocumarcodeveloper.set_point.use_case.MatchUseCase
import com.marrocumarcodeveloper.set_point.use_case.SettingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideMatchRepository(settings: Settings): Match {
        return MatchImpl(settings)
    }

    @Provides
    fun provideMatchUseCase(repository: Match): MatchUseCase {
        return MatchUseCase(repository)
    }

    @Provides
    fun provideSettingsUseCase(repository: Settings): SettingsUseCase {
        return SettingsUseCase(repository)
    }
}