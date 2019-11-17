package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

class SubEspecie : Serializable {

    var codigo: Long? = null

    var nome: String? = null

    var especie: Especie? = null

    constructor() : super() {}

    constructor(codigo: Long?, nome: String, especie: Especie) : super() {
        this.codigo = codigo
        this.nome = nome
        this.especie = especie
    }

}
