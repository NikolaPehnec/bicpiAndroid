package hr.fer.pi.bicpi.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hr.fer.infosus.bicpi.Constants
import hr.fer.infosus.bicpi.R
import hr.fer.infosus.bicpi.model.KupovniArtikl
import hr.fer.pi.bicpi.view.BasketActivity
import kotlinx.android.synthetic.main.artikl_view.view.*
import kotlinx.android.synthetic.main.dimension_prompt.view.*

class BasketAdapter(
    private var kupovniArtiklList: MutableList<KupovniArtikl>,
    private val context: BasketActivity,
    private val arrayID: String
) : RecyclerView.Adapter<BasketAdapter.PaperViewHolder>() {

    class PaperViewHolder(val paperView: View) : RecyclerView.ViewHolder(paperView)

    private val prefName = "Bicpi"

    override fun getItemCount(): Int = kupovniArtiklList.size

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PaperViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.artikl_view, parent, false)

        return PaperViewHolder(view)
    }


    override fun onBindViewHolder(holder: PaperViewHolder, position: Int) {

        with(holder.paperView) {


            artiklNaziv.text = "Boja: ${kupovniArtiklList[position].artikl.naziv}"
            artiklOpis.text = "Tip: ${kupovniArtiklList[position].artikl.opis}"
            artiklVrsta.text = "Težina: ${kupovniArtiklList[position].artikl.idGrupa}"
            artiklCijena.text = "Format: ${kupovniArtiklList[position].cijena}"


            quantityCounter.text = kupovniArtiklList[position].count.toString()
            addToCartBtn.visibility = View.GONE
            quantityCounter.visibility = View.VISIBLE
            deleteItemBtn.visibility = View.VISIBLE

            /*addDimensions.text =
                if (kupovniArtiklList[position].width != 0 && kupovniArtiklList[position].height != 0) "Promijeni\ndimenzije"
                else "Unesi nove\ndimenzije"*/

            paperServiceLayout.visibility =
                if (context.getSharedPreferences(prefName, Context.MODE_PRIVATE).getInt(
                        Constants.ROLE_ID, Constants.ROLE_NONE
                    ) == Constants.ROLE_ADMIN
                )
                    View.VISIBLE else View.GONE

            addDimensions.isEnabled =
                context.getSharedPreferences(prefName, Context.MODE_PRIVATE).getInt(
                    Constants.ROLE_ID, Constants.ROLE_NONE
                ) == Constants.ROLE_ADMIN


            deleteItemBtn.setOnClickListener {
                kupovniArtiklList.removeAt(position)
                deleteFromPref(position)
                notifyDataSetChanged()
            }

            addDimensions.setOnClickListener {
                getInput(position)

            }


        }


    }

    private fun deleteFromPref(pos: Int) {
        val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val gson = Gson()
        val recieved = pref.getString(arrayID, null)
        val type = object : TypeToken<MutableList<KupovniArtikl>>() {}.type
        val list = gson.fromJson<MutableList<KupovniArtikl>>(recieved, type)
        list.removeAt(pos)
        pref.edit().putString(arrayID, gson.toJson(list, type)).apply()

    }

    private fun changeAmount(pos: Int, width: Int, height: Int) {
        val pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        val gson = Gson()
        val recieved = pref.getString(arrayID, null)
        val type = object : TypeToken<MutableList<KupovniArtikl>>() {}.type
        val list = gson.fromJson<MutableList<KupovniArtikl>>(recieved, type)


        kupovniArtiklList = list
        pref.edit().putString(arrayID, gson.toJson(list, type)).apply()

        notifyItemChanged(pos)
    }

    private fun getInput(i: Int) {

        val li = LayoutInflater.from(context).inflate(R.layout.dimension_prompt, null);

        val builder = AlertDialog.Builder(context);

        builder.setView(li)

        builder.setCancelable(false)
            .setPositiveButton("Dodaj") { dialog, id ->
                changeAmount(
                    i,
                    li.widthInput.text.toString().toInt(),
                    li.heightInput.text.toString().toInt()
                )

            }.setNegativeButton("Natrag") { dialog, _ ->
                dialog.cancel()
            }

        builder.show()
    }


    private fun putPhoto(idx: Int) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"

        context.startActivityForResult(intent, Constants.RESULT_LOAD_IMAGE);
    }

}