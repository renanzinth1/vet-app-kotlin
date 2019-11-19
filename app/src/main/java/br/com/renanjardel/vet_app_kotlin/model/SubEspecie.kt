package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

data class SubEspecie (var codigo: Long? = null,
                  var nome: String = "",
                  var especie: Especie = Especie()) : Serializable {

    override fun toString(): String {
        return this.nome
    }

}
