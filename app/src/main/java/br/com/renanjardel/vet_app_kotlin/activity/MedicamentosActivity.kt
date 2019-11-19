package br.com.renanjardel.vet_app_kotlin.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ContextMenu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.activity.form.FormMedicamentoActivity
import br.com.renanjardel.vet_app_kotlin.model.Medicamento
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import kotlinx.android.synthetic.main.activity_medicamentos.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MedicamentosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicamentos)

        carregaLista()

        val botaoNovoMedicamento = findViewById<Button>(R.id.novo_medicamento)

        botaoNovoMedicamento.setOnClickListener {
            val goCadastrarMedicamento = Intent(this@MedicamentosActivity, FormMedicamentoActivity::class.java)
            startActivity(goCadastrarMedicamento)
        }

        lista_medicamento.onItemClickListener = AdapterView.OnItemClickListener { lista, view, position, id ->
            val medicamento = lista.getItemAtPosition(position) as Medicamento
            val formMedicamento = Intent(this@MedicamentosActivity, FormMedicamentoActivity::class.java)
            formMedicamento.putExtra("medicamento", medicamento)
            startActivity(formMedicamento)
        }

        registerForContextMenu(lista_medicamento)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val medicamento = lista_medicamento.getItemAtPosition(info.position) as Medicamento

        val remover = menu.add("Remover")
        remover.setOnMenuItemClickListener {
            callExcludeAlertDialog(medicamento)
            false
        }

        super.onCreateContextMenu(menu, v, menuInfo)
    }

    private fun callExcludeAlertDialog(medicamento: Medicamento) {
        AlertDialog.Builder(this@MedicamentosActivity)
                .setTitle("Excluir")
                .setIcon(R.drawable.ic_error_icon)
                .setMessage("Deseja excluir o medicamento " + medicamento.nome + "?")
                .setPositiveButton("Sim") { dialog, which ->
                    val removerMedicamento = RetrofitInicializador().medicamentoService.remover(medicamento.codigo!!)
                    removerMedicamento.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.i("onResponse", "Requisição feita com sucesso!")
                            Toast.makeText(this@MedicamentosActivity, "Medicamento removido!", Toast.LENGTH_SHORT).show()
                            carregaLista()

                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("onFailure", "Requisão mal sucedida!")
                            Toast.makeText(this@MedicamentosActivity, "Medicamento não removido!", Toast.LENGTH_SHORT).show()
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

        val medicamentos = RetrofitInicializador().medicamentoService.listar()

        medicamentos.enqueue(object : Callback<List<Medicamento>> {
            override fun onResponse(call: Call<List<Medicamento>>, response: Response<List<Medicamento>>) {
                val medicamentos = response.body()

                //ClientesAdapter adapter = new ClientesAdapter(ClientesActivity.this, clientes);
                val adapter = ArrayAdapter(this@MedicamentosActivity, android.R.layout.simple_list_item_1, medicamentos)
                lista_medicamento.adapter = adapter
            }

            override fun onFailure(call: Call<List<Medicamento>>, t: Throwable) {

            }
        })
    }
}
