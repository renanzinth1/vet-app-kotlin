package br.com.renanjardel.vet_app_kotlin.helper

import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.activity.form.FormClienteActivity
import br.com.renanjardel.vet_app_kotlin.model.Cliente
import br.com.renanjardel.vet_app_kotlin.model.Sexo
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import kotlinx.android.synthetic.main.activity_form_cliente.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormularioClienteHelper(private val activity: FormClienteActivity) {

    private var cliente = Cliente()

    fun pegaCliente(): Cliente? {
        cliente.nome = activity.campo_cliente_nome.text.toString()
        cliente.sobrenome = activity.campo_cliente_sobrenome.text.toString()
        cliente.cpf = activity.campo_cliente_cpf.text.toString()
        cliente.telefone = activity.campo_cliente_telefone.text.toString()

        cliente.dataNascimento = activity.campo_cliente_dataNascimento.text.toString()

        //Pegando qual radiobutton foi selecinada
        val selectecId = activity.radioGroup_sexos.checkedRadioButtonId

        when (selectecId) {
            R.id.campo_cliente_masculino -> cliente.sexo = Sexo.MASCULINO

            R.id.campo_cliente_feminino -> cliente.sexo = Sexo.FEMININO
        }

        return cliente
    }

    fun preencheFormulario(cliente: Cliente?) {
        val codigo = cliente?.codigo

        val retrofit = RetrofitInicializador().clienteService.buscarPorId(codigo!!)

        retrofit.enqueue(object : Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {

                val cliente = response.body()

                activity.campo_cliente_nome.setText(cliente!!.nome)
                activity.campo_cliente_sobrenome.setText(cliente.sobrenome)
                activity.campo_cliente_cpf.setText(cliente.cpf)
                activity.campo_cliente_telefone.setText(cliente.telefone)
                activity.campo_cliente_dataNascimento.setText(cliente.dataNascimento)

                val sexo = cliente.sexo

                if (sexo == Sexo.MASCULINO) {
                    activity.campo_cliente_masculino.isChecked = true
                    activity.campo_cliente_feminino.isChecked = false

                } else {
                    activity.campo_cliente_feminino.isChecked = true
                    activity.campo_cliente_masculino.isChecked = false
                }

                this@FormularioClienteHelper.cliente = cliente

                campoTrue(false)

            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {

            }
        })
    }

    fun campoTrue(value: Boolean) {
        activity.campo_cliente_nome.isEnabled = value
        activity.campo_cliente_sobrenome.isEnabled = value
        activity.campo_cliente_cpf.isEnabled = value
        activity.campo_cliente_telefone.isEnabled = value
        activity.campo_cliente_dataNascimento.isEnabled = value
        activity.campo_cliente_masculino.isEnabled = value
        activity.campo_cliente_feminino.isEnabled = value
    }
}
