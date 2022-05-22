package hr.fer.infosus.bicpi.model

import com.google.gson.annotations.SerializedName

class RentabilniArtikl(
    grupa: Int,
    naziv: String,
    cijena: Float,
    opis: String,
    iznajmljeno: Boolean
) {

    @SerializedName("sifartikl")
    val sifraArtikl: Int? = null

    /*
        @SerializedName("idgrupa")
        val idGrupa: Int = grupa

        @SerializedName("naziv")
        val naziv: String = naziv

        @SerializedName("opis")
        val opis: String = opis*/
    
    @SerializedName("artikl")
    val artikl: Artikl = Artikl(null, grupa, naziv, opis)

    @SerializedName("cijenadan")
    val cijena: Float = cijena

    @SerializedName("iznajmljeno")
    val iznajmljeno: Boolean = iznajmljeno

}
