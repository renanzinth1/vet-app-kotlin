package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

data class Tratamento(var codigo: Long? = null,
                      var resumo: String = "",
                      var listaMedicacoes: MutableList<Medicacao> = mutableListOf()) : Serializable
