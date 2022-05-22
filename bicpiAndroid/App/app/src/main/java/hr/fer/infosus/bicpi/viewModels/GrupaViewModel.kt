package hr.fer.infosus.bicpi.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.fer.infosus.bicpi.NetworkChecker
import hr.fer.infosus.bicpi.model.Grupa
import hr.fer.infosus.bicpi.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GrupaViewModel(
    val context: Context
) : ViewModel() {

    private val grupaLiveData: MutableLiveData<List<Grupa>> by lazy {
        MutableLiveData<List<Grupa>>()
    }

    private val errorMessageLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getErrorMessageLiveData(): LiveData<String> {
        val err: LiveData<String> = errorMessageLiveData
        errorMessageLiveData.value = null
        return err
    }

    fun getGrupaLiveData(): LiveData<List<Grupa>> {
        return grupaLiveData
    }

    private val postGrupaResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getPostGrupaResultLiveData(): LiveData<Boolean> {
        return postGrupaResultLiveData
    }

    fun getGrupe() {
        val networkChecker = NetworkChecker(context)
        if (networkChecker.isOnline()) {

            ApiModule.retrofit.getGrupeArtikala()
                .enqueue(object : Callback<List<Grupa>> {
                    override fun onResponse(
                        call: Call<List<Grupa>>,
                        response: Response<List<Grupa>>
                    ) {
                        grupaLiveData.value = response.body()?.map {
                            Grupa(
                                it.idGrupa,
                                it.nazivGrupa
                            )
                        }
                    }

                    override fun onFailure(call: Call<List<Grupa>>, t: Throwable) {
                        errorMessageLiveData.value = t.message
                    }
                })
        } else {
            errorMessageLiveData.value = "No internet connection"
        }
    }


    fun postGrupa(nazivGrupe: String) {
        ApiModule.retrofit.postGrupa(Grupa(null, nazivGrupe)).enqueue(
            object : Callback<Grupa> {
                override fun onResponse(
                    call: Call<Grupa>,
                    response: Response<Grupa>
                ) {
                    postGrupaResultLiveData.value = response.isSuccessful
                }

                override fun onFailure(call: Call<Grupa>, t: Throwable) {
                    postGrupaResultLiveData.value = false
                }
            })
    }

}