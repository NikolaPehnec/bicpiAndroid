package hr.fer.infosus.bicpi.model

import com.google.gson.annotations.SerializedName

class Artikl(
    sifraArtikl: Int?,
    grupa: Int,
    naziv: String,
    opis: String,
) {
    @SerializedName("sifartikl")
    val sifraArtikl: Int? = sifraArtikl

    @SerializedName("idgrupa")
    val idGrupa: Int = grupa

    @SerializedName("naziv")
    val naziv: String = naziv

    @SerializedName("opis")
    val opis: String = opis
}