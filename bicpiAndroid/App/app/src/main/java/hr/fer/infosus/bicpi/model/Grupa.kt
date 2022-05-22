package hr.fer.infosus.bicpi.model

import com.google.gson.annotations.SerializedName

data class Grupa(
    @SerializedName("idgrupa")
    val idGrupa: Int?,

    @SerializedName("naziv")
    val nazivGrupa: String
)
