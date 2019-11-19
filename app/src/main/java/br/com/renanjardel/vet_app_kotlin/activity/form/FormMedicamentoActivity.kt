package br.com.renanjardel.vet_app_kotlin.activity.form

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.helper.FormularioMedicamentoHelper
import br.com.renanjardel.vet_app_kotlin.model.Medicamento
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormMedicamentoActivity : AppCompatActivity() {

    private val helper: FormularioMedicamentoHelper by lazy{
        FormularioMedicamentoHelper(this)
    }
    private var medicamento: Medicamento? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_medicamento)

        val intent = intent
        medicamento = intent.getSerializableExtra("medicamento") as? Medicamento

        if (medicamento != null)
            helper.preencherFormulario(medicamento)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_formulario, menu)

        if (medicamento == null) {
            menu.findItem(R.id.menu_formulario_remover).isVisible = false
            menu.findItem(R.id.menu_formulario_editar).isVisible = false
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_formulario_remover -> this.remover(medicamento!!)

            R.id.menu_formulario_editar -> helper.campoTrue(true)

            R.id.menu_formulario_salvar -> {
                val medicamento = helper.pegaMedicamento()

                if (medicamento.codigo != null) {
                    val editar = RetrofitInicializador().medicamentoService.editar(medicamento.codigo!!, medicamento)

                    editar.enqueue(object : Callback<Medicamento> {
                        override fun onResponse(call: Call<Medicamento>, response: Response<Medicamento>) {
                            val resposta = response.code()

                            if (resposta == 202) {
                                Log.i("onResponse", "Requisição feita com sucesso!")
                                Toast.makeText(this@FormMedicamentoActivity, "Medicamento alterado!", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Log.e("onFailure", "Requisão mal sucedida!")
                                Toast.makeText(this@FormMedicamentoActivity, "Medicamento não alterado!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<Medicamento>, t: Throwable) {

                        }
                    })

                } else {
                    val salvar = RetrofitInicializador().medicamentoService.salvar(medicamento)

                    salvar.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            val resposta = response.code()

                            if (resposta == 201) {
                                Log.i("onResponse", "Requisição feita com sucesso!")
                                Toast.makeText(this@FormMedicamentoActivity, "Medicamento salvo!", Toast.LENGTH_SHORT).show()
                                finish()

                            } else {
                                Log.e("onFailure", "Requisão mal sucedida!")
                                Toast.makeText(this@FormMedicamentoActivity, "Medicamento não salvo!", Toast.LENGTH_SHORT).show()
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

    private fun remover(medicamento: Medicamento) {
        AlertDialog.Builder(this@FormMedicamentoActivity)
                .setTitle("Excluir")
                .setIcon(R.drawable.ic_error_icon)
                .setMessage("Deseja excluir o medicamento " + medicamento.nome + "?")
                .setPositiveButton("Sim") { dialog, which ->
                    val removerMedicamento = RetrofitInicializador().medicamentoService.remover(medicamento.codigo!!)
                    removerMedicamento.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.i("onResponse", "Requisição feita com sucesso!")
                            Toast.makeText(this@FormMedicamentoActivity, "Medicamento removido!", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("onFailure", "Requisão mal sucedida!")
                            Toast.makeText(this@FormMedicamentoActivity, "Medicamento não removido!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    })
                }
                .setNegativeButton("Não", null)
                .show()
    }
}
