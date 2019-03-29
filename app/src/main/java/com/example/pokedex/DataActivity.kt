package com.example.pokedex

import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.pokedex.models.Pokemon
import com.example.pokedex.utilities.NetworkUtils
import kotlinx.android.synthetic.main.activity_data.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class DataActivity : AppCompatActivity() {

    private lateinit var result: TextView
    private lateinit var header: LinearLayout
    private lateinit var typesView: TextView
    private lateinit var numberView: TextView
    private lateinit var heightView: TextView
    private lateinit var weightView: TextView
    private lateinit var abilitiesView: TextView
    private lateinit var num: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)

        result = tv_name
        header = ll_header
        typesView = tv_types
        heightView = tv_height
        weightView = tv_weight
        numberView = tv_number
        abilitiesView = tv_abilities

        val mIntent = intent
        num = mIntent.getStringExtra("pokeId")
        FetchPokemonTask().execute(num)
    }

    private inner class FetchPokemonTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg pokemonNumbers: String): String? {

            if (pokemonNumbers.isEmpty()) {
                return null
            }

            val ID = pokemonNumbers[0]

            val pokeAPI = NetworkUtils.buildUrl(ID, "pokemon")

            try {
                return NetworkUtils.getResponseFromHttpUrl(pokeAPI!!)
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }

        }

        override fun onPostExecute(pokemonInfo: String?) {
            if (pokemonInfo != null && pokemonInfo != "") {
                val resultado = JSONObject(pokemonInfo)

                result.text = resultado.getJSONObject("species").getString("name").capitalize()
                numberView.text = "Pokédex #"+num
                heightView.text = "Height: "+(resultado.getInt("height").toFloat()/10.0).toString() + " m"
                weightView.text = "Weight: "+(resultado.getInt("weight").toFloat()/10.0).toString() + " kg"
                header.setBackgroundColor(Color.GRAY)
                result.setTextColor(Color.WHITE)

                val abilitiesList = resultado.getJSONArray("abilities")
                var abilitiesText : String = ""

                for (i in 0..(abilitiesList.length()-1)){
                    abilitiesText = abilitiesText+abilitiesList.getJSONObject(i).getJSONObject("ability").getString("name").capitalize()
                    if (i != abilitiesList.length()-1){
                        abilitiesText = abilitiesText+"\n"
                    }
                }

                abilitiesView.text = abilitiesText

                val types = resultado.getJSONArray("types")

                if (types.length() == 2){
                    typesView.text = "Types: "+types.getJSONObject(0).getJSONObject("type").getString("name").capitalize()+", "+types.getJSONObject(1).getJSONObject("type").getString("name").capitalize()
                }else{
                    typesView.text = "Type: "+types.getJSONObject(0).getJSONObject("type").getString("name").capitalize()
                }

                when(resultado.getJSONArray("types").getJSONObject(0).getJSONObject("type").getString("name")){
                    "poison" -> header.setBackgroundColor(Color.rgb(149, 0, 204))
                    "psychic" -> header.setBackgroundColor(Color.rgb(149, 0, 204))
                    "grass" -> header.setBackgroundColor(Color.rgb(0, 165, 27))
                    "water" -> header.setBackgroundColor(Color.rgb(0, 96, 186))
                    "fire" -> header.setBackgroundColor(Color.rgb(186, 0, 18))
                    "fairy" -> {
                        header.setBackgroundColor(Color.rgb(247, 191, 255))
                        result.setTextColor(Color.BLACK)
                    }
                    "fighting" -> header.setBackgroundColor(Color.rgb(193, 93, 0))
                    "flying" -> header.setBackgroundColor(Color.rgb(0, 177, 201))
                    "bug" -> header.setBackgroundColor(Color.rgb(136, 204, 0))
                    "rock" -> header.setBackgroundColor(Color.rgb(150, 95, 0))
                    "ground" -> header.setBackgroundColor(Color.rgb(198, 149, 0))
                    "dragon" -> header.setBackgroundColor(Color.rgb(61, 0, 122))
                    "ghost" -> header.setBackgroundColor(Color.rgb(128, 0, 214))
                    "ice" -> header.setBackgroundColor(Color.rgb(0, 171, 193))
                    "electric" -> {
                        header.setBackgroundColor(Color.rgb(255, 233, 0))
                        result.setTextColor(Color.BLACK)
                    }
                }
            }
        }
    }
}
