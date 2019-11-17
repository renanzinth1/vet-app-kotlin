package br.com.renanjardel.vet_app_kotlin.service

import br.com.renanjardel.vet_app_kotlin.model.Especie
import br.com.renanjardel.vet_app_kotlin.model.SubEspecie
import retrofit2.Call
import retrofit2.http.*

interface EspecieService {

    @POST("/especies")
    fun salvar(@Body cliente: Especie?): Call<Void>

    @GET("/especies")
    fun listar(): Call<List<Especie>>

    @PUT("/especies/{codigo}")
    fun editar(@Path("codigo") codigo: Long, @Body especie: Especie?): Call<Especie>

    @GET("/especies/{codigo}")
    fun buscarPorId(@Path("codigo") codigo: Long): Call<Especie>

    @DELETE("/especies/{codigo}")
    fun remover(@Path("codigo") codigo: Long): Call<Void>

    @GET("/especies/{codigo}/subEspecies")
    fun listarSubEspecies(@Path("codigo") codigo: Long?): Call<List<SubEspecie>>
}
