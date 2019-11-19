package br.com.renanjardel.vet_app_kotlin.activity.form

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.activity.AnimaisActivity
import br.com.renanjardel.vet_app_kotlin.helper.FormularioClienteHelper
import br.com.renanjardel.vet_app_kotlin.model.Cliente
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import kotlinx.android.synthetic.main.activity_form_cliente.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormClienteActivity : AppCompatActivity() {

    private val helper: FormularioClienteHelper by lazy{
        FormularioClienteHelper(this)
    }

    private var cliente: Cliente? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form_cliente)

        val intent = intent
        cliente = intent.getSerializableExtra("cliente") as? Cliente

        if (cliente != null)
            helper.preencheFormulario(cliente)
        else
            listar_animais.visibility = View.GONE

        listar_animais.setOnClickListener {
            val intent = Intent(this@FormClienteActivity, AnimaisActivity::class.java)
            intent.putExtra("cliente", cliente)
            startActivity(intent)
        }

    }

    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //
    //        getMenuInflater().inflate(R.menu.menu_formulario, menu);
    //        return super.onCreateOptionsMenu(menu);
    //    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_formulario, menu)

        if (cliente == null) {
            menu.findItem(R.id.menu_formulario_remover).isVisible = false
            menu.findItem(R.id.menu_formulario_editar).isVisible = false
        }

        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_formulario_remover -> this.remover(cliente!!)

            R.id.menu_formulario_editar -> helper.campoTrue(true)

            R.id.menu_formulario_salvar -> {
                val cliente = helper.pegaCliente()

                if (cliente!!.codigo != null) {
                    val editar = RetrofitInicializador().clienteService.editar(cliente.codigo!!, cliente)

                    editar.enqueue(object : Callback<Cliente> {
                        override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                            val resposta = response.code()

                            if (resposta == 202) {
                                Log.i("onResponse", "Requisição feita com sucesso!")
                                Toast.makeText(this@FormClienteActivity, "Cliente alterado!", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Log.e("onFailure", "Requisão mal sucedida!")
                                Toast.makeText(this@FormClienteActivity, "Cliente não alterado!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<Cliente>, t: Throwable) {

                        }
                    })

                } else {
                    val call = RetrofitInicializador().clienteService.salvar(cliente)

                    call.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            val resposta = response.code()

                            if (resposta == 201) {
                                Log.i("onResponse", "Requisição feita com sucesso!")
                                Toast.makeText(this@FormClienteActivity, "Cliente salvo!", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Log.e("onFailure", "Requisão mal sucedida!")
                                Toast.makeText(this@FormClienteActivity, "Cliente não salvo!", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            //Log.e("onFailure", "Requisão mal sucedida!");
                            //Toast.makeText(FormClienteActivity.this, "Cliente não salvo!", Toast.LENGTH_SHORT).show();
                            //finish();
                        }
                    })
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun remover(cliente: Cliente) {
        AlertDialog.Builder(this@FormClienteActivity)
                .setTitle("Excluir")
                .setIcon(R.drawable.ic_error_icon)
                .setMessage("Deseja excluir o cliente " + cliente.nome + " " + cliente.sobrenome + "?")
                .setPositiveButton("Sim") { dialog, which ->
                    val removerCliente = RetrofitInicializador().clienteService.remover(cliente.codigo)
                    removerCliente.enqueue(object : Callback<Void> {
                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            Log.i("onResponse", "Requisição feita com sucesso!")
                            Toast.makeText(this@FormClienteActivity, "Cliente removido!", Toast.LENGTH_SHORT).show()
                            finish()
                        }

                        override fun onFailure(call: Call<Void>, t: Throwable) {
                            Log.e("onFailure", "Requisão mal sucedida!")
                            Toast.makeText(this@FormClienteActivity, "Cliente não removido!", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    })
                }
                .setNegativeButton("Não", null)
                .show()
    }
}
