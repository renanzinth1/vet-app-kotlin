package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

data class Animal (var codigo: Long? = null,
                   var nome: String = "",
                   var dataNascimento: String = "",
                   var sexo: SexoAnimal = SexoAnimal.MACHO,
                   var subEspecie: SubEspecie = SubEspecie(),
                   var cliente: Cliente = Cliente()) : Serializable {

    override fun toString(): String {
        return this.nome
    }

}
