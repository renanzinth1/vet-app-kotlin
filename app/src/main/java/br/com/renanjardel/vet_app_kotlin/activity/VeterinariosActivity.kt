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
import br.com.renanjardel.vet_app_kotlin.activity.form.FormVeterinarioActivity
import br.com.renanjardel.vet_app_kotlin.adapter.VeterinariosAdapter
import br.com.renanjardel.vet_app_kotlin.model.Veterinario
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VeterinariosActivity : AppCompatActivity() {

    private var veterinariosView: ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_veterinarios)

        val botaoNovoVeterinario = findViewById<Button>(R.id.novo_veterinario)

        botaoNovoVeterinario.setOnClickListener {
            val goCadastrarVeterinario = Intent(this@VeterinariosActivity, FormVeterinarioActivity::class.java)
            startActivity(goCadastrarVeterinario)
        }

        veterinariosView = findViewById(R.id.lista_veterinario)

        veterinariosView!!.onItemClickListener = AdapterView.OnItemClickListener { lista, view, position, l ->
            val veterinario = lista.getItemAtPosition(position) as Veterinario
            val intent = Intent(this@VeterinariosActivity, FormVeterinarioActivity::class.java)
            intent.putExtra("veterinario", veterinario)
            startActivity(intent)
        }

        registerForContextMenu(veterinariosView)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val veterinario = veterinariosView!!.getItemAtPosition(info.position) as Veterinario

        val remover = menu.add("Remover")
        remover.setOnMenuItemClickListener {
            callExcludeAlertDialog(veterinario)
            false
        }

        super.onCreateContextMenu(menu, v, menuInfo)
    }

    private fun callExcludeAlertDialog(veterinario: Veterinario) {
        AlertDialog.Builder(this@VeterinariosActivity)
                .setTitle("Excluir")
                .setIcon(R.drawable.ic_error_icon)
                .setMessage("Deseja excluir o veterinário " + veterinario.nome + " " + veterinario.sobrenome + "?")
                .setPositiveButton("Sim") { dialog, which ->
                    val removerVeterinario = RetrofitInicializador().veterinarioService.remover(veterinario.codigo)
                    removerVeterinario.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.i("onResponse", "Requisição feita com sucesso!")
                            Toast.makeText(this@VeterinariosActivity, "Veterinario removido!", Toast.LENGTH_SHORT).show()
                            carregaLista()
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("onFailure", "Requisão mal sucedida!")
                            Toast.makeText(this@VeterinariosActivity, "Veterinario não removido!", Toast.LENGTH_SHORT).show()
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


        val veterinarios = RetrofitInicializador().veterinarioService.listar()

        veterinarios.enqueue(object : Callback<List<Veterinario>> {
            override fun onResponse(call: Call<List<Veterinario>>, response: Response<List<Veterinario>>) {
                val veterinarios = response.body()

                val adapter = VeterinariosAdapter(this@VeterinariosActivity, veterinarios!!)
                //ArrayAdapter<Veterinario> adapter = new ArrayAdapter<Veterinario> (VeterinariosActivity.this, android.R.layout.simple_list_item_1, Veterinarios);
                veterinariosView!!.adapter = adapter
            }

            override fun onFailure(call: Call<List<Veterinario>>, t: Throwable) {

            }
        })
    }
}
