package net.nomia.settings.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.nomia.settings.config.SettingsDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun database(@ApplicationContext appContext: Context): SettingsDatabase {
        return Room.databaseBuilder(
            appContext,
            SettingsDatabase::class.java,
            "settings"
        ).build()
    }
}
