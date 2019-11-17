package br.com.renanjardel.vet_app_kotlin.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ContextMenu
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.activity.form.FormClienteActivity
import br.com.renanjardel.vet_app_kotlin.adapter.ClientesAdapter
import br.com.renanjardel.vet_app_kotlin.model.Cliente
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClientesActivity : AppCompatActivity() {

    private var clientesView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clientes)

        carregaLista()

        val botaoNovoCliente = findViewById<Button>(R.id.novo_cliente)

        botaoNovoCliente.setOnClickListener {
            val goCadastrarCliente = Intent(this@ClientesActivity, FormClienteActivity::class.java)
            startActivity(goCadastrarCliente)
        }

        clientesView = findViewById(R.id.lista_clientes)

        clientesView!!.onItemClickListener = AdapterView.OnItemClickListener { lista, view, position, id ->
            val cliente = lista.getItemAtPosition(position) as Cliente
            val formCliente = Intent(this@ClientesActivity, FormClienteActivity::class.java)
            formCliente.putExtra("cliente", cliente)
            startActivity(formCliente)
        }

        registerForContextMenu(clientesView)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val cliente = clientesView!!.getItemAtPosition(info.position) as Cliente

        val remover = menu.add("Remover")
        remover.setOnMenuItemClickListener {
            callExcludeAlertDialog(cliente)
            false
        }

        val animais = menu.add("Animais")
        animais.setOnMenuItemClickListener {
            val goToListarAnimais = Intent(this@ClientesActivity, AnimaisActivity::class.java)
            goToListarAnimais.putExtra("cliente", cliente)
            startActivity(goToListarAnimais)
            false
        }

        super.onCreateContextMenu(menu, v, menuInfo)
    }

    private fun callExcludeAlertDialog(cliente: Cliente) {
        AlertDialog.Builder(this@ClientesActivity)
                .setTitle("Excluir")
                .setIcon(R.drawable.ic_error_icon)
                .setMessage("Deseja excluir o cliente " + cliente.nome + " " + cliente.sobrenome + "?")
                .setPositiveButton("Sim") { dialog, which ->
                    val removerCliente = RetrofitInicializador().clienteService.remover(cliente.codigo)
                    removerCliente.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.i("onResponse", "Requisição feita com sucesso!")
                            Toast.makeText(this@ClientesActivity, "Cliente removido!", Toast.LENGTH_SHORT).show()
                            carregaLista()
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("onFailure", "Requisão mal sucedida!")
                            Toast.makeText(this@ClientesActivity, "Cliente não removido!", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                .setNegativeButton("Não", null)
                .show()
    }

    override fun onResume() {
        carregaLista()
        super.onResume()
    }

    private fun carregaLista() {
        //final ListView clientesView = findViewById(R.id.lista_clientes);

        val clientes = RetrofitInicializador().clienteService.listar()

        clientes.enqueue(object : Callback<List<Cliente>> {
            override fun onResponse(call: Call<List<Cliente>>, response: Response<List<Cliente>>) {
                val clientes = response.body()

                val adapter = ClientesAdapter(this@ClientesActivity, clientes!!)
                //ArrayAdapter<Cliente> adapter = new ArrayAdapter<Cliente> (ClientesActivity.this, android.R.layout.simple_list_item_1, clientes);
                clientesView!!.adapter = adapter
            }

            override fun onFailure(call: Call<List<Cliente>>, t: Throwable) {

            }
        })
    }
}
