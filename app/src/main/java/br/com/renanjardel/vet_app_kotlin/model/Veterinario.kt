package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

data class Veterinario(var cfmv: String = "") : Serializable, Pessoa()