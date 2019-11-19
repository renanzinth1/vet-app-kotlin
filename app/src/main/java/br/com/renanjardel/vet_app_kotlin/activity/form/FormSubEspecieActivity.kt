package br.com.renanjardel.vet_app_kotlin.activity.form

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.helper.FormularioSubEspecieHelper
import br.com.renanjardel.vet_app_kotlin.model.Especie
import br.com.renanjardel.vet_app_kotlin.model.SubEspecie
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormSubEspecieActivity : AppCompatActivity() {

    private val helper: FormularioSubEspecieHelper by lazy{
        FormularioSubEspecieHelper(this)
    }
    private var especie: Especie? = null
    private var subEspecie: SubEspecie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_sub_especie)

        val intent = intent
        subEspecie = intent.getSerializableExtra("subEspecie") as? SubEspecie
        especie = intent.getSerializableExtra("especie") as? Especie

        if (subEspecie != null)
            helper.preencheFormulario(subEspecie)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_formulario, menu)

        if (subEspecie == null) {
            menu.findItem(R.id.menu_formulario_remover).isVisible = false
            menu.findItem(R.id.menu_formulario_editar).isVisible = false
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_formulario_remover -> this.remover(subEspecie)

            R.id.menu_formulario_editar -> helper.campoTrue(true)

            R.id.menu_formulario_salvar -> {
                val subEspecie = helper.pegaSubEspecie()

                if (subEspecie.codigo != null) {

                    val editar = RetrofitInicializador().subEspecieService.editar(subEspecie.codigo!!, subEspecie)
                    editar.enqueue(object : Callback<SubEspecie> {
                        override fun onResponse(call: Call<SubEspecie>, response: Response<SubEspecie>) {
                            val resposta = response.code()

                            if (resposta == 202) {
                                Log.i("onResponse", "Requisição feita com sucesso!")
                                Toast.makeText(this@FormSubEspecieActivity, "SubEspecie alterado!", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Log.e("onFailure", "Requisão mal sucedida!")
                                Toast.makeText(this@FormSubEspecieActivity, "SubEspecie não alterado!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<SubEspecie>, t: Throwable) {

                        }
                    })

                } else {

                    //Setando a especie recuperada em um objeto com o nome subEspecie do tipo SubEspecie
                    subEspecie.especie = especie!!
                    val call = RetrofitInicializador().subEspecieService.salvar(subEspecie)

                    call.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            val resposta = response.code()

                            if (resposta == 201) {
                                Log.i("onResponse", "Requisição feita com sucesso!")
                                Toast.makeText(this@FormSubEspecieActivity, "SubEspecie salvo!", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Log.e("onFailure", "Requisão mal sucedida!")
                                Toast.makeText(this@FormSubEspecieActivity, "SubEspecie não salvo!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            //Log.e("onFailure", "Requisão mal sucedida!");
                            //Toast.makeText(FormEspecieActivity.this, "Especie não salvo!", Toast.LENGTH_SHORT).show();
                            //finish();
                        }
                    })
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun remover(subEspecie: SubEspecie?) {
        AlertDialog.Builder(this@FormSubEspecieActivity)
                .setTitle("Excluir")
                .setIcon(R.drawable.ic_error_icon)
                .setMessage("Deseja excluir a Subespecie " + subEspecie!!.nome + "?")
                .setPositiveButton("Sim") { dialog, which ->
                    val removerSubEspecie = RetrofitInicializador().subEspecieService.remover(subEspecie.codigo)
                    removerSubEspecie.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.i("onResponse", "Requisição feita com sucesso!")
                            Toast.makeText(this@FormSubEspecieActivity, "Subespecie removido!", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("onFailure", "Requisão mal sucedida!")
                            Toast.makeText(this@FormSubEspecieActivity, "Subespecie não removido!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    })
                }
                .setNegativeButton("Não", null)
                .show()
    }
}
