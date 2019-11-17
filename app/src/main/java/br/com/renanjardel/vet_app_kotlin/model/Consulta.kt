package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable
import java.time.LocalDateTime

class Consulta : Serializable {

    var codigo: Long? = null

    var dataHora: LocalDateTime? = null

    var resumo: String? = null

    var veterinario: Veterinario? = null

    var animal: Animal? = null

    var listaTratamentos: List<Tratamento>? = null

    constructor() : super() {}

    constructor(codigo: Long?, dataHora: LocalDateTime, resumo: String,
                veterinario: Veterinario, animal: Animal, listaTratamentos: List<Tratamento>) : super() {
        this.codigo = codigo
        this.dataHora = dataHora
        this.resumo = resumo
        this.veterinario = veterinario
        this.animal = animal
        this.listaTratamentos = listaTratamentos
    }
}