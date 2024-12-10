package com.marrocumarcodeveloper.set_point

import android.content.Context
import android.content.SharedPreferences
import com.marrocumarcodeveloper.set_point.business_logic.Settings
import com.marrocumarcodeveloper.set_point.business_logic.SettingsImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Singleton
    @Provides
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("APP_SETTINGS", Context.MODE_PRIVATE)

    @Provides
    fun provideSettingsRepository(sharedPreferences: SharedPreferences): Settings {
        return SettingsImpl(sharedPreferences)
    }
}