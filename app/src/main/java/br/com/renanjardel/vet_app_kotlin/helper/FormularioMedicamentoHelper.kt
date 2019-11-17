package br.com.renanjardel.vet_app_kotlin.helper

import android.widget.EditText
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.activity.form.FormMedicamentoActivity
import br.com.renanjardel.vet_app_kotlin.model.Medicamento
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormularioMedicamentoHelper(activity: FormMedicamentoActivity) {

    private val campoNome: EditText
    private var medicamento: Medicamento? = null

    init {
        campoNome = activity.findViewById(R.id.campo_medicamento_nome)
        medicamento = Medicamento()
    }

    fun pegaMedicamento(): Medicamento {
        medicamento!!.nome = campoNome.text.toString()

        return medicamento!!
    }


    fun preencherFormulario(medicamento: Medicamento?) {
        val codigo = medicamento!!.codigo

        val retrofit = RetrofitInicializador().medicamentoService.buscarPorId(codigo!!)
        retrofit.enqueue(object : Callback<Medicamento> {
            override fun onResponse(call: Call<Medicamento>, response: Response<Medicamento>) {
                val medicamento = response.body()

                campoNome.setText(medicamento!!.nome)

                this@FormularioMedicamentoHelper.medicamento = medicamento

                campoTrue(false)

            }

            override fun onFailure(call: Call<Medicamento>, t: Throwable) {

            }
        })
    }

    fun campoTrue(value: Boolean) {
        campoNome.isEnabled = value
    }
}
