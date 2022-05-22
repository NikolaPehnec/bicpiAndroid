package hr.fer.infosus.bicpi.model

import com.google.gson.annotations.SerializedName
import java.util.*

class Order (
    @SerializedName("iduser") val idUser: UUID,
    @SerializedName("price") val price: Double?,
    isSubscription : Int
){

    @SerializedName("idorder")
    val idOrder : UUID? = null

    @SerializedName("issubscription")
    val isSubscription : Int = isSubscription

    @SerializedName("subscriptioncancelled")
    val subscriptionCanceled : Int = 0

    @SerializedName("time")
    val time : Date? = null

    @SerializedName("userByIduser")
    val korisnik: Korisnik? = null

}