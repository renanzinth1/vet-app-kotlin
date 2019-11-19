package br.com.renanjardel.vet_app_kotlin.extension

fun String.formataCpf() =
    this.substring(0, 3) + "." + this.substring(3, 6) + "." + this.substring(6, 9) + "-" + this.substring(9, 11)