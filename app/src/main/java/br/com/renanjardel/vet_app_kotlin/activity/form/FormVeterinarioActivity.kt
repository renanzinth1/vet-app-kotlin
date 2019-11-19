package br.com.renanjardel.vet_app_kotlin.activity.form

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.helper.FormularioVeterinarioHelper
import br.com.renanjardel.vet_app_kotlin.model.Veterinario
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormVeterinarioActivity : AppCompatActivity() {

    private val helper: FormularioVeterinarioHelper by lazy{
        FormularioVeterinarioHelper(this)
    }
    private var veterinario: Veterinario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_veterinario)

        val intent = intent
        veterinario = intent.getSerializableExtra("veterinario") as? Veterinario

        if (veterinario != null)
            helper.preencheFormulario(veterinario)
    }

    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //
    //        getMenuInflater().inflate(R.menu.menu_formulario, menu);
    //        return super.onCreateOptionsMenu(menu);
    //    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_formulario, menu)

        if (veterinario == null) {
            menu.findItem(R.id.menu_formulario_remover).isVisible = false
            menu.findItem(R.id.menu_formulario_editar).isVisible = false
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_formulario_remover -> this.remover(veterinario!!)

            R.id.menu_formulario_editar -> helper.campoTrue(true)

            R.id.menu_formulario_salvar -> {

                val veterinario = helper.pegaVeterinario()

                if (veterinario.codigo != null) {
                    val editar = RetrofitInicializador().veterinarioService.editar(veterinario.codigo!!, veterinario)
                    editar.enqueue(object : Callback<Veterinario> {
                        override fun onResponse(call: Call<Veterinario>, response: Response<Veterinario>) {
                            val resposta = response.code()

                            if (resposta == 202) {
                                Log.i("onResponse", "Requisição feita com sucesso!")
                                Toast.makeText(this@FormVeterinarioActivity, "Veterinário alterado!", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Log.e("onFailure", "Requisão mal sucedida!")
                                Toast.makeText(this@FormVeterinarioActivity, "Veterinário não alterado!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<Veterinario>, t: Throwable) {
                            //                            Log.e("onFailure", "Requisão mal sucedida!");
                            //                            Toast.makeText(FormVeterinarioActivity.this, "Veterinário não alterado!", Toast.LENGTH_SHORT).show();
                            //                            finish();
                        }
                    })

                } else {
                    val call = RetrofitInicializador().veterinarioService.salvar(veterinario)

                    call.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            val resposta = response.code()

                            if (resposta == 201) {
                                Log.i("onResponse", "Requisição feita com sucesso!")
                                Toast.makeText(this@FormVeterinarioActivity, "Veterinario salvo!", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Log.e("onFailure", "Requisão mal sucedida!")
                                Toast.makeText(this@FormVeterinarioActivity, "Veterinario não salvo!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            //Log.e("onFailure", "Requisão mal sucedida!");
                            //Toast.makeText(FormVeterinarioActivity.this, "Veterinario não salvo!", Toast.LENGTH_SHORT).show();
                            //finish();
                        }
                    })
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun remover(veterinario: Veterinario) {
        AlertDialog.Builder(this@FormVeterinarioActivity)
                .setTitle("Excluir")
                .setIcon(R.drawable.ic_error_icon)
                .setMessage("Deseja excluir o veterinário " + veterinario.nome + " " + veterinario.sobrenome + "?")
                .setPositiveButton("Sim") { dialog, which ->
                    val removerVeterinario = RetrofitInicializador().veterinarioService.remover(veterinario.codigo)
                    removerVeterinario.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.i("onResponse", "Requisição feita com sucesso!")
                            Toast.makeText(this@FormVeterinarioActivity, "Veterinario removido!", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("onFailure", "Requisão mal sucedida!")
                            Toast.makeText(this@FormVeterinarioActivity, "Veterinario não removido!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    })
                }
                .setNegativeButton("Não", null)
                .show()
    }
}
