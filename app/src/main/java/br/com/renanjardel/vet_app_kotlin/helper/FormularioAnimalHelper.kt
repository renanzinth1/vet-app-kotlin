package br.com.renanjardel.vet_app_kotlin.helper

import android.view.View
import android.widget.*
import br.com.renanjardel.vet_app_kotlin.R
import br.com.renanjardel.vet_app_kotlin.activity.form.FormAnimalActivity
import br.com.renanjardel.vet_app_kotlin.model.Animal
import br.com.renanjardel.vet_app_kotlin.model.Especie
import br.com.renanjardel.vet_app_kotlin.model.SexoAnimal
import br.com.renanjardel.vet_app_kotlin.model.SubEspecie
import br.com.renanjardel.vet_app_kotlin.retrofit.RetrofitInicializador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormularioAnimalHelper(private val activity: FormAnimalActivity) {

    private val campoNome: EditText
    private val campoDataNascimento: EditText
    private val sexos: RadioGroup
    private val campoMacho: RadioButton
    private val campoFemea: RadioButton
    private val spinnerEspecie: Spinner
    private val spinnerSubEspecie: Spinner

    private var especies: List<Especie>? = null
    private var subespecies: List<SubEspecie>? = null
    private var animal: Animal? = null

    init {
        campoNome = activity.findViewById(R.id.campo_animal_nome)
        campoDataNascimento = activity.findViewById(R.id.campo_animal_dataNascimento)
        sexos = activity.findViewById(R.id.radioGroup_sexos)
        campoMacho = activity.findViewById(R.id.campo_animal_macho)
        campoFemea = activity.findViewById(R.id.campo_animal_femea)
        spinnerEspecie = activity.findViewById(R.id.spinner_especies)
        spinnerSubEspecie = activity.findViewById(R.id.spinner_subEspecies)

        animal = Animal()
    }


    fun pegaAnimal(): Animal? {
        animal!!.nome = campoNome.text.toString()
        animal!!.dataNascimento = campoDataNascimento.text.toString()

        val checkedRadioButtonId = sexos.checkedRadioButtonId

        when (checkedRadioButtonId) {
            R.id.campo_animal_macho -> animal!!.sexo = SexoAnimal.MACHO

            R.id.campo_animal_femea -> animal!!.sexo = SexoAnimal.FEMEA
        }

        animal!!.subEspecie = spinnerSubEspecie.selectedItem as SubEspecie

        return animal
    }

    fun campoTrue(value: Boolean) {
        campoNome.isEnabled = value
        campoDataNascimento.isEnabled = value
        campoMacho.isEnabled = value
        campoFemea.isEnabled = value
        spinnerEspecie.isEnabled = value
        spinnerSubEspecie.isEnabled = value
    }

    fun carregaSpinnerEspecieESubEspecie() {

        val especieCall = RetrofitInicializador().especieService.listar()

        especieCall.enqueue(object : Callback<List<Especie>> {
            override fun onResponse(call: Call<List<Especie>>, response: Response<List<Especie>>) {

                this@FormularioAnimalHelper.especies = response.body()

                val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, especies!!)
                spinnerEspecie.adapter = adapter

                spinnerEspecie.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                        val especie = parent.getItemAtPosition(position) as Especie

                        val subEspecieCall = RetrofitInicializador().especieService.listarSubEspecies(especie.codigo)

                        subEspecieCall.enqueue(object : Callback<List<SubEspecie>> {
                            override fun onResponse(call: Call<List<SubEspecie>>, response: Response<List<SubEspecie>>) {

                                this@FormularioAnimalHelper.subespecies = response.body()

                                val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, subespecies!!)
                                spinnerSubEspecie.adapter = adapter

                                if (animal!!.codigo != null) {
                                    spinnerSubEspecie.setSelection(pegarPosicaoSubEspecie(animal!!.subEspecie))
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
        while (i <= especies!!.size - 1) {
            if (especies!![i] == especie) {
                return i
            }
            i++
        }
        return 0
    }

    fun pegarPosicaoSubEspecie(subEspecie: SubEspecie?): Int {

        var i = 0
        while (i <= subespecies!!.size - 1) {
            if (subespecies!![i] == subEspecie) {
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

                campoNome.setText(animal!!.nome)
                campoDataNascimento.setText(animal.dataNascimento)

                val sexo = animal.sexo

                if (sexo == SexoAnimal.MACHO) {
                    campoMacho.isChecked = true
                    campoFemea.isChecked = false
                } else {
                    campoFemea.isChecked = true
                    campoMacho.isChecked = false
                }

                spinnerEspecie.setSelection(pegarPosicaoEspecie(animal.subEspecie!!.especie))

                //Aqui estava dando bug, então mudei para dentro do carregaSpinnerEspecieESubEspecie()
                //spinnerSubEspecie.setSelection(pegarPosicaoSubEspecie(animal.getSubEspecie()));

                this@FormularioAnimalHelper.animal = animal

                campoTrue(false)
            }

            override fun onFailure(call: Call<Animal>, t: Throwable) {

            }
        })
    }
}
