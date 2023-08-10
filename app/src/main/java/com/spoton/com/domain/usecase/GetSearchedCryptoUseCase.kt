package com.spoton.com.domain.usecase

import com.spoton.com.domain.models.CryptoModel
import com.spoton.com.remote.NetworkResults
import com.spoton.com.remote.NetworkResults.Loading
import com.spoton.com.remote.NetworkResults.Success
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetSearchedCryptoUseCase {
    suspend fun getSearchedCrypto(query: String, mainCryptoList: List<CryptoModel>) = flow {

        /** we are emitting "loading = true" because we have to show the loading in UI .
         * so before calling the API we are emitting "loading"  **/
        emit(Loading(true))

        /** We are taking "mainCryptoList" as input for searching the query */

        val searchList: ArrayList<CryptoModel> = arrayListOf()
        if (query.isNotEmpty() && query.length > 2) { // we validating if query is not empty and length should be greater that 2
            if (mainCryptoList.isNotEmpty()) {
                for (item in mainCryptoList) {
                    if (item.name?.lowercase()?.contains(query.lowercase()) == true) {
                        searchList.add(item)
                    }
                }
            }
            emit(Success(searchList))
        } else {
            emit(Success(mainCryptoList))

        }
    }.catch { e ->
        emit(NetworkResults.Failure(e.message ?: "Unknown Error"))
    }
}