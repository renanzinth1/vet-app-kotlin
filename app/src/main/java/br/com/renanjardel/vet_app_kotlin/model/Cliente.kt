package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

data class Cliente(var cpf: String = "") : Serializable, Pessoa() {

    override fun toString(): String {
        return super.nome
    }

    fun getCpfFormatado(): String {
        return this.cpf.substring(0, 3) + "." + this.cpf.substring(3, 6) + "." + this.cpf.substring(6, 9) + "-" + this.cpf.substring(9, 11)
    }
}