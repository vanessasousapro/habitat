package br.com.habitat

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.habitat.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private var isLoginMode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnAcao.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val senha = binding.etSenha.text.toString().trim()

            if (!validarCampos(email, senha)) return@setOnClickListener

            if (isLoginMode) {
                fazerLogin(email, senha)
            } else {
                fazerCadastro(email, senha)
            }
        }

        binding.tvAlternar.setOnClickListener {
            isLoginMode = !isLoginMode
            atualizarModo()
        }
    }

    private fun validarCampos(email: String, senha: String): Boolean {
        if (email.isEmpty()) {
            binding.tilEmail.error = "Informe seu e-mail"
            return false
        }
        binding.tilEmail.error = null

        if (senha.isEmpty()) {
            binding.tilSenha.error = "Informe sua senha"
            return false
        }
        if (senha.length < 6) {
            binding.tilSenha.error = "A senha deve ter pelo menos 6 caracteres"
            return false
        }
        binding.tilSenha.error = null

        return true
    }

    private fun fazerLogin(email: String, senha: String) {
        binding.btnAcao.isEnabled = false
        auth.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                Toast.makeText(this, "Bem-vinda! 🌱", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .addOnFailureListener {
                binding.btnAcao.isEnabled = true
                Toast.makeText(this, "E-mail ou senha incorretos.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fazerCadastro(email: String, senha: String) {
        binding.btnAcao.isEnabled = false
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                Toast.makeText(this, "Conta criada com sucesso! 🌱", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .addOnFailureListener {
                binding.btnAcao.isEnabled = true
                Toast.makeText(this, "Erro ao criar conta. Tente novamente.", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun atualizarModo() {
        if (isLoginMode) {
            binding.tvFormTitle.text = "Entrar"
            binding.btnAcao.text = "Entrar"
            binding.tvAlternar.text = "Não tem conta?"
        } else {
            binding.tvFormTitle.text = "Cadastrar"
            binding.btnAcao.text = "Cadastrar"
            binding.tvAlternar.text = "Já tem conta?"
        }
    }
}