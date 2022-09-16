package br.senai.sp.jandira.imc20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.senai.sp.jandira.imc20.databinding.ActivityCalculateBinding
import br.senai.sp.jandira.imc20.utils.getBmi
import br.senai.sp.jandira.imc20.utils.getStatusBmi

class CalculateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCalculateBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar!!.hide()

        loadProfile()

        binding.buttonCalculate.setOnClickListener {
            bmiCalculate()
        }
    }

    private fun loadProfile() {
        // Abrir o arquivo SharedPreferences
        val data = getSharedPreferences("dados", MODE_PRIVATE)

        binding.textViewUsername.text = data.getString("name", "")
        binding.textViewEmail.text = data.getString("email", "")
        binding.textViewWeight.text = "${resources.getText(R.string.weight)} ${data.getInt("weight", 0)} Kg"
        binding.textViewHeight.text = "${resources.getText(R.string.height)} ${data.getFloat("height", 0.0f)} M"
    }

    private fun bmiCalculate() {
        val openResult = Intent(this, ResultActivity:: class.java)
        val data = getSharedPreferences("dados", MODE_PRIVATE)
        val editor = data.edit()
        var height = 0.0f

        if(validateData()) {
            if(binding.editTextHeight.text.isEmpty()) {
                height = data.getFloat("height", 0.0f)
            } else {
                height = binding.editTextHeight.text.toString().toFloat()
            }
            var weight = binding.editTextWeight.text.toString().toInt()

            editor.putInt("weight", weight)
            editor.putFloat("height", height)
            editor.commit()

            var bmi = getBmi(weight, height)
            var bmiStatus = getStatusBmi(bmi, this)

            // Enviar dados de uma Activity para outra
            openResult.putExtra("bmi", bmi)
            openResult.putExtra("bmiStatus", bmiStatus)

            startActivity(openResult)
        }


    }

    private fun validateData(): Boolean {
        if(binding.editTextWeight.text.toString().isEmpty()) {
            binding.editTextWeight.error = "Weight is required"
            return false
        } else {
            return true
        }
    }
}