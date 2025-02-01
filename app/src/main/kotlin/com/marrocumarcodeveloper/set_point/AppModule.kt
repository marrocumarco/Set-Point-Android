package com.marrocumarcodeveloper.set_point

import android.content.Context
import com.marrocumarcodeveloper.set_point.business_logic.Match
import com.marrocumarcodeveloper.set_point.business_logic.MatchImpl
import com.marrocumarcodeveloper.set_point.business_logic.Settings
import com.marrocumarcodeveloper.set_point.business_logic.SettingsImpl
import com.marrocumarcodeveloper.set_point.persistency.Preferences
import com.marrocumarcodeveloper.set_point.use_case.DataAccess
import com.marrocumarcodeveloper.set_point.use_case.MatchUseCase
import com.marrocumarcodeveloper.set_point.use_case.SettingsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    fun provideDataAccess(@ApplicationContext context: Context): DataAccess {
        val sharedPreferences = context.getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE)
        return Preferences(sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideSettings(): Settings {
        return SettingsImpl()
    }

    @Provides
    fun provideMatchRepository(settings: Settings): Match {
        return MatchImpl(settings)
    }

    @Provides
    fun provideMatchUseCase(repository: Match): MatchUseCase {
        return MatchUseCase(repository)
    }

    @Provides
    fun provideSettingsUseCase(repository: Settings, preferences: DataAccess): SettingsUseCase {
        return SettingsUseCase(repository, preferences)
    }
}