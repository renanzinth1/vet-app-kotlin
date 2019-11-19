package br.com.renanjardel.vet_app_kotlin.helper

import br.com.renanjardel.vet_app_kotlin.activity.form.FormMedicamentoActivity
import br.com.renanjardel.vet_app_kotlin.model.Medicamento
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import kotlinx.android.synthetic.main.activity_form_medicamento.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormularioMedicamentoHelper(private val activity: FormMedicamentoActivity) {

    private var medicamento = Medicamento()

    fun pegaMedicamento(): Medicamento {
        medicamento.nome = activity.campo_medicamento_nome.text.toString()

        return medicamento
    }


    fun preencherFormulario(medicamento: Medicamento?) {
        val codigo = medicamento!!.codigo

        val retrofit = RetrofitInicializador().medicamentoService.buscarPorId(codigo!!)
        retrofit.enqueue(object : Callback<Medicamento> {
            override fun onResponse(call: Call<Medicamento>, response: Response<Medicamento>) {
                val medicamento = response.body()

                activity.campo_medicamento_nome.setText(medicamento!!.nome)

                this@FormularioMedicamentoHelper.medicamento = medicamento

                campoTrue(false)

            }

            override fun onFailure(call: Call<Medicamento>, t: Throwable) {

            }
        })
    }

    fun campoTrue(value: Boolean) {
        activity.campo_medicamento_nome.isEnabled = value
    }
}
