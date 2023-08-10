package com.spoton.com.presentation.di

import com.spoton.com.data.GetCryptoListRepoImpl
import com.spoton.com.domain.GetCryptoListRepository
import com.spoton.com.domain.usecase.GetSearchedCryptoUseCase
import com.spoton.com.remote.ApiService
import com.spoton.com.remote.RetrofitBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModule {

    @Provides
    @Singleton
        fun provideCryptoAPIService(): ApiService {
        return RetrofitBuilder.apiService
    }

    @Provides
    @Singleton
    fun provideCryptoRePO(characterRemote: GetCryptoListRepoImpl): GetCryptoListRepository {
        return characterRemote
    }

    @Provides
    @Singleton
    fun provideCryptoUseCase(): GetSearchedCryptoUseCase {
        return GetSearchedCryptoUseCase()
    }
}
