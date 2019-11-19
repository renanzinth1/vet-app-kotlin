package br.com.renanjardel.vet_app_kotlin.activity.form

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.helper.FormularioAnimalHelper
import br.com.renanjardel.vet_app_kotlin.model.Animal
import br.com.renanjardel.vet_app_kotlin.model.Cliente
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormAnimalActivity : AppCompatActivity() {

    private var cliente: Cliente? = null
    private var animal: Animal? = null

    private val helper: FormularioAnimalHelper by lazy{
        FormularioAnimalHelper(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_animal)

        val intent = intent

        helper.carregaSpinnerEspecieESubEspecie()

        //Pegando o objeto cliente do formulário
        cliente = intent.getSerializableExtra("cliente") as? Cliente
        //Pegando o objeto animal da lista de animais ao selecionar
        animal = intent.getSerializableExtra("animal") as? Animal

        if (animal != null)
            helper.preencherFomulario(animal)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_formulario, menu)

        if (animal == null) {
            menu.findItem(R.id.menu_formulario_remover).isVisible = false
            menu.findItem(R.id.menu_formulario_editar).isVisible = false
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_formulario_remover -> this.remover(animal)

            R.id.menu_formulario_editar -> helper.campoTrue(true)

            R.id.menu_formulario_salvar -> {
                animal = helper.pegaAnimal()

                if (animal!!.codigo != null) {

                    val editar = RetrofitInicializador().animalService.editar(animal!!.codigo!!, animal)
                    editar.enqueue(object : Callback<Animal> {
                        override fun onResponse(call: Call<Animal>, response: Response<Animal>) {
                            val resposta = response.code()

                            if (resposta == 202) {
                                Log.i("onResponse", "Requisição feita com sucesso!")
                                Toast.makeText(this@FormAnimalActivity, "Animal alterado!", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Log.e("onFailure", "Requisão mal sucedida!")
                                Toast.makeText(this@FormAnimalActivity, "Animal não alterado!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<Animal>, t: Throwable) {

                        }
                    })

                } else {

                    animal!!.cliente = this.cliente!!
                    val salvarCall = RetrofitInicializador().animalService.salvar(animal)

                    salvarCall.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            val resposta = response.code()

                            if (resposta == 201) {
                                Log.i("onResponse", "Requisição feita com sucesso!")
                                Toast.makeText(this@FormAnimalActivity, "Animal salvo!", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Log.e("onFailure", "Requisão mal sucedida!")
                                Toast.makeText(this@FormAnimalActivity, "Animal não salvo!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {

                        }
                    })

                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun remover(animal: Animal?) {
        AlertDialog.Builder(this@FormAnimalActivity)
                .setTitle("Excluir")
                .setIcon(R.drawable.ic_error_icon)
                .setMessage("Deseja excluir o Animal " + animal!!.nome + "?")
                .setPositiveButton("Sim") { _, _ ->
                    val removerAnimal = RetrofitInicializador().animalService.remover(animal.codigo)
                    removerAnimal.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.i("onResponse", "Requisição feita com sucesso!")
                            Toast.makeText(this@FormAnimalActivity, "Animal removido!", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("onFailure", "Requisão mal sucedida!")
                            Toast.makeText(this@FormAnimalActivity, "Animal não removido!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    })
                }
                .setNegativeButton("Não", null)
                .show()
    }
}
