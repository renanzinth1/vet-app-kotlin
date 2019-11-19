package br.com.renanjardel.vet_app_kotlin.retrofit

import br.com.renanjardel.vet_app_kotlin.service.*
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class RetrofitInicializador {

    private val retrofit: Retrofit

    val clienteService: ClienteService get() = retrofit.create(ClienteService::class.java)

    val veterinarioService: VeterinarioService get() = retrofit.create(VeterinarioService::class.java)

    val especieService: EspecieService get() = retrofit.create(EspecieService::class.java)

    val medicamentoService: MedicamentoService get() = retrofit.create(MedicamentoService::class.java)

    val subEspecieService: SubEspecieService get() = retrofit.create(SubEspecieService::class.java)

    val animalService: AnimalService get() = retrofit.create(AnimalService::class.java)

    init {
        retrofit = Retrofit.Builder().baseUrl("http:/192.168.31.180:8080")
                .addConverterFactory(JacksonConverterFactory.create()).build()
    }
}