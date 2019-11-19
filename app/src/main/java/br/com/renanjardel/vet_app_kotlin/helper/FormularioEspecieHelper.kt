package br.com.renanjardel.vet_app_kotlin.helper

import br.com.renanjardel.vet_app_kotlin.activity.form.FormEspecieActivity
import br.com.renanjardel.vet_app_kotlin.model.Especie
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import kotlinx.android.synthetic.main.activity_form_especie.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormularioEspecieHelper(private val activity: FormEspecieActivity) {

    private var especie = Especie()

    fun pegaEspecie(): Especie? {
        especie.nome = activity.campo_especie_nome.text.toString()
        return especie
    }

    fun preencheFormulario(especie: Especie?) {
        val codigo = especie?.codigo

        val retrofit = RetrofitInicializador().especieService.buscarPorId(codigo!!)

        retrofit.enqueue(object : Callback<Especie> {
            override fun onResponse(call: Call<Especie>, response: Response<Especie>) {

                val especie = response.body()

                activity.campo_especie_nome.setText(especie!!.nome)

                this@FormularioEspecieHelper.especie = especie

                campoTrue(false)
            }

            override fun onFailure(call: Call<Especie>, t: Throwable) {

            }
        })
    }

    fun campoTrue(value: Boolean) {
        activity.campo_especie_nome.isEnabled = value
    }
}
