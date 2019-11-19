package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

open class Pessoa (var codigo: Long? = null,
                   var nome: String = "",
                   var sobrenome: String = "",
                   var telefone: String = "",
                   var dataNascimento: String = "",
                   var sexo: Sexo = Sexo.MASCULINO) : Serializable
