package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

class Medicamento : Serializable {

    var codigo: Long? = null

    var nome: String? = null

    var listaMedicacoes: List<Medicacao>? = null

    constructor() : super() {}

    constructor(codigo: Long?, nome: String, listaMedicacoes: List<Medicacao>) : super() {
        this.codigo = codigo
        this.nome = nome
        this.listaMedicacoes = listaMedicacoes
    }

}