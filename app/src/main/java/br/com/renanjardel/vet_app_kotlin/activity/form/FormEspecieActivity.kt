package br.com.renanjardel.vet_app_kotlin.activity.form

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.activity.SubEspeciesActivity
import br.com.renanjardel.vet_app_kotlin.helper.FormularioEspecieHelper
import br.com.renanjardel.vet_app_kotlin.model.Especie
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormEspecieActivity : AppCompatActivity() {

    private var helper: FormularioEspecieHelper? = null

    private var btnListarSubEspecies: ImageButton? = null
    private var especie: Especie? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_especie)

        helper = FormularioEspecieHelper(this)
        btnListarSubEspecies = findViewById(R.id.listar_subEspecie)

        val intent = intent
        especie = intent.getSerializableExtra("especie") as Especie

        if (especie != null)
            helper!!.preencheFormulario(especie)
        else
            btnListarSubEspecies!!.visibility = View.GONE

        btnListarSubEspecies!!.setOnClickListener {
            val intent = Intent(this@FormEspecieActivity, SubEspeciesActivity::class.java)
            intent.putExtra("especie", especie)
            startActivity(intent)
        }

    }

    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        getMenuInflater().inflate(R.menu.menu_formulario, menu);
    //
    //        return super.onCreateOptionsMenu(menu);
    //    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_formulario, menu)

        if (especie == null) {
            menu.findItem(R.id.menu_formulario_remover).isVisible = false
            menu.findItem(R.id.menu_formulario_editar).isVisible = false
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_formulario_remover -> this.remover(especie)

            R.id.menu_formulario_editar -> helper!!.campoTrue(true)

            R.id.menu_formulario_salvar -> {
                val especie = helper!!.pegaEspecie()

                if (especie?.codigo != null) {
                    val editar = RetrofitInicializador().especieService.editar(especie?.codigo!!, especie)
                    editar.enqueue(object : Callback<Especie> {
                        override fun onResponse(call: Call<Especie>, response: Response<Especie>) {
                            val resposta = response.code()

                            if (resposta == 202) {
                                Log.i("onResponse", "Requisição feita com sucesso!")
                                Toast.makeText(this@FormEspecieActivity, "Especie alterado!", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Log.e("onFailure", "Requisão mal sucedida!")
                                Toast.makeText(this@FormEspecieActivity, "Especie não alterado!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<Especie>, t: Throwable) {

                        }
                    })

                } else {
                    val call = RetrofitInicializador().especieService.salvar(especie)

                    call.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            val resposta = response.code()

                            if (resposta == 201) {
                                Log.i("onResponse", "Requisição feita com sucesso!")
                                Toast.makeText(this@FormEspecieActivity, "Especie salvo!", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Log.e("onFailure", "Requisão mal sucedida!")
                                Toast.makeText(this@FormEspecieActivity, "Especie não salvo!", Toast.LENGTH_SHORT).show()
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

    fun remover(especie: Especie?) {
        AlertDialog.Builder(this@FormEspecieActivity)
                .setTitle("Excluir")
                .setIcon(R.drawable.ic_error_icon)
                .setMessage("Deseja excluir a especie " + especie!!.nome + "?")
                .setPositiveButton("Sim") { dialog, which ->
                    val removerEspecie = RetrofitInicializador().especieService.remover(especie.codigo!!)
                    removerEspecie.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.i("onResponse", "Requisição feita com sucesso!")
                            Toast.makeText(this@FormEspecieActivity, "Especie removido!", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("onFailure", "Requisão mal sucedida!")
                            Toast.makeText(this@FormEspecieActivity, "Especie não removido!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    })
                }
                .setNegativeButton("Não", null)
                .show()
    }
}
