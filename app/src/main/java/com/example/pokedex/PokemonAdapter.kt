package com.example.pokedex

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.pokedex.models.Pokemon
import kotlinx.android.synthetic.main.list_element_pokemon.view.*

class PokemonAdapter(val items: List<Pokemon>, val pokeListener : (View,Int,Int) -> Unit) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    // TODO: Para contar elementos creados


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_element_pokemon, parent, false)


        /*
         * TODO: Muestra el valor de contador de view creadas solo se hace aqui, para asegurar
         * que solo se asigne el valor aqui
         */
        return ViewHolder(view).onClick(pokeListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Pokemon) = with(itemView) {
            count_element.text = item.number
            tv_pokemon_name.text = item.name
            ll_container.tag = item.id
            count_element.tag = item.id
            tv_pokemon_name.tag = item.id
        }
    }

    fun <T : RecyclerView.ViewHolder> T.onClick(event: (view: View, position: Int, type: Int) -> Unit): T {
        itemView.setOnClickListener {
            event.invoke(it, getAdapterPosition(), getItemViewType())
        }
        return this
    }

}