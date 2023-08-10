package com.spoton.com.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spoton.com.data.mappers.CryptoDataMapper
import com.spoton.com.domain.usecase.GetCryptoListUseCase
import com.spoton.com.domain.models.CryptoModel
import com.spoton.com.domain.usecase.GetSearchedCryptoUseCase
import com.spoton.com.presentation.CryptoUIViewState
import com.spoton.com.remote.NetworkResults.Failure
import com.spoton.com.remote.NetworkResults.Loading
import com.spoton.com.remote.NetworkResults.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class CryptoViewModel @Inject constructor(
    private val getCryptoListUseCase: GetCryptoListUseCase,
    private val getSearchCryptoUseCase: GetSearchedCryptoUseCase
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private val _cryptoListState =
        MutableStateFlow<CryptoUIViewState>(CryptoUIViewState.CryptoLoading)
    val cryptoListState: StateFlow<CryptoUIViewState> = _cryptoListState

    /** _mainCryptoListState is to backup the CryptoList .
    For example if we search any crypto ,
    our current list will updated according to search result,
    but when we clear the search we have to show the main crypto list again .
    so we can fetch from "_mainCryptoListState"  bellow **/
    private val _mainCryptoListState =
        MutableStateFlow<List<CryptoModel>>(arrayListOf())

    fun getMainCryptoListState() = _mainCryptoListState


    fun getCryptoList() {
        viewModelScope.launch {
            getCryptoListUseCase.getCharacters().collect { response ->
                when (response) {

                    is Success -> {
                        _mainCryptoListState.value =  CryptoDataMapper.mapCharacter(response.data)
                        _cryptoListState.value = CryptoUIViewState.CryptoSuccessState(
                            CryptoDataMapper.mapCharacter(response.data)
                        )
                    }
                    is Failure -> {
                        _cryptoListState.value =
                            CryptoUIViewState.CryptoFailureState(response.errorMessage)
                    }
                    is Loading -> {
                        _cryptoListState.value = CryptoUIViewState.CryptoLoading
                    }
                }
            }
        }
    }

    fun searchCryptoName(query: String) {
        viewModelScope.launch {
            getSearchCryptoUseCase.getSearchedCrypto(query, _mainCryptoListState.value).collect{ response->
                when (response) {

                    is Success -> {
                        _cryptoListState.value = CryptoUIViewState.CryptoSuccessState(
                           response.data)
                    }
                    is Failure -> {
                        _cryptoListState.value =
                            CryptoUIViewState.CryptoFailureState(response.errorMessage)
                    }
                    is Loading -> {
                        _cryptoListState.value = CryptoUIViewState.CryptoLoading
                    }
                }
            }
        }

    }
}