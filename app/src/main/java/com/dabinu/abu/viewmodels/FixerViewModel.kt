package com.dabinu.abu.viewmodels


import android.util.Log
import androidx.lifecycle.*
import com.dabinu.abu.data.ApiService
import com.dabinu.abu.data.RoomDB
import com.dabinu.abu.models.*
import kotlinx.coroutines.launch
import java.lang.Exception

class FixerViewModel(
    private val api: ApiService,
    private val roomDB: RoomDB) : ViewModel() {

    private val convertLiveData = MutableLiveData<Resource<ConvertResponse>>()
    private val symbolsLiveData = MutableLiveData<Resource<SymbolsResponse>>()


    val getProfileFromRoom: LiveData<List<Account>> = roomDB.getAccount.asLiveData()


    fun convert(from: String, to: String, amount: Double) {

        viewModelScope.launch {
            convertLiveData.postValue(Resource.loading())
            try {
                val result = api.convert(from, to, amount)
                Log.d("PPPPPP", result.toString())
                when(result.success) {
                    true -> convertLiveData.postValue(Resource.success(result))
                    false -> convertLiveData.postValue(Resource.error("Failed, try again later"))
                }
            }
            catch (e: Exception) {
                Log.d("PPPPPP", e.message as String)
                convertLiveData.postValue(Resource.error("Failed, try again later"))
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
    fun getSymbolsLiveData() = symbolsLiveData




}
