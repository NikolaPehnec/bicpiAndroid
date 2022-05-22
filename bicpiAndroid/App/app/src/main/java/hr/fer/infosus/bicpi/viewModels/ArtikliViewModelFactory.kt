package hr.fer.infosus.bicpi.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class ArtikliViewModelFactory(
    val context: Context
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ArtikliViewModel::class.java)) {
            return ArtikliViewModel(context) as T
        }

        throw IllegalArgumentException("Radi samo sa ArtikliViewModel klasama")
    }

}
