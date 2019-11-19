package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

data class Cliente(var cpf: String = "") : Serializable, Pessoa() {

    override fun toString(): String {
        return super.nome
    }
}