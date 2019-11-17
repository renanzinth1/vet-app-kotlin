package br.com.renanjardel.vet_app_kotlin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.extension.formataCpf
import br.com.renanjardel.vet_app_kotlin.model.Cliente

class ClientesAdapter(private val context: Context, private val listaClientes: List<Cliente>) : BaseAdapter() {

    override fun getCount(): Int {
        return listaClientes.size
    }

    override fun getItem(position: Int): Any {
        return listaClientes[position]
    }

    override fun getItemId(position: Int): Long {
        return listaClientes[position].codigo!!
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val cliente = listaClientes[position]
        val inflater = LayoutInflater.from(context)

        var view = convertView
        if (convertView == null) {
            view = inflater.inflate(R.layout.cliente_list_item, parent, false)
        }

        val campoNome = view!!.findViewById<TextView>(R.id.cliente_item_nome)
        val campoSobrenome = view.findViewById<TextView>(R.id.cliente_item_sobrenome)
        val campoCpf = view.findViewById<TextView>(R.id.cliente_item_cpf)

        //if (campoNome != null)
        campoNome.text = cliente.nome

        //if (campoSobrenome != null)
        campoSobrenome.text = cliente.sobrenome

        //if (campoCpf != null)
        campoCpf.text = cliente.cpf?.formataCpf()

        return view
    }

}
