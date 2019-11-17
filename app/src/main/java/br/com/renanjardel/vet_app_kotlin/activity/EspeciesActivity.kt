package br.com.renanjardel.vet_app_kotlin.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.ContextMenu
import android.view.View
import android.widget.*
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.activity.form.FormEspecieActivity
import br.com.renanjardel.vet_app_kotlin.model.Especie
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EspeciesActivity : AppCompatActivity() {

    private var especiesView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_especies)

        carregaLista()

        val botaoNovaEspecie = findViewById<Button>(R.id.nova_especie)

        botaoNovaEspecie.setOnClickListener {
            val goCadastrarEspecie = Intent(this@EspeciesActivity, FormEspecieActivity::class.java)
            startActivity(goCadastrarEspecie)
        }

        especiesView = findViewById(R.id.lista_especie)

        especiesView!!.onItemClickListener = AdapterView.OnItemClickListener { lista, view, position, id ->
            val especie = lista.getItemAtPosition(position) as Especie
            val formEspecie = Intent(this@EspeciesActivity, FormEspecieActivity::class.java)
            formEspecie.putExtra("especie", especie)
            startActivity(formEspecie)
        }

        registerForContextMenu(especiesView)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val especie = especiesView!!.getItemAtPosition(info.position) as Especie

        val remover = menu.add("Remover")
        remover.setOnMenuItemClickListener {
            callExcludeAlertDialog(especie)
            false
        }

        val subespecies = menu.add("Subespécies")
        subespecies.setOnMenuItemClickListener {
            val goToListarSubEspecies = Intent(this@EspeciesActivity, SubEspeciesActivity::class.java)
            goToListarSubEspecies.putExtra("especie", especie)
            startActivity(goToListarSubEspecies)
            false
        }

        super.onCreateContextMenu(menu, v, menuInfo)
    }

    fun callExcludeAlertDialog(especie: Especie) {
        AlertDialog.Builder(this@EspeciesActivity)
                .setTitle("Excluir")
                .setIcon(R.drawable.ic_error_icon)
                .setMessage("Deseja excluir a especie " + especie.nome + "?")
                .setPositiveButton("Sim") { dialog, which ->
                    val removerEspecie = RetrofitInicializador().especieService.remover(especie.codigo!!)
                    removerEspecie.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.i("onResponse", "Requisição feita com sucesso!")
                            Toast.makeText(this@EspeciesActivity, "Especie removido!", Toast.LENGTH_SHORT).show()
                            carregaLista()
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("onFailure", "Requisão mal sucedida!")
                            Toast.makeText(this@EspeciesActivity, "Especie não removido!", Toast.LENGTH_SHORT).show()
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

    fun carregaLista() {
        //final ListView especiesView = findViewById(R.id.lista_especies);

        val especies = RetrofitInicializador().especieService.listar()

        especies.enqueue(object : Callback<List<Especie>> {
            override fun onResponse(call: Call<List<Especie>>, response: Response<List<Especie>>) {
                val especies = response.body()

                //EspeciesAdapter adapter = new EspeciesAdapter(EspeciesActivity.this, especies);
                val adapter = ArrayAdapter(this@EspeciesActivity, android.R.layout.simple_list_item_1, especies!!)
                especiesView!!.adapter = adapter
            }

            override fun onFailure(call: Call<List<Especie>>, t: Throwable) {

            }
        })
    }
}
