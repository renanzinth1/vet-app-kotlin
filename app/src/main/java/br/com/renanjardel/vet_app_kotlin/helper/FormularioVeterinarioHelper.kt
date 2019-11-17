package br.com.renanjardel.vet_app_kotlin.helper

import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.activity.form.FormVeterinarioActivity
import br.com.renanjardel.vet_app_kotlin.model.Sexo
import br.com.renanjardel.vet_app_kotlin.model.Veterinario
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormularioVeterinarioHelper(activity: FormVeterinarioActivity) {

    private val campoNome: EditText
    private val campoSobrenome: EditText
    private val campoCfmv: EditText
    private val campoTelefone: EditText
    private val campodataNascimento: EditText
    private val sexos: RadioGroup
    private val campoMasculino: RadioButton
    private val campoFeminino: RadioButton
    private var veterinario: Veterinario? = null

    init {
        campoNome = activity.findViewById(R.id.campo_veterinario_nome)
        campoSobrenome = activity.findViewById(R.id.campo_veterinario_sobrenome)
        campoCfmv = activity.findViewById(R.id.campo_veterinario_cfmv)
        campoTelefone = activity.findViewById(R.id.campo_veterinario_telefone)
        campodataNascimento = activity.findViewById(R.id.campo_veterinario_dataNascimento)
        sexos = activity.findViewById(R.id.radioGroup_sexos)
        campoMasculino = activity.findViewById(R.id.campo_veterinario_masculino)
        campoFeminino = activity.findViewById(R.id.campo_veterinario_feminino)
        veterinario = Veterinario()
    }

    fun pegaVeterinario(): Veterinario {

        veterinario!!.nome = campoNome.text.toString()
        veterinario!!.sobrenome = campoSobrenome.text.toString()
        veterinario!!.cfmv = campoCfmv.text.toString()
        veterinario!!.telefone = campoTelefone.text.toString()

        veterinario!!.dataNascimento = campodataNascimento.text.toString()

        //Pegando qual radiobutton foi selecinada
        val selectecId = sexos.checkedRadioButtonId

        when (selectecId) {
            R.id.campo_veterinario_masculino -> veterinario!!.sexo = Sexo.MASCULINO

            R.id.campo_veterinario_feminino -> veterinario!!.sexo = Sexo.FEMININO
        }

        return veterinario!!
    }

    fun preencheFormulario(veterinario: Veterinario?) {
        val codigo = veterinario!!.codigo

        val retrofit = RetrofitInicializador().veterinarioService.buscarPorId(codigo!!)

        retrofit.enqueue(object : Callback<Veterinario> {
            override fun onResponse(call: Call<Veterinario>, response: Response<Veterinario>) {

                val veterinario = response.body()

                campoNome.setText(veterinario!!.nome)
                campoSobrenome.setText(veterinario.sobrenome)
                campoCfmv.setText(veterinario.cfmv)
                campoTelefone.setText(veterinario.telefone)
                campodataNascimento.setText(veterinario.dataNascimento)

                val sexo = veterinario.sexo

                if (sexo == Sexo.MASCULINO) {
                    campoMasculino.isChecked = true
                    campoFeminino.isChecked = false

                } else {
                    campoFeminino.isChecked = true
                    campoMasculino.isChecked = false
                }

                this@FormularioVeterinarioHelper.veterinario = veterinario
                campoTrue(false)

            }

            override fun onFailure(call: Call<Veterinario>, t: Throwable) {

            }
        })
    }

    fun campoTrue(value: Boolean) {
        campoNome.isEnabled = value
        campoSobrenome.isEnabled = value
        campoCfmv.isEnabled = value
        campoTelefone.isEnabled = value
        campodataNascimento.isEnabled = value
        campoMasculino.isEnabled = value
        campoFeminino.isEnabled = value
    }

}
