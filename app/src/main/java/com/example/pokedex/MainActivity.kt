package com.example.pokedex

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.example.pokedex.models.Pokemon
import com.example.pokedex.utilities.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_element_pokemon.*
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var mPokemonNumber: EditText? = null
    private var mSearchButton: Button? = null
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var pokemonRes: MutableList<Pokemon>
    private lateinit var linearRecycle: LinearLayout
    //internal var mResultText: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindView()

        mSearchButton?.setOnClickListener { view ->
            val pokemonNumber = mPokemonNumber!!.text.toString().toLowerCase().trim { it <= ' ' }
            if (pokemonNumber.isEmpty()) {
                pokemonRes = MutableList(1) {i ->
                    Pokemon("Empty","Check the information provided", "No data found")
                }

                initRecycler()
            } else {
                FetchPokemonTask().execute(pokemonNumber)

            }
        }
    }

    internal fun bindView() {
        mPokemonNumber = findViewById(R.id.et_pokemon_type)
        mSearchButton = findViewById(R.id.bt_search_pokemon)
        //mResultText = findViewById(R.id.tv_result)
    }


    private inner class FetchPokemonTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg pokemonNumbers: String): String? {

            if (pokemonNumbers.isEmpty()) {
                return null
            }

            val ID = pokemonNumbers[0]

            val pokeAPI = NetworkUtils.buildUrl(ID, "type")

            try {
                return NetworkUtils.getResponseFromHttpUrl(pokeAPI!!)
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }

        }

        override fun onPostExecute(pokemonInfo: String?) {
            if (pokemonInfo != null && pokemonInfo != "") {
                val resultados = JSONObject(pokemonInfo)
                val pokemones = resultados.getJSONArray("pokemon")
                //var textoRes = ""

                pokemonRes = MutableList(pokemones.length()) {i ->
                    Pokemon(pokemones.getJSONObject(i).getJSONObject("pokemon").getString("url").split("/")[6],"Pokémon: " + pokemones.getJSONObject(i).getJSONObject("pokemon").getString("name").capitalize(), "Pokédex #"+pokemones.getJSONObject(i).getJSONObject("pokemon").getString("url").split("/")[6])
                }

                initRecycler()

                //mResultText?.text = textoRes
            } else {
                pokemonRes = MutableList(1) {i ->
                    Pokemon("Empty","Check the information provided", "No data found")
                }

                initRecycler()
            }
        }
    }

    fun initRecycler() {

        viewManager = LinearLayoutManager(this)
        viewAdapter = PokemonAdapter(pokemonRes,mostrarId)

        rv_pokemon_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    val mostrarId : (View,Int,Int) -> Unit = {v,pos,type ->
            if (v.tag != "Empty") {
                val mIntent = Intent(this@MainActivity, DataActivity::class.java)
                mIntent.putExtra("pokeId", v.tag.toString())
                startActivity(mIntent)
            }
    }

}
