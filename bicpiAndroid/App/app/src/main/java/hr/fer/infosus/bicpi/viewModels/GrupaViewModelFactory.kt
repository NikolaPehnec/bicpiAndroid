package hr.fer.infosus.bicpi.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class GrupaViewModelFactory(
    val context: Context
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GrupaViewModel::class.java)) {
            return GrupaViewModel(context) as T
        }

        throw IllegalArgumentException("Radi samo sa GrupaViewModel klasama")
    }

}
