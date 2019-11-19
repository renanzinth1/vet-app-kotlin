package br.com.renanjardel.vet_app_kotlin.helper

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.activity.form.FormAnimalActivity
import br.com.renanjardel.vet_app_kotlin.model.Animal
import br.com.renanjardel.vet_app_kotlin.model.Especie
import br.com.renanjardel.vet_app_kotlin.model.SexoAnimal
import br.com.renanjardel.vet_app_kotlin.model.SubEspecie
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import kotlinx.android.synthetic.main.activity_form_animal.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormularioAnimalHelper(private val activity: FormAnimalActivity) {

    private lateinit var especies: List<Especie>
    private lateinit var subespecies: List<SubEspecie>
    private var animal = Animal()

    fun pegaAnimal(): Animal? {
        animal.nome = activity.campo_animal_nome.text.toString()
        animal.dataNascimento = activity.campo_animal_dataNascimento.text.toString()

        val checkedRadioButtonId = activity.radioGroup_sexos.checkedRadioButtonId

        when (checkedRadioButtonId) {
            R.id.campo_animal_macho -> animal.sexo = SexoAnimal.MACHO

            R.id.campo_animal_femea -> animal.sexo = SexoAnimal.FEMEA
        }

        animal.subEspecie = activity.spinner_subEspecies.selectedItem as SubEspecie

        return animal
    }

    fun campoTrue(value: Boolean) {
        activity.campo_animal_nome.isEnabled = value
        activity.campo_animal_dataNascimento.isEnabled = value
        activity.campo_animal_macho.isEnabled = value
        activity.campo_animal_femea.isEnabled = value
        activity.spinner_especies.isEnabled = value
        activity.spinner_subEspecies.isEnabled = value
    }

    fun carregaSpinnerEspecieESubEspecie() {

        val especieCall = RetrofitInicializador().especieService.listar()

        especieCall.enqueue(object : Callback<List<Especie>> {
            override fun onResponse(call: Call<List<Especie>>, response: Response<List<Especie>>) {

                this@FormularioAnimalHelper.especies = response.body()!!

                val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, especies)
                activity.spinner_especies.adapter = adapter

                activity.spinner_especies.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                        val especie = parent.getItemAtPosition(position) as Especie

                        val subEspecieCall = RetrofitInicializador().especieService.listarSubEspecies(especie.codigo)

                        subEspecieCall.enqueue(object : Callback<List<SubEspecie>> {
                            override fun onResponse(call: Call<List<SubEspecie>>, response: Response<List<SubEspecie>>) {

                                this@FormularioAnimalHelper.subespecies = response.body()!!

                                val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, subespecies)
                                activity.spinner_subEspecies.adapter = adapter

                                if (animal.codigo != null) {
                                    activity.spinner_subEspecies.setSelection(pegarPosicaoSubEspecie(animal.subEspecie))
                                }

                            }

                            override fun onFailure(call: Call<List<SubEspecie>>, t: Throwable) {

                            }
                        })
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {

                    }
                }
            }

            override fun onFailure(call: Call<List<Especie>>, t: Throwable) {

            }
        })
    }

    fun pegarPosicaoEspecie(especie: Especie?): Int {

        var i = 0
        while (i <= especies.size - 1) {
            if (especies[i] == especie) {
                return i
            }
            i++
        }
        return 0
    }

    fun pegarPosicaoSubEspecie(subEspecie: SubEspecie?): Int {

        var i = 0
        while (i <= subespecies.size - 1) {
            if (subespecies[i] == subEspecie) {
                return i
            }
            i++
        }
        return 0
    }

    fun preencherFomulario(animal: Animal?) {
        val codigo = animal!!.codigo

        val retrofit = RetrofitInicializador().animalService.buscarPorId(codigo!!)

        retrofit.enqueue(object : Callback<Animal> {
            override fun onResponse(call: Call<Animal>, response: Response<Animal>) {

                val animal = response.body()

                activity.campo_animal_nome.setText(animal!!.nome)
                activity.campo_animal_dataNascimento.setText(animal.dataNascimento)

                val sexo = animal.sexo

                if (sexo == SexoAnimal.MACHO) {
                    activity.campo_animal_macho.isChecked = true
                    activity.campo_animal_femea.isChecked = false
                } else {
                    activity.campo_animal_femea.isChecked = true
                    activity.campo_animal_macho.isChecked = false
                }

                activity.spinner_especies.setSelection(pegarPosicaoEspecie(animal.subEspecie.especie))

                //Aqui estava dando bug, então mudei para dentro do carregaSpinnerEspecieESubEspecie()
                //activity.spinner_subEspecies.setSelection(pegarPosicaoSubEspecie(animal.getSubEspecie()));

                this@FormularioAnimalHelper.animal = animal

                campoTrue(false)
            }

            override fun onFailure(call: Call<Animal>, t: Throwable) {

            }
        })
    }
}
