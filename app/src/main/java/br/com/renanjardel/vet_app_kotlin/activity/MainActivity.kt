package br.com.renanjardel.vet_app_kotlin.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.renanjardel.vet_app_kotlin.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        botao_listar_cliente.setOnClickListener {
            val clienteIntent = Intent(this@MainActivity, ClientesActivity::class.java)
            startActivity(clienteIntent)
        }

        botao_listar_veterinario.setOnClickListener {
            val veterinarioIntent = Intent(this@MainActivity, VeterinariosActivity::class.java)
            startActivity(veterinarioIntent)
        }

        botao_listar_medicamento.setOnClickListener {
            val medicamentoIntent = Intent(this@MainActivity, MedicamentosActivity::class.java)
            startActivity(medicamentoIntent)
        }

        botao_listar_especie.setOnClickListener {
            val especieIntent = Intent(this@MainActivity, EspeciesActivity::class.java)
            startActivity(especieIntent)
        }

    }
}