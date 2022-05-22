package hr.fer.infosus.bicpi.networking

import hr.fer.infosus.bicpi.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface BicpiApiService {

    @POST("user/register")
    fun register(@Body user: RegisterRequest?): Call<Korisnik>

    @POST("user/login")
    fun login(@Body user: LoginRequest?): Call<Korisnik>

    @GET("objects/kupovniartikl")
    fun getKupovniArtikli(): Call<List<KupovniArtikl>>

    @GET("objects/rentabilniartikl")
    fun getRentabilniArtikli(): Call<List<RentabilniArtikl>>

    @GET("objects/grupa")
    fun getGrupeArtikala(): Call<List<Grupa>>

    @POST("objects/postgrupa")
    fun postGrupa(@Body grupa: Grupa): Call<Grupa>

    @POST("objects/postkupovniartikl")
    fun postKupovniArtikl(@Body artikl: KupovniArtikl): Call<KupovniArtikl>

    @POST("objects/postrentabilniartikl")
    fun postRentabilniArtikl(@Body artikl: RentabilniArtikl): Call<RentabilniArtikl>

    @POST("deleteArtikl")
    fun deleteArtikl(@Body artiklId: Int): Call<String>


    @POST("order")
    fun sendOrder(@Body order: Order): Call<String>

    @GET("order/{id}")
    fun getOrder(@Path("id") id: String?): Call<List<Order>>

    @GET("order")
    fun getOrders(): Call<List<Order>>


}