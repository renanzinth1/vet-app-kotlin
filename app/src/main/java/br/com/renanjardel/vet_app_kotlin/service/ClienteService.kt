package br.com.renanjardel.vet_app_kotlin.service

import br.com.renanjardel.vet_app_kotlin.model.Animal
import br.com.renanjardel.vet_app_kotlin.model.Cliente
import retrofit2.Call
import retrofit2.http.*

interface ClienteService {

    @POST("/clientes")
    fun salvar(@Body cliente: Cliente): Call<Void>

    @GET("/clientes")
    fun listar(): Call<List<Cliente>>

    @GET("/clientes/{codigo}/animais")
    fun listarAnimais(@Path("codigo") codigo: Long?): Call<List<Animal>>

    @PUT("/clientes/{codigo}")
    fun editar(@Path("codigo") codigo: Long, @Body cliente: Cliente): Call<Cliente>

    @GET("/clientes/{codigo}")
    fun buscarPorId(@Path("codigo") codigo: Long): Call<Cliente>

    @DELETE("/clientes/{codigo}")
    fun remover(@Path("codigo") codigo: Long?): Call<Void>
}
