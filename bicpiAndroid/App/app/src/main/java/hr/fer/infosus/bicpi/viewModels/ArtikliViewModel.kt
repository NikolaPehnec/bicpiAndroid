package hr.fer.infosus.bicpi.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.fer.infosus.bicpi.NetworkChecker
import hr.fer.infosus.bicpi.model.KupovniArtikl
import hr.fer.infosus.bicpi.model.RentabilniArtikl
import hr.fer.infosus.bicpi.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ArtikliViewModel(
    val context: Context
) : ViewModel() {

    private val kupovniArtiklLiveData: MutableLiveData<List<KupovniArtikl>> by lazy {
        MutableLiveData<List<KupovniArtikl>>()
    }

    private val rentabilniArtiklLiveData: MutableLiveData<List<RentabilniArtikl>> by lazy {
        MutableLiveData<List<RentabilniArtikl>>()
    }

    private val errorMessageLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getErrorMessageLiveData(): LiveData<String> {
        val err: LiveData<String> = errorMessageLiveData
        errorMessageLiveData.value = null
        return err
    }

    fun getKupovniArtikliLiveData(): LiveData<List<KupovniArtikl>> {
        return kupovniArtiklLiveData
    }

    fun getRentabilniArtikliLiveData(): LiveData<List<RentabilniArtikl>> {
        return rentabilniArtiklLiveData
    }

    private val postKupovniResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getPostKupovniResultLiveData(): LiveData<Boolean> {
        return postKupovniResultLiveData
    }

    private val postRentabilniResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getPostRentabilniResultLiveData(): LiveData<Boolean> {
        return postRentabilniResultLiveData
    }

    private val deleteArtiklResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getDeleteArtiklResultLiveData(): LiveData<Boolean> {
        return deleteArtiklResultLiveData
    }


    fun getKupovniArtikli() {
        val networkChecker = NetworkChecker(context)
        if (networkChecker.isOnline()) {

            ApiModule.retrofit.getKupovniArtikli()
                .enqueue(object : Callback<List<KupovniArtikl>> {
                    override fun onResponse(
                        call: Call<List<KupovniArtikl>>,
                        response: Response<List<KupovniArtikl>>
                    ) {
                        kupovniArtiklLiveData.value = response.body()?.map {
                            KupovniArtikl(
                                it.sifraArtikl,
                                it.artikl.idGrupa,
                                it.artikl.naziv,
                                it.cijena,
                                it.artikl.opis,
                                it.kolicina,
                            )
                        }
                    }

                    override fun onFailure(call: Call<List<KupovniArtikl>>, t: Throwable) {
                        errorMessageLiveData.value = t.message
                    }
                })
        } else {
            errorMessageLiveData.value = "No internet connection"
        }
    }

    fun getRentabilniArtikli() {
        ApiModule.retrofit.getRentabilniArtikli()
            .enqueue(object : Callback<List<RentabilniArtikl>> {
                override fun onResponse(
                    call: Call<List<RentabilniArtikl>>,
                    response: Response<List<RentabilniArtikl>>
                ) {
                    rentabilniArtiklLiveData.value = response.body()?.map {
                        RentabilniArtikl(
                            it.idGrupa,
                            it.naziv,
                            it.cijena,
                            it.opis,
                            it.iznajmljeno
                        )
                    }
                }

                override fun onFailure(call: Call<List<RentabilniArtikl>>, t: Throwable) {
                    errorMessageLiveData.value = t.message
                }
            })
    }

    fun postKupovniArtikl(idGrupe: Int, naziv: String, cijena: Float, opis: String, kolicina: Int) {
        ApiModule.retrofit.postKupovniArtikl(
            KupovniArtikl(
                null,
                idGrupe,
                naziv,
                cijena,
                opis,
                kolicina
            )
        )
            .enqueue(
                object : Callback<KupovniArtikl> {
                    override fun onResponse(
                        call: Call<KupovniArtikl>,
                        response: Response<KupovniArtikl>
                    ) {
                        postKupovniResultLiveData.value = response.isSuccessful
                    }

                    override fun onFailure(call: Call<KupovniArtikl>, t: Throwable) {
                        postKupovniResultLiveData.value = false
                    }
                })
    }

    fun postRentabilniArtikl(idGrupe: Int, naziv: String, cijena: Float, opis: String) {
        ApiModule.retrofit.postRentabilniArtikl(
            RentabilniArtikl(
                idGrupe,
                naziv,
                cijena,
                opis,
                false
            )
        )
            .enqueue(
                object : Callback<RentabilniArtikl> {
                    override fun onResponse(
                        call: Call<RentabilniArtikl>,
                        response: Response<RentabilniArtikl>
                    ) {
                        postRentabilniResultLiveData.value = response.isSuccessful
                    }

                    override fun onFailure(call: Call<RentabilniArtikl>, t: Throwable) {
                        postRentabilniResultLiveData.value = false
                    }
                })
    }

    fun deleteArtikl(idArtikl: Int) {
        ApiModule.retrofit.deleteArtikl(idArtikl)
            .enqueue(
                object : Callback<String> {
                    override fun onResponse(
                        call: Call<String>,
                        response: Response<String>
                    ) {
                        deleteArtiklResultLiveData.value = response.isSuccessful
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        deleteArtiklResultLiveData.value = false
                    }
                })
    }


}