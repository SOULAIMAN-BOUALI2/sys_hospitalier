package com.example.systemhospitalier

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.graphics.Color
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.systemhospitalier.databinding.ActivitySignupBinding
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private var selectedRole = "medecin"
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRoleSelection()
        setupSpinners()
        setupPasswordToggle()
        setupPasswordStrength()
        setupButtons()
    }

    private fun setupRoleSelection() {
        binding.roleMedecin.setOnClickListener {
            selectedRole = "medecin"
            binding.roleMedecin.setBackgroundResource(R.drawable.role_selected)
            binding.roleInfirmier.setBackgroundResource(R.drawable.role_unselected)
            binding.layoutMedecin.visibility = View.VISIBLE
            binding.layoutInfirmier.visibility = View.GONE
        }

        binding.roleInfirmier.setOnClickListener {
            selectedRole = "infirmier"
            binding.roleInfirmier.setBackgroundResource(R.drawable.role_selected)
            binding.roleMedecin.setBackgroundResource(R.drawable.role_unselected)
            binding.layoutMedecin.visibility = View.GONE
            binding.layoutInfirmier.visibility = View.VISIBLE
        }
    }

    private fun setupSpinners() {
        val specialites = listOf("Urgentiste", "Cardiologue", "Chirurgien", "Pédiatre", "Radiologue", "Autre")
        binding.spinnerSpecialite.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, specialites)

        val services = listOf("Urgences", "Réanimation", "Pédiatrie", "Chirurgie", "Cardiologie")
        binding.spinnerService.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, services)

        val shifts = listOf("Matin (07h-15h)", "Après-midi (15h-23h)", "Nuit (23h-07h)")
        binding.spinnerShift.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, shifts)
    }

    private fun setupPasswordToggle() {
        binding.tvShowPassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                binding.etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                binding.tvShowPassword.text = "Cacher"
            } else {
                binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                binding.tvShowPassword.text = "Voir"
            }
            binding.etPassword.setSelection(binding.etPassword.text.length)
        }
    }

    private fun setupPasswordStrength() {
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val pass = s.toString()
                val strength = when {
                    pass.length >= 12 && pass.any { it.isDigit() } && pass.any { !it.isLetterOrDigit() } -> 4
                    pass.length >= 10 && pass.any { it.isDigit() } -> 3
                    pass.length >= 8 -> 2
                    pass.isNotEmpty() -> 1
                    else -> 0
                }

                val green  = Color.parseColor("#4CAF50")
                val orange = Color.parseColor("#FF9800")
                val red    = Color.parseColor("#E5383B")
                val gray   = Color.parseColor("#E5E7EB")

                binding.strength1.setBackgroundColor(if (strength >= 1) red    else gray)
                binding.strength2.setBackgroundColor(if (strength >= 2) orange else gray)
                binding.strength3.setBackgroundColor(if (strength >= 3) green  else gray)
                binding.strength4.setBackgroundColor(if (strength >= 4) green  else gray)

                val (label, color) = when (strength) {
                    1    -> Pair("Faible",    red)
                    2    -> Pair("Moyen",     orange)
                    3    -> Pair("Fort",      green)
                    4    -> Pair("Très fort", green)
                    else -> Pair("",          gray)
                }
                binding.tvStrengthLabel.text = label
                binding.tvStrengthLabel.setTextColor(color)
            }
        })
    }

    private fun setupButtons() {
        binding.tvBack.setOnClickListener { finish() }

        binding.tvGoToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.btnSignup.setOnClickListener {
            val nom     = binding.etNom.text.toString().trim()
            val prenom  = binding.etPrenom.text.toString().trim()
            val email   = binding.etEmail.text.toString().trim()
            val pass    = binding.etPassword.text.toString()
            val confirm = binding.etConfirmPassword.text.toString()

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pass != confirm) {
                Toast.makeText(this, "Les mots de passe ne correspondent pas", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pass.length < 8) {
                Toast.makeText(this, "Mot de passe trop court (minimum 8 caractères)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnSignup.isEnabled = false

            lifecycleScope.launch {
                val success = if (selectedRole == "medecin") {
                    val matricule = binding.etMatriculeMedecin.text.toString().trim()
                    if (matricule.isEmpty()) {
                        Toast.makeText(this@SignupActivity, "Veuillez entrer votre matricule", Toast.LENGTH_SHORT).show()
                        binding.btnSignup.isEnabled = true
                        return@launch
                    }
                    AuthRepository.signupMedecin(
                        nom = nom,
                        prenom = prenom,
                        email = email,
                        password = pass,
                        matricule = matricule,
                        specialite = binding.spinnerSpecialite.selectedItem.toString()
                    )
                } else {
                    val matricule = binding.etMatriculeInfirmier.text.toString().trim()
                    if (matricule.isEmpty()) {
                        Toast.makeText(this@SignupActivity, "Veuillez entrer votre matricule", Toast.LENGTH_SHORT).show()
                        binding.btnSignup.isEnabled = true
                        return@launch
                    }
                    AuthRepository.signupInfirmier(
                        nom = nom,
                        prenom = prenom,
                        email = email,
                        password = pass,
                        matricule = matricule,
                        service = binding.spinnerService.selectedItem.toString(),
                        shift = binding.spinnerShift.selectedItem.toString()
                    )
                }

                if (success) {
                    Toast.makeText(this@SignupActivity, "Compte créé avec succès !", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@SignupActivity, "Erreur. Email ou matricule déjà utilisé ?", Toast.LENGTH_LONG).show()
                    binding.btnSignup.isEnabled = true
                }
            }
        }
    }
}