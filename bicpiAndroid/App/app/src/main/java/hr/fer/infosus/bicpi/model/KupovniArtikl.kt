package hr.fer.infosus.bicpi.model

import com.google.gson.annotations.SerializedName

class KupovniArtikl(
    sifraArtikl: Int?,
    grupa: Int,
    naziv: String,
    cijena: Float,
    opis: String,
    kolicina: Int
) {

    @SerializedName("sifartikl")
    val sifraArtikl: Int? = sifraArtikl

    /* @SerializedName("idgrupa")
     val idGrupa: Int = grupa

     @SerializedName("naziv")
     val naziv: String = naziv

     @SerializedName("opis")
     val opis: String = opis*/

    @SerializedName("artikl")
    val artikl: Artikl = Artikl(null, grupa, naziv, opis)

    @SerializedName("cijena")
    val cijena: Float = cijena

    @SerializedName("kolicina")
    val kolicina: Int = kolicina


    var image: ByteArray? = null

    var count: Int = 0

    var wasModified: Boolean = false

    var hasWatermark: Boolean = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KupovniArtikl

        if (sifraArtikl != other.sifraArtikl) return false

        return true
    }

    override fun hashCode(): Int {
        return sifraArtikl?.hashCode() ?: 0
    }

}
