package com.engdacomp.dka

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.engdacomp.dka.adapter.ResenhaAdapter
import com.engdacomp.dka.databinding.ActivityListaBinding
import com.engdacomp.dka.model.Resenha
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.*
import com.google.firebase.ktx.Firebase
import java.util.*

class ListaActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityListaBinding.inflate(layoutInflater)
    }
    private val firestore = Firebase.firestore
    private val adapter = ResenhaAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        rv()
        buscarTodos()
    }

    private fun buscarTodos() {
        firestore.collection("resenhas")
            .addSnapshotListener { s, _ ->
                s?.let { snapshot ->
                    val resenhas = mutableListOf<Resenha>()

                    for (documento in snapshot.documents) {
                        Log.i("Listagem", "Doc find tempo real ${documento.data}")
                        val resenhaDocumento = documento.toObject<ResenhaDocumento>()
                        resenhaDocumento?.let { resenhaDocumentoNaoNulo ->
                            resenhas.add(resenhaDocumentoNaoNulo.paraResenha(documento.id))
                        }
                    }
                    adapter.atualiza(resenhas)
                }
            }
    }

    private fun rv() {
        val rv = binding.recyclerView
        rv.layoutManager = LinearLayoutManager(applicationContext)
        rv.itemAnimator = DefaultItemAnimator()
        rv.adapter = adapter
        adapter.ClicaNoItem = {
            Toast.makeText(this, "Clique", Toast.LENGTH_SHORT).show()
            /*val i = Intent(
                this,
                DetalheResenhaActivity::class.java
            ).apply {
                putExtra("PRODUTO_ID", it.id)
            }


            startActivity(i)*/
        }
    }

    class ResenhaDocumento(
        val titulo: String = "",
        val texto: String = ""
    ) {
        fun paraResenha(id: String): Resenha {
            return Resenha(id = id, titulo = titulo, texto = texto)
        }
    }

}

