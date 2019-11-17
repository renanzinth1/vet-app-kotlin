package br.com.renanjardel.vet_app_kotlin.service

import br.com.renanjardel.vet_app_kotlin.model.Veterinario
import retrofit2.Call
import retrofit2.http.*

interface VeterinarioService {

    @POST("/veterinarios")
    fun salvar(@Body veterinario: Veterinario): Call<Void>

    @GET("/veterinarios")
    fun listar(): Call<List<Veterinario>>

    @PUT("/veterinarios/{codigo}")
    fun editar(@Path("codigo") codigo: Long, @Body veterinario: Veterinario): Call<Veterinario>

    @GET("/veterinarios/{codigo}")
    fun buscarPorId(@Path("codigo") codigo: Long): Call<Veterinario>

    @DELETE("/veterinarios/{codigo}")
    fun remover(@Path("codigo") codigo: Long?): Call<Void>
}
