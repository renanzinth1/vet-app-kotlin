package br.com.renanjardel.vet_app_kotlin.service

import br.com.renanjardel.vet_app_kotlin.model.Medicamento
import retrofit2.Call
import retrofit2.http.*

interface MedicamentoService {

    @POST("/medicamentos")
    fun salvar(@Body medicamento: Medicamento): Call<Void>

    @GET("/medicamentos")
    fun listar(): Call<List<Medicamento>>

    @PUT("/medicamentos/{codigo}")
    fun editar(@Path("codigo") codigo: Long, @Body medicamento: Medicamento): Call<Medicamento>

    @GET("/medicamentos/{codigo}")
    fun buscarPorId(@Path("codigo") codigo: Long): Call<Medicamento>

    @DELETE("/medicamentos/{codigo}")
    fun remover(@Path("codigo") codigo: Long): Call<Void>

    //    @GET("/especies/{codigo}/subEspecies")
    //    Call<List<SubEspecie>> listarSubEspecies(@Path("codigo") Long codigo);
}
