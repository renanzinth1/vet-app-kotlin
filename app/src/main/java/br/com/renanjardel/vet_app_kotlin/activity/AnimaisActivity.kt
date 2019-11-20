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
import android.widget.Toast
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.activity.form.FormAnimalActivity
import br.com.renanjardel.vet_app_kotlin.model.Animal
import br.com.renanjardel.vet_app_kotlin.model.Cliente
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import kotlinx.android.synthetic.main.activity_animais.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AnimaisActivity : AppCompatActivity() {

    private var cliente = Cliente()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animais)

        val intent = intent
        cliente = intent.getSerializableExtra("cliente") as Cliente

        carregaLista()

        novo_animal.setOnClickListener {
            val goCadastrarAnimal = Intent(this@AnimaisActivity, FormAnimalActivity::class.java)
            goCadastrarAnimal.putExtra("cliente", cliente)
            startActivity(goCadastrarAnimal)
        }

        lista_animal.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val animal = parent.getItemAtPosition(position) as Animal
            val formAnimal = Intent(this@AnimaisActivity, FormAnimalActivity::class.java)
            formAnimal.putExtra("animal", animal)
            startActivity(formAnimal)
        }

        registerForContextMenu(lista_animal)

    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo) {
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val animal = lista_animal.getItemAtPosition(info.position) as Animal

        val remover = menu.add("Remover")
        remover.setOnMenuItemClickListener {
            callExcludeAlertDialog(animal)
            false
        }

        super.onCreateContextMenu(menu, v, menuInfo)
    }

    fun callExcludeAlertDialog(animal: Animal) {
        AlertDialog.Builder(this@AnimaisActivity)
                .setTitle("Excluir")
                .setIcon(R.drawable.ic_error_icon)
                .setMessage("Deseja excluir o Animal " + animal.nome + "?")
                .setPositiveButton("Sim") { dialog, which ->
                    val removerAnimal = RetrofitInicializador().animalService.remover(animal.codigo)
                    removerAnimal.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.i("onResponse", "Requisição feita com sucesso!")
                            Toast.makeText(this@AnimaisActivity, "Animal removido!", Toast.LENGTH_SHORT).show()
                            carregaLista()
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("onFailure", "Requisão mal sucedida!")
                            Toast.makeText(this@AnimaisActivity, "Animal não removido!", Toast.LENGTH_SHORT).show()
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

        val animais = RetrofitInicializador().clienteService.listarAnimais(cliente.codigo)

        animais.enqueue(object : Callback<List<Animal>> {
            override fun onResponse(call: Call<List<Animal>>, response: Response<List<Animal>>) {
                val animais = response.body()

                //SubEspeciesAdapter adapter = new SubEspeciesAdapter(SubEspeciesActivity.this, subespecies);
                val adapter = ArrayAdapter(this@AnimaisActivity, android.R.layout.simple_list_item_1, animais)
                lista_animal.adapter = adapter
            }

            override fun onFailure(call: Call<List<Animal>>, t: Throwable) {

            }
        })

    }
}
