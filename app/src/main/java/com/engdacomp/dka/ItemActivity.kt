package com.engdacomp.dka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.engdacomp.dka.databinding.ActivityItemBinding
import com.engdacomp.dka.databinding.ActivityLoginBinding
import com.engdacomp.dka.model.Resenha
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemBinding
    private var resenha: Resenha? = null
    private val firestore = Firebase.firestore
    private var resenhaId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        resenhaId = intent.getStringExtra("RESENHA_ID")

        editarResenha()
        deletarResenha()
    }

    private fun deletarResenha() {
        val btDeletar = binding.deletarBt
        btDeletar.setOnClickListener {

            firestore.collection("resenhas")
                .document(resenhaId.toString())
                .delete()
            finish()
        }
    }

    private fun editarResenha() {
        val btEditar = binding.editarBt
        btEditar.setOnClickListener {
            val i = Intent(
                this,
                MainActivity::class.java
            ).apply {
                putExtra("RESENHA_ID", resenhaId)
            }
            i.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }
    }
}