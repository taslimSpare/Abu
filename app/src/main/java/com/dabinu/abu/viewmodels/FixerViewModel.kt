package com.dabinu.abu.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.dabinu.abu.data.ApiService
import com.dabinu.abu.models.*
import kotlinx.coroutines.launch
import java.lang.Exception

class FixerViewModel(
    val api: ApiService) : ViewModel() {

    private val convertLiveData = MutableLiveData<Resource<ConvertResponse>>()
    private val latestRatesLiveData = MutableLiveData<Resource<LatestResponse>>()
    private val symbolsLiveData = MutableLiveData<Resource<SymbolsResponse>>()


    fun convert(from: String, to: String, amount: Double) {

        viewModelScope.launch {
            convertLiveData.postValue(Resource.loading())
            try {
                val result = api.convert(from, to, amount)
                when(result.success) {
                    true -> convertLiveData.postValue(Resource.success(result))
                    false -> convertLiveData.postValue(Resource.error("Failed, try again later"))
                }
            }
            catch (e: Exception) {
                convertLiveData.postValue(Resource.error("Failed, try again later"))
            }
        }
    }


    fun fetchLatest(base: String, symbols: String) {

        viewModelScope.launch {
            latestRatesLiveData.postValue(Resource.loading())
            try {
                val result = api.latest(base, symbols)
                when(result.success) {
                    true -> latestRatesLiveData.postValue(Resource.success(result))
                    false -> latestRatesLiveData.postValue(Resource.error("Failed, try again later"))
                }
            }
            catch (e: Exception) {
                latestRatesLiveData.postValue(Resource.error("Failed, try again later"))
            }
        }
    }


    fun fetchAllSymbols() {

        viewModelScope.launch {
            symbolsLiveData.postValue(Resource.loading())
            try {
                val result = api.symbols()
                when(result.success) {
                    true -> symbolsLiveData.postValue(Resource.success(result))
                    false -> symbolsLiveData.postValue(Resource.error("Failed, try again later"))
                }
            }
            catch (e: Exception) {
                symbolsLiveData.postValue(Resource.error(e.message))
            }
        }
    }


    fun getConvertLiveData() = convertLiveData
    fun getLatestRatesLiveData() = latestRatesLiveData
    fun getSymbolsLiveData() = symbolsLiveData




}
