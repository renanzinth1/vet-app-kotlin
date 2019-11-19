package br.com.renanjardel.vet_app_kotlin.helper

import br.com.renanjardel.vet_app_kotlin.activity.form.FormSubEspecieActivity
import br.com.renanjardel.vet_app_kotlin.model.SubEspecie
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import kotlinx.android.synthetic.main.activity_form_sub_especie.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormularioSubEspecieHelper(private val activity: FormSubEspecieActivity) {

    private var subEspecie = SubEspecie()


    fun pegaSubEspecie(): SubEspecie {
        subEspecie.nome = activity.campo_subEspecie_nome.text.toString()

        return subEspecie
    }

    fun preencheFormulario(subEspecie: SubEspecie?) {
        val codigo = subEspecie!!.codigo

        val retrofit = RetrofitInicializador().subEspecieService.buscarPorId(codigo!!)

        retrofit.enqueue(object : Callback<SubEspecie> {
            override fun onResponse(call: Call<SubEspecie>, response: Response<SubEspecie>) {

                val subEspecie = response.body()

                activity.campo_subEspecie_nome.setText(subEspecie!!.nome)

                this@FormularioSubEspecieHelper.subEspecie = subEspecie

                campoTrue(false)

            }

            override fun onFailure(call: Call<SubEspecie>, t: Throwable) {

            }
        })
    }

    fun campoTrue(value: Boolean) {
        activity.campo_subEspecie_nome.isEnabled = value
    }
}
