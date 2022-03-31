package com.engdacomp.dka

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.engdacomp.dka.databinding.ActivityMainBinding
import com.engdacomp.dka.model.Resenha
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val firestore = Firebase.firestore
    private var resenhaId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        resenhaId = intent.getStringExtra("RESENHA_ID")
        buscarResenha()
        cadastrarResenha()

        binding.listarBtn.setOnClickListener{
            var intent = Intent(applicationContext, ListaActivity::class.java)
            startActivity(intent)
        }

    }

    private fun cadastrarResenha(){
        binding.salvarBtn.setOnClickListener {
            Thread(Runnable {
                val titulo =  binding.tituloEt.text.toString()
                val texto = binding.textoEt.text.toString()

                val resenhaDocumento = ResenhaDocumento(titulo = titulo, texto = texto)
                val colecao = firestore.collection("resenhas")
                val documento = resenhaId?.let {
                    colecao.document(it)
                } ?: colecao.document()
                documento.set(resenhaDocumento)

                Log.i("Salvar", "Resenha salva: ${documento.id}")

                runOnUiThread(Runnable {
                    val editavel: String = ""
                    binding.tituloEt.text = createEditable(editavel)
                    binding.textoEt.text = createEditable(editavel)
                })
            }).start()
            Toast.makeText(this, "Resenha salva.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun buscarResenha() {
        resenhaId = intent.getStringExtra("RESENHA_ID")

        firestore.collection("resenhas")
            .document(resenhaId.toString())
            .addSnapshotListener { s, _ ->
                s?.let { document ->
                    document.toObject<ResenhaDocumento>()
                        ?.paraResenha(document.id)
                        ?.let { resenha ->
                            with(binding) {
                                title = "Editar"
                                tituloEt.setText(resenha.titulo)
                                textoEt.setText(resenha.texto)
                            }
                        }

                }
            }
    }

    private fun createEditable(editavel: String) =
        Editable.Factory.getInstance().newEditable(editavel)

    class ResenhaDocumento(

        val titulo: String = "",
        val texto: String = ""
    ) {
        fun paraResenha(id: String): Resenha {
            return Resenha(id = id, titulo = titulo, texto = texto)
        }
    }
}