package br.com.renanjardel.vet_app_kotlin.helper

import android.widget.EditText
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.activity.form.FormEspecieActivity
import br.com.renanjardel.vet_app_kotlin.model.Especie
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormularioEspecieHelper(activity: FormEspecieActivity) {

    private val campoNome: EditText
    private var especie: Especie? = null


    init {
        campoNome = activity.findViewById(R.id.campo_especie_nome)
        especie = Especie()
    }

    fun pegaEspecie(): Especie? {
        especie!!.nome = campoNome.text.toString()

        return especie
    }

    fun preencheFormulario(especie: Especie?) {
        val codigo = especie?.codigo

        val retrofit = RetrofitInicializador().especieService.buscarPorId(codigo!!)

        retrofit.enqueue(object : Callback<Especie> {
            override fun onResponse(call: Call<Especie>, response: Response<Especie>) {

                val especie = response.body()

                campoNome.setText(especie!!.nome)

                this@FormularioEspecieHelper.especie = especie

                campoTrue(false)

            }

            override fun onFailure(call: Call<Especie>, t: Throwable) {

            }
        })
    }

    fun campoTrue(value: Boolean) {
        campoNome.isEnabled = value
    }
}
