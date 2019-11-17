package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

open class Pessoa : Serializable {

    var codigo: Long? = null

    var nome: String? = null

    var sobrenome: String? = null

    var telefone: String? = null

    var dataNascimento: String? = null

    var sexo: Sexo? = null

    constructor() {}

    constructor(codigo: Long?, nome: String, sobrenome: String, telefone: String, dataNascimento: String, sexo: Sexo) {
        this.codigo = codigo
        this.nome = nome
        this.sobrenome = sobrenome
        this.telefone = telefone
        this.dataNascimento = dataNascimento
        this.sexo = sexo
    }
}
