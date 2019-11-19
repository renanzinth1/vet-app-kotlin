package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

data class Medicacao(var codigo: Long? = null,
                var dosagem: Float = 0F,
                var tratamento: Tratamento = Tratamento(),
                var medicamento: Medicamento = Medicamento()) : Serializable