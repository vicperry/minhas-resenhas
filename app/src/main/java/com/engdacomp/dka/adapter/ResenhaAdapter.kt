package com.engdacomp.dka.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.engdacomp.dka.databinding.ListaItemBinding
import com.engdacomp.dka.model.Resenha

class ResenhaAdapter(
    private val context: Context,
    resenhas: List<Resenha> = emptyList(),
    var ClicaNoItem: (resenha: Resenha) -> Unit = {}
) : RecyclerView.Adapter<ResenhaAdapter.ViewHolder>() {
    private val resenhas = resenhas.toMutableList()

    inner class ViewHolder(private val binding: ListaItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var resenha: Resenha

        init {
            itemView.setOnClickListener {
                if (::resenha.isInitialized) {
                    ClicaNoItem(resenha)
                }
            }
        }

        fun vincula(resenha: Resenha) {
            this.resenha = resenha
            val titulo = binding.tituloTv
            titulo.text = resenha.titulo
            val texto = binding.textoTv
            texto.text = resenha.texto

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ListaItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resenha = resenhas[position]
        holder.vincula(resenha)
    }

    override fun getItemCount(): Int = resenhas.size

    fun atualiza(resenhas: List<Resenha>) {
        this.resenhas.clear()
        this.resenhas.addAll(resenhas)
        notifyDataSetChanged()
    }
}