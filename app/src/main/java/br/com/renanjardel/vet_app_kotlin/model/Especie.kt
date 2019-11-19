package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

data class Especie(var codigo: Long? = null,
                   var nome: String = ""): Serializable {

    override fun toString(): String {
        return this.nome
    }

}
