package com.mohi.parkingappma.di

import com.mohi.parkingappma.MainActivity
import com.mohi.parkingappma.model.repo.BaseEntitiesRepository
import com.mohi.parkingappma.model.repo.EntitiesRepository
import com.mohi.parkingappma.model.repo.localrepo.EntityDao
import com.mohi.parkingappma.model.repo.localrepo.LocalDatabase
import com.mohi.parkingappma.model.repo.localrepo.LocalEntitiesRepository
import com.mohi.parkingappma.model.service.EntitiesService
import com.mohi.parkingappma.model.usecase.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.6:2021/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesMAService(retrofit: Retrofit): EntitiesService {
        return retrofit.create(EntitiesService::class.java)
    }

    @Provides
    @Singleton
    fun providesEntityDao(): EntityDao {
        return LocalDatabase.getDatabase(MainActivity.instance.applicationContext).entityDao()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface AppModuleInt {

        @Binds
        @Singleton
        fun provideMARepository(repo: BaseEntitiesRepository): EntitiesRepository

        @Binds
        @Singleton
        fun provideGetAllUseCase(uc: BaseGetEntitiesUseCase): GetEntitiesUseCase

        @Binds
        @Singleton
        fun provideAddUseCase(uc: BaseAddEntityUseCase): AddEntityUseCase

        @Binds
        @Singleton
        fun provideDeleteUseCase(uc: BaseDeleteEntityUseCase): DeleteEntityUseCase

        @Binds
        @Singleton
        fun provideGetFreeUseCase(uc: BaseGetFreeUseCase): GetFreeUseCase

        @Binds
        @Singleton
        fun provideTakeUseCase(uc: BaseTakeUseCase): TakeUseCase

        @Binds
        @Singleton
        fun provideSyncUseCase(uc: BaseSyncUseCase): SyncUseCase
    }

}