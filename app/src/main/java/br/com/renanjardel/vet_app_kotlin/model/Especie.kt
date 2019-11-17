package br.com.renanjardel.vet_app_kotlin.model

import java.io.Serializable

class Especie : Serializable {

    var codigo: Long? = null

    var nome: String? = null

    //private List<SubEspecie> listaSubEspecies;

    constructor() : super() {}

    constructor(codigo: Long?, nome: String/*, List<SubEspecie> listaSubEspecies*/) : super() {
        this.codigo = codigo
        this.nome = nome
        //this.listaSubEspecies = listaSubEspecies;
    }

    //    public List<SubEspecie> getListaSubEspecies() {
    //        return listaSubEspecies;
    //    }
    //
    //    public void setListaSubEspecies(List<SubEspecie> listaSubEspecies) {
    //        this.listaSubEspecies = listaSubEspecies;
    //    }
}
