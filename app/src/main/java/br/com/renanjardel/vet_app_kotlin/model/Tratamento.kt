package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

class Tratamento : Serializable {

    var codigo: Long? = null

    var resumo: String? = null

    private val consulta: Consulta? = null

    var listaMedicacoes: List<Medicacao>? = null

    constructor() : super() {}

    constructor(codigo: Long?, resumo: String, listaMedicacoes: List<Medicacao>) : super() {
        this.codigo = codigo
        this.resumo = resumo
        this.listaMedicacoes = listaMedicacoes
    }
}
