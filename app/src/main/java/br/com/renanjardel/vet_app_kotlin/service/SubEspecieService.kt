package br.com.renanjardel.vet_app_kotlin.service

import br.com.renanjardel.vet_app_kotlin.model.SubEspecie
import retrofit2.Call
import retrofit2.http.*

interface SubEspecieService {

    @POST("/subEspecies")
    fun salvar(@Body subEspecie: SubEspecie): Call<Void>

    @GET("/subEspecies")
    fun listar(): Call<List<SubEspecie>>

    @PUT("/subEspecies/{codigo}")
    fun editar(@Path("codigo") codigo: Long, @Body subEspecie: SubEspecie): Call<SubEspecie>

    @GET("/subEspecies/{codigo}")
    fun buscarPorId(@Path("codigo") codigo: Long): Call<SubEspecie>

    @DELETE("/subEspecies/{codigo}")
    fun remover(@Path("codigo") codigo: Long?): Call<Void>
}
