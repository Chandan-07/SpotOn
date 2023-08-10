package com.spoton.com.domain.usecase

import com.spoton.com.domain.GetCryptoListRepository
import com.spoton.com.remote.NetworkResults
import com.spoton.com.remote.NetworkResults.Loading
import com.spoton.com.remote.NetworkResults.Success
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCryptoListUseCase  @Inject constructor (private val getCryptoListRepository: GetCryptoListRepository){
    suspend fun getCharacters() = flow {
        emit(Loading(true))
        val response = getCryptoListRepository.getCryptoList()
        emit(Success(response))
    }.catch { e ->
        emit(NetworkResults.Failure(e.message ?: "Unknown Error"))
    }
}