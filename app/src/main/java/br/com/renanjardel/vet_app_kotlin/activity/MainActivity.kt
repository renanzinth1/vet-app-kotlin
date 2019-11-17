package br.com.renanjardel.vet_app_kotlin.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageButton
import br.com.renanjardel.vet_app_kotlin.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val botaoCliente = findViewById<ImageButton>(R.id.botao_listar_cliente)
        val botaoVeterinario = findViewById<ImageButton>(R.id.botao_listar_veterinario)
        val botaoMedicamento = findViewById<ImageButton>(R.id.botao_listar_medicamento)
        val botaoEspecie = findViewById<ImageButton>(R.id.botao_listar_especie)

        botaoCliente.setOnClickListener {
            val clienteIntent = Intent(this@MainActivity, ClientesActivity::class.java)
            startActivity(clienteIntent)
        }

        botaoVeterinario.setOnClickListener {
            val veterinarioIntent = Intent(this@MainActivity, VeterinariosActivity::class.java)
            startActivity(veterinarioIntent)
        }

        botaoMedicamento.setOnClickListener {
            val medicamentoIntent = Intent(this@MainActivity, MedicamentosActivity::class.java)
            startActivity(medicamentoIntent)
        }

        botaoEspecie.setOnClickListener {
            val especieIntent = Intent(this@MainActivity, EspeciesActivity::class.java)
            startActivity(especieIntent)
        }

    }
}