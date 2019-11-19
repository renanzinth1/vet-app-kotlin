package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

data class Medicamento(var codigo: Long? = null,
                       var nome: String = "",
                       var listaMedicacoes: MutableList<Medicacao> = mutableListOf()) : Serializable {

    override fun toString(): String {
        return this.nome
    }
}