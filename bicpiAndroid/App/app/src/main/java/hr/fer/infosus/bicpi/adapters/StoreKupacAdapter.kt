package hr.fer.infosus.bicpi.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.fer.infosus.bicpi.Constants
import hr.fer.infosus.bicpi.R
import hr.fer.infosus.bicpi.model.KupovniArtikl
import hr.fer.infosus.bicpi.model.RentabilniArtikl
import kotlinx.android.synthetic.main.artikl_view.view.*

class StoreKupacAdapter(
    private var kupovniArtiklList: List<KupovniArtikl>,
    private var rentabilniArtiklList: List<RentabilniArtikl>,
    private val adapter: Activity,
    private val kupovni: Boolean
) : RecyclerView.Adapter<StoreKupacAdapter.ArtiklViewHolder>() {


    class ArtiklViewHolder(val artiklView: View) : RecyclerView.ViewHolder(artiklView)

    override fun getItemCount(): Int = kupovniArtiklList.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArtiklViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.artikl_view, parent, false)

        return ArtiklViewHolder(view)
    }

    fun setArtikli(artikli: List<KupovniArtikl>) {
        kupovniArtiklList = artikli
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ArtiklViewHolder, position: Int) {
        with(holder.artiklView) {
            if (kupovni) {
                artiklNaziv.text = "Naziv: " + kupovniArtiklList[position].artikl.naziv
                artiklOpis.text = "Opis: " + kupovniArtiklList[position].artikl.opis
                artiklVrsta.text = "Vrsta: " + kupovniArtiklList[position].artikl.idGrupa.toString()
                artiklCijena.text = "Cijena: " + kupovniArtiklList[position].cijena.toString()
                artiklIznajmljen.visibility = android.view.View.GONE
            } else {
                //Za rent:
                artiklNaziv.text = "Naziv: " + rentabilniArtiklList[position].artikl.naziv
                artiklOpis.text = "Opis: " + rentabilniArtiklList[position].artikl.opis
                artiklVrsta.text =
                    "Grupa: " + rentabilniArtiklList[position].artikl.idGrupa.toString()
                artiklCijena.text =
                    "Cijena/dan: " + rentabilniArtiklList[position].cijena.toString()
                artiklIznajmljen.visibility = android.view.View.VISIBLE
                if (rentabilniArtiklList[position].iznajmljeno)
                    artiklIznajmljen.text = "Dostupno za rent: NE"
                else
                    artiklIznajmljen.text = "Dostupno za rent: DA"

            }

            deleteItemBtn.visibility = View.GONE
            addToCartBtn.visibility = View.VISIBLE

            if (kupovni)
                quantityCounter.visibility = View.VISIBLE
            else
                quantityCounter.visibility = View.GONE


            addToCartBtn.setOnClickListener {
                storeToPref(Constants.BASKET_ID, position, holder)
                Toast.makeText(adapter, "Predmet dodan!", Toast.LENGTH_LONG).show()

                if (kupovni) {
                    var kol = Integer.parseInt(quantityCounter.text.toString().split(":")[1])
                    kol++
                    quantityCounter.text = "Kol:" + kol.toString()
                }
            }


        }

    }

    private fun storeToPref(where: String, pos: Int, holder: ArtiklViewHolder) {
        val pref = adapter.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val recieved = pref.getString(where, null)
        val type = object : TypeToken<MutableList<KupovniArtikl>>() {}.type
        var list = gson.fromJson<MutableList<KupovniArtikl>>(recieved, type)
        if (list == null) list = mutableListOf()
        var modified = false
        list.forEach {
            if (it == kupovniArtiklList[pos]) {
                it.count++
                modified = true
            }
        }
        if (!modified) list.add(kupovniArtiklList[pos])

        pref.edit().putString(where, gson.toJson(list, type)).apply()
    }

}