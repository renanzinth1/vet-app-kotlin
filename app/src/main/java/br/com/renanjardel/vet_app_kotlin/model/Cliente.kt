package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

class Cliente : Pessoa, Serializable {

    var cpf: String? = null

    //    public List<Animal> getListaAnimais() {
    //        return listaAnimais;
    //    }
    //
    //    public void setListaAnimais(List<Animal> listaAnimais) {
    //        this.listaAnimais = listaAnimais;
    //    }

    constructor() : super() {}

    constructor(cpf: String/*, List<Animal> listaAnimais*/) : super() {
        this.cpf = cpf
        //this.listaAnimais = listaAnimais;
    }
}