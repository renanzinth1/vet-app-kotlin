package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable
import java.time.LocalDateTime
import java.util.*

data class Consulta (var codigo: Long? = null,
                     var dataHora: String = "",
                     var resumo: String = "",
                     var veterinario: Veterinario = Veterinario(),
                     var animal: Animal = Animal(),
                     var listaTratamentos: MutableList<Tratamento> = mutableListOf()) : Serializable