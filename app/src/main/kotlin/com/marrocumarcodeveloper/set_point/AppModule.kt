package com.marrocumarcodeveloper.set_point

import android.content.Context
import com.marrocumarcodeveloper.set_point.business_logic.Match
import com.marrocumarcodeveloper.set_point.business_logic.MatchImpl
import com.marrocumarcodeveloper.set_point.business_logic.Settings
import com.marrocumarcodeveloper.set_point.business_logic.SettingsImpl
import com.marrocumarcodeveloper.set_point.persistency.LocalizationRepositoryImpl
import com.marrocumarcodeveloper.set_point.persistency.Preferences
import com.marrocumarcodeveloper.set_point.use_case.DataAccess
import com.marrocumarcodeveloper.set_point.use_case.LocalizationRepository
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

    @Singleton
    @Provides
    fun provideDataAccess(@ApplicationContext context: Context): DataAccess {
        val sharedPreferences = context.getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE)
        return Preferences(sharedPreferences)
    }

    @Singleton
    @Provides
    fun provideLocalizationRepository(@ApplicationContext context: Context): LocalizationRepository {
        return LocalizationRepositoryImpl(context)
    }

    @Singleton
    @Provides
    fun provideSettings(): Settings {
        return SettingsImpl()
    }

    @Singleton
    @Provides
    fun provideMatchRepository(settings: Settings): Match {
        return MatchImpl(settings)
    }

    @Singleton
    @Provides
    fun provideMatchUseCase(match: Match, localizationRepository: LocalizationRepository): MatchUseCase {
        return MatchUseCase(match, localizationRepository)
    }

    @Singleton
    @Provides
    fun provideSettingsUseCase(repository: Settings, preferences: DataAccess, localizationRepository: LocalizationRepository): SettingsUseCase {
        return SettingsUseCase(repository, preferences, localizationRepository)
    }
}