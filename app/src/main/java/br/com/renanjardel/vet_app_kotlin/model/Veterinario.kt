package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

class Veterinario : Pessoa, Serializable {

    var cfmv: String? = null

    constructor() : super() {}

    constructor(cfmv: String) : super() {
        this.cfmv = cfmv
    }
}