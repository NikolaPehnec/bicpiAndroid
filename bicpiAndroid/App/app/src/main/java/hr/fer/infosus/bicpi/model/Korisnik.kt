package hr.fer.infosus.bicpi.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("korisnickoime") val email: String,
    @SerializedName("lozinka") val lozinka: String
)

data class RegisterRequest(
    @SerializedName("lozinka") val lozinka: String,
    @SerializedName("korisnickoime") val email: String,
    @SerializedName("ime") val ime: String,
    @SerializedName("prezime") val prezime: String,
    @SerializedName("oib") val OIB: String
)

data class Korisnik(
    @SerializedName("idkorisnik")
    val userID: Int? = null,

    @SerializedName("ime")
    val ime: String?,

    @SerializedName("prezime")
    val prezime: String?,

    @SerializedName("korisnickoime")
    val korisnickoIme: String?,

    @SerializedName("lozinka")
    val lozinka: String?,

    @SerializedName("oib")
    val oib: String?,

    @SerializedName("uloga")
    val uloga: Int?,

    @SerializedName("adresa")
    val adresa: String?
)
