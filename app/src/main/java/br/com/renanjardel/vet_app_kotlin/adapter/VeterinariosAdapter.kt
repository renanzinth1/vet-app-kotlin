package br.com.renanjardel.vet_app_kotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.model.Veterinario
import kotlinx.android.synthetic.main.veterinario_list_item.view.*

class VeterinariosAdapter(private val context: Context, private val listaVeterinarios: List<Veterinario>) : BaseAdapter() {

    override fun getCount(): Int {
        return listaVeterinarios.size
    }

    override fun getItem(position: Int): Any {
        return listaVeterinarios[position]
    }

    override fun getItemId(position: Int): Long {
        return listaVeterinarios[position].codigo!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val veterinario = listaVeterinarios[position]
        val inflater = LayoutInflater.from(context)

        var view = convertView
        if (convertView == null) {
            view = inflater.inflate(R.layout.veterinario_list_item, parent, false)
        }

        val campoNome = view!!.veterinario_item_nome
        val campoSobrenome = view.veterinario_item_sobrenome
        val campoCfmv = view.veterinario_item_cfmv

        //if (campoNome != null)
        campoNome.text = veterinario.nome

        //if (campoSobrenome != null)
        campoSobrenome.text = veterinario.sobrenome

        //if (campoCpf != null)
        campoCfmv.text = veterinario.cfmv

        return view
    }
}