package hr.fer.infosus.bicpi.viewModels

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import hr.fer.infosus.bicpi.Constants
import hr.fer.infosus.bicpi.model.Korisnik
import hr.fer.infosus.bicpi.model.RegisterRequest
import hr.fer.infosus.bicpi.networking.ApiModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private var message: String? = ""

    private val registerResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getRegistrationResultLiveData(): LiveData<Boolean> {
        return registerResultLiveData
    }

    fun getMessage(): String? {
        return message
    }

    fun register(
        lozinka: String,
        ime: String,
        prezime: String,
        email: String,
        OIB: String,
        sharedPref: SharedPreferences
    ) {
        ApiModule.retrofit.register(RegisterRequest(lozinka, email, ime, prezime, OIB))
            .enqueue(object : Callback<Korisnik> {
                override fun onResponse(
                    call: Call<Korisnik>,
                    response: Response<Korisnik>
                ) {

                    with(sharedPref.edit()) {
                        var uloga = 0;
                        var id = 0;
                        try {
                            uloga = response.body()?.uloga!!
                            id = response.body()?.userID!!
                        } catch (e: Exception) {
                        }
                        this.putInt(Constants.ROLE_ID, uloga)
                        this.putInt(Constants.USER_ID, id)
                        this.apply()
                    }

                    if (!response.isSuccessful) {
                        message = response.errorBody()?.string()?.substringAfter("errors\":[\"")
                            ?.substringBeforeLast("\"]}")
                    }

                    registerResultLiveData.value = response.isSuccessful
                }

                override fun onFailure(call: Call<Korisnik>, t: Throwable) {
                    registerResultLiveData.value = false
                }
            })
    }


}