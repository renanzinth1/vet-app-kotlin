package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

class Medicacao : Serializable {

    var codigo: Long? = null

    var tratamento: Tratamento? = null

    var medicamento: Medicamento? = null

    var dosagem: Float = 0.toFloat()

    constructor() : super() {}

    constructor(codigo: Long?, dosagem: Float, tratamento: Tratamento, medicamento: Medicamento) : super() {
        this.codigo = codigo
        this.dosagem = dosagem
        this.tratamento = tratamento
        this.medicamento = medicamento
    }
}