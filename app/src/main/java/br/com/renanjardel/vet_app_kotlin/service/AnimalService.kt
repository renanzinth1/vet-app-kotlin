package br.com.renanjardel.vet_app_kotlin.service

import br.com.renanjardel.vet_app_kotlin.model.Animal
import retrofit2.Call
import retrofit2.http.*

interface AnimalService {

    @POST("/animais")
    fun salvar(@Body animal: Animal?): Call<Void>

    @GET("/animais")
    fun listar(): Call<List<Animal>>

    @PUT("/animais/{codigo}")
    fun editar(@Path("codigo") codigo: Long, @Body animal: Animal?): Call<Animal>

    @GET("/animais/{codigo}")
    fun buscarPorId(@Path("codigo") codigo: Long): Call<Animal>

    @DELETE("/animais/{codigo}")
    fun remover(@Path("codigo") codigo: Long?): Call<Void>
}
