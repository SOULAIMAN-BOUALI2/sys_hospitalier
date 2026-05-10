package com.example.systemhospitalier

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.systemhospitalier.databinding.ActivityLoginBinding
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPasswordToggle()

        binding.tvGoToSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val pass  = binding.etPassword.text.toString()

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.btnLogin.isEnabled = false

            lifecycleScope.launch {
                try {
                    val result = AuthRepository.login(email, pass)

                    if (result != null) {
                        val (role, userId) = result
                        Toast.makeText(this@LoginActivity, "Bienvenue ! $role", Toast.LENGTH_SHORT).show()

                        val targetActivity = when (role) {
                            "medecin" -> DashboardMedecinActivity::class.java
                            "infirmier" -> DashboardInfirmierActivity::class.java
                            else -> DashboardAdminActivity::class.java
                        }

                        val intent = Intent(this@LoginActivity, targetActivity).apply {
                            putExtra("USER_ID", userId)
                            putExtra("USER_ROLE", role)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)

                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Email ou mot de passe incorrect",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.btnLogin.isEnabled = true
                    }

                } catch (e: Exception) {
                    android.util.Log.e("LOGIN_ERROR", "Erreur: ${e.message}", e)
                    Toast.makeText(
                        this@LoginActivity,
                        "Erreur réseau, réessayez",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.btnLogin.isEnabled = true
                }
            }
        }
    }

    private fun setupPasswordToggle() {
        binding.tvShowPassword.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            binding.etPassword.inputType = if (isPasswordVisible) {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            binding.tvShowPassword.text = if (isPasswordVisible) "Cacher" else "Voir"
            binding.etPassword.setSelection(binding.etPassword.text.length)
        }
    }
}