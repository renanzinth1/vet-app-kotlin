package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable
import java.time.LocalDate

class Animal : Serializable {

    var codigo: Long? = null

    var nome: String? = null

    var dataNascimento: String? = null

    var sexo: SexoAnimal? = null

    var subEspecie: SubEspecie? = null

    var cliente: Cliente? = null

    constructor() : super() {}

    constructor(codigo: Long?, nome: String, dataNascimento: String, sexo: SexoAnimal, subEspecie: SubEspecie, cliente: Cliente) {
        this.codigo = codigo
        this.nome = nome
        this.dataNascimento = dataNascimento
        this.sexo = sexo
        this.subEspecie = subEspecie
        this.cliente = cliente
    }

}
