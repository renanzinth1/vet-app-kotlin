package br.com.renanjardel.vet_app_kotlin.helper

import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.activity.form.FormClienteActivity
import br.com.renanjardel.vet_app_kotlin.model.Cliente
import br.com.renanjardel.vet_app_kotlin.model.Sexo
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormularioClienteHelper(activity: FormClienteActivity) {

    private val campoNome: EditText
    private val campoSobrenome: EditText
    private val campoCpf: EditText
    private val campoTelefone: EditText
    private val campodataNascimento: EditText
    private val sexos: RadioGroup
    private val campoMasculino: RadioButton
    private val campoFeminino: RadioButton

    private var cliente: Cliente? = null

    init {
        campoNome = activity.findViewById(R.id.campo_cliente_nome)
        campoSobrenome = activity.findViewById(R.id.campo_cliente_sobrenome)
        campoCpf = activity.findViewById(R.id.campo_cliente_cpf)
        campoTelefone = activity.findViewById(R.id.campo_cliente_telefone)
        campodataNascimento = activity.findViewById(R.id.campo_cliente_dataNascimento)
        sexos = activity.findViewById(R.id.radioGroup_sexos)
        campoMasculino = activity.findViewById(R.id.campo_cliente_masculino)
        campoFeminino = activity.findViewById(R.id.campo_cliente_feminino)
        cliente = Cliente()
    }

    fun pegaCliente(): Cliente? {
        cliente!!.nome = campoNome.text.toString()
        cliente!!.sobrenome = campoSobrenome.text.toString()
        cliente!!.cpf = campoCpf.text.toString()
        cliente!!.telefone = campoTelefone.text.toString()

        cliente!!.dataNascimento = campodataNascimento.text.toString()

        //Pegando qual radiobutton foi selecinada
        val selectecId = sexos.checkedRadioButtonId

        when (selectecId) {
            R.id.campo_cliente_masculino -> cliente!!.sexo = Sexo.MASCULINO

            R.id.campo_cliente_feminino -> cliente!!.sexo = Sexo.FEMININO
        }

        return cliente
    }

    fun preencheFormulario(cliente: Cliente?) {
        val codigo = cliente?.codigo

        val retrofit = RetrofitInicializador().clienteService.buscarPorId(codigo!!)

        retrofit.enqueue(object : Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {

                val cliente = response.body()

                campoNome.setText(cliente!!.nome)
                campoSobrenome.setText(cliente.sobrenome)
                campoCpf.setText(cliente.cpf)
                campoTelefone.setText(cliente.telefone)
                campodataNascimento.setText(cliente.dataNascimento)

                val sexo = cliente.sexo

                if (sexo == Sexo.MASCULINO) {
                    campoMasculino.isChecked = true
                    campoFeminino.isChecked = false

                } else {
                    campoFeminino.isChecked = true
                    campoMasculino.isChecked = false
                }

                this@FormularioClienteHelper.cliente = cliente

                campoTrue(false)

            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {

            }
        })
    }

    fun campoTrue(value: Boolean) {
        campoNome.isEnabled = value
        campoSobrenome.isEnabled = value
        campoCpf.isEnabled = value
        campoTelefone.isEnabled = value
        campodataNascimento.isEnabled = value
        campoMasculino.isEnabled = value
        campoFeminino.isEnabled = value
    }
}
