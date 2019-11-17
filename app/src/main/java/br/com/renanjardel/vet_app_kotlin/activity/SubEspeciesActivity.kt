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
import br.com.renanjardel.vet_app_kotlin.activity.form.FormSubEspecieActivity
import br.com.renanjardel.vet_app_kotlin.model.Especie
import br.com.renanjardel.vet_app_kotlin.model.SubEspecie
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubEspeciesActivity : AppCompatActivity() {

    private var subEspeciesView: ListView? = null
    private var especie: Especie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_especies)

        val intent = intent
        especie = intent.getSerializableExtra("especie") as Especie

        carregaLista()

        val botaoNovaSubEspecie = findViewById<Button>(R.id.nova_subEspecie)

        botaoNovaSubEspecie.setOnClickListener {
            val goCadastrarSubEspecie = Intent(this@SubEspeciesActivity, FormSubEspecieActivity::class.java)

            //Passando objeto de especie para depois setar no subEspecie
            goCadastrarSubEspecie.putExtra("especie", especie)
            startActivity(goCadastrarSubEspecie)
        }

        subEspeciesView = findViewById(R.id.lista_subEspecie)

        subEspeciesView!!.onItemClickListener = AdapterView.OnItemClickListener { lista, view, position, id ->
            val subEspecie = lista.getItemAtPosition(position) as SubEspecie
            val formEspecie = Intent(this@SubEspeciesActivity, FormSubEspecieActivity::class.java)
            formEspecie.putExtra("subEspecie", subEspecie)
            startActivity(formEspecie)
        }

        registerForContextMenu(subEspeciesView)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val subEspecie = subEspeciesView!!.getItemAtPosition(info.position) as SubEspecie

        val remover = menu.add("Remover")
        remover.setOnMenuItemClickListener {
            callExcludeAlertDialog(subEspecie)
            false
        }

        super.onCreateContextMenu(menu, v, menuInfo)
    }

    fun callExcludeAlertDialog(subEspecie: SubEspecie) {
        AlertDialog.Builder(this@SubEspeciesActivity)
                .setTitle("Excluir")
                .setIcon(R.drawable.ic_error_icon)
                .setMessage("Deseja excluir a Subespecie " + subEspecie.nome + "?")
                .setPositiveButton("Sim") { dialog, which ->
                    val removersubEspecie = RetrofitInicializador().subEspecieService.remover(subEspecie.codigo)
                    removersubEspecie.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.i("onResponse", "Requisição feita com sucesso!")
                            Toast.makeText(this@SubEspeciesActivity, "Subespecie removido!", Toast.LENGTH_SHORT).show()
                            carregaLista()
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("onFailure", "Requisão mal sucedida!")
                            Toast.makeText(this@SubEspeciesActivity, "Subespecie não removido!", Toast.LENGTH_SHORT).show()
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

        val subespecies = RetrofitInicializador().especieService.listarSubEspecies(especie!!.codigo)

        subespecies.enqueue(object : Callback<List<SubEspecie>> {
            override fun onResponse(call: Call<List<SubEspecie>>, response: Response<List<SubEspecie>>) {
                val subespecies = response.body()

                //SubEspeciesAdapter adapter = new SubEspeciesAdapter(SubEspeciesActivity.this, subespecies);
                val adapter = ArrayAdapter(this@SubEspeciesActivity, android.R.layout.simple_list_item_1, subespecies!!)
                subEspeciesView!!.adapter = adapter
            }

            override fun onFailure(call: Call<List<SubEspecie>>, t: Throwable) {

            }
        })
    }
}
