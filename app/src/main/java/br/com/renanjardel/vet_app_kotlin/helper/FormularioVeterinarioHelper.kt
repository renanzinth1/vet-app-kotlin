package br.com.renanjardel.vet_app_kotlin.helper

import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.activity.form.FormVeterinarioActivity
import br.com.renanjardel.vet_app_kotlin.model.Sexo
import br.com.renanjardel.vet_app_kotlin.model.Veterinario
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import kotlinx.android.synthetic.main.activity_form_veterinario.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormularioVeterinarioHelper(private val activity: FormVeterinarioActivity) {

    private var veterinario = Veterinario()

    fun pegaVeterinario(): Veterinario {

        veterinario.nome = activity.campo_veterinario_nome.text.toString()
        veterinario.sobrenome = activity.campo_veterinario_sobrenome.text.toString()
        veterinario.cfmv = activity.campo_veterinario_cfmv.text.toString()
        veterinario.telefone = activity.campo_veterinario_telefone.text.toString()

        veterinario.dataNascimento = activity.campo_veterinario_dataNascimento.text.toString()

        //Pegando qual radiobutton foi selecinada
        val selectecId = activity.radioGroup_sexos.checkedRadioButtonId

        when (selectecId) {
            R.id.campo_veterinario_masculino -> veterinario.sexo = Sexo.MASCULINO

            R.id.campo_veterinario_feminino -> veterinario.sexo = Sexo.FEMININO
        }

        return veterinario
    }

    fun preencheFormulario(veterinario: Veterinario?) {
        val codigo = veterinario!!.codigo

        val retrofit = RetrofitInicializador().veterinarioService.buscarPorId(codigo!!)

        retrofit.enqueue(object : Callback<Veterinario> {
            override fun onResponse(call: Call<Veterinario>, response: Response<Veterinario>) {

                val veterinario = response.body()

                activity.campo_veterinario_nome.setText(veterinario!!.nome)
                activity.campo_veterinario_sobrenome.setText(veterinario.sobrenome)
                activity.campo_veterinario_cfmv.setText(veterinario.cfmv)
                activity.campo_veterinario_telefone.setText(veterinario.telefone)
                activity.campo_veterinario_dataNascimento.setText(veterinario.dataNascimento)

                val sexo = veterinario.sexo

                if (sexo == Sexo.MASCULINO) {
                    activity.campo_veterinario_masculino.isChecked = true
                    activity.campo_veterinario_feminino.isChecked = false

                } else {
                    activity.campo_veterinario_feminino.isChecked = true
                    activity.campo_veterinario_masculino.isChecked = false
                }

                this@FormularioVeterinarioHelper.veterinario = veterinario
                campoTrue(false)

            }

            override fun onFailure(call: Call<Veterinario>, t: Throwable) {

            }
        })
    }

    fun campoTrue(value: Boolean) {
        activity.campo_veterinario_nome.isEnabled = value
        activity.campo_veterinario_sobrenome.isEnabled = value
        activity.campo_veterinario_cfmv.isEnabled = value
        activity.campo_veterinario_telefone.isEnabled = value
        activity.campo_veterinario_dataNascimento.isEnabled = value
        activity.campo_veterinario_masculino.isEnabled = value
        activity.campo_veterinario_feminino.isEnabled = value
    }

}
