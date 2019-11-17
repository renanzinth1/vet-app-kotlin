package br.com.renanjardel.vet_app_kotlin.helper

import android.widget.EditText
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.activity.form.FormSubEspecieActivity
import br.com.renanjardel.vet_app_kotlin.model.SubEspecie
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormularioSubEspecieHelper(activity: FormSubEspecieActivity) {

    private val campoNome: EditText
    private var subEspecie: SubEspecie? = null

    init {
        campoNome = activity.findViewById(R.id.campo_subEspecie_nome)
        subEspecie = SubEspecie()
    }

    fun pegaSubEspecie(): SubEspecie {
        subEspecie!!.nome = campoNome.text.toString()

        return subEspecie!!
    }

    fun preencheFormulario(subEspecie: SubEspecie?) {
        val codigo = subEspecie!!.codigo

        val retrofit = RetrofitInicializador().subEspecieService.buscarPorId(codigo!!)

        retrofit.enqueue(object : Callback<SubEspecie> {
            override fun onResponse(call: Call<SubEspecie>, response: Response<SubEspecie>) {

                val subEspecie = response.body()

                campoNome.setText(subEspecie!!.nome)

                this@FormularioSubEspecieHelper.subEspecie = subEspecie

                campoTrue(false)

            }

            override fun onFailure(call: Call<SubEspecie>, t: Throwable) {

            }
        })
    }

    fun campoTrue(value: Boolean) {
        campoNome.isEnabled = value
    }
}
