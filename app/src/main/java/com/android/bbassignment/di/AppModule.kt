package com.android.bbassignment.di

import android.content.Context
import androidx.room.Room
import com.android.bbassignment.core.data.AppDatabase
import com.android.bbassignment.core.data.TaskDao
import com.android.bbassignment.core.network.DummyNetworkService
import com.android.bbassignment.core.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext ctx: Context): AppDatabase =
        Room.databaseBuilder(ctx, AppDatabase::class.java, "taskdb").build()

    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao = db.taskDao()

    @Singleton
    @Provides
    fun provideDummyNetwork() = DummyNetworkService()

    @Singleton
    @Provides
    fun provideRepository(dao: TaskDao, net: DummyNetworkService) = TaskRepository(dao, net)
}
