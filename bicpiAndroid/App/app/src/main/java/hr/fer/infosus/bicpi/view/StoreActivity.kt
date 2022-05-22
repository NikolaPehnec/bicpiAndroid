package hr.fer.infosus.bicpi.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.fer.infosus.bicpi.Constants
import hr.fer.infosus.bicpi.R
import hr.fer.infosus.bicpi.Utils
import hr.fer.infosus.bicpi.adapters.SpinnerItem
import hr.fer.infosus.bicpi.adapters.StoreAdminAdapter
import hr.fer.infosus.bicpi.adapters.StoreKupacAdapter
import hr.fer.infosus.bicpi.model.KupovniArtikl
import hr.fer.infosus.bicpi.model.RentabilniArtikl
import hr.fer.infosus.bicpi.viewModels.ArtikliViewModel
import hr.fer.infosus.bicpi.viewModels.ArtikliViewModelFactory
import hr.fer.infosus.bicpi.viewModels.GrupaViewModel
import hr.fer.infosus.bicpi.viewModels.GrupaViewModelFactory
import kotlinx.android.synthetic.main.activity_store.*

class StoreActivity : AppCompatActivity(), StoreAdminAdapter.onIzbrisiButtonCliked {
    private lateinit var pref: SharedPreferences

    private val viewModelArtikli: ArtikliViewModel by viewModels() {
        ArtikliViewModelFactory(this)
    }
    private val viewModelGrupa: GrupaViewModel by viewModels() {
        GrupaViewModelFactory(this)
    }

    private var viewLists: List<List<View>> = mutableListOf()
    private var kupacViewList: List<View> = mutableListOf()
    private var adminViewList: List<View> = mutableListOf()

    private var grupaList: List<SpinnerItem> = mutableListOf()
    private var tipProizvodaList: List<SpinnerItem> = mutableListOf()

    private lateinit var storeAdapter: StoreAdminAdapter
    private lateinit var storekupacAdapter: StoreKupacAdapter

    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var kupovniArtiklList: List<KupovniArtikl>
    private lateinit var rentaArtiklList: List<RentabilniArtikl>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)

        postaviSpinnere()
        addViews()
        addListeners()

        pref = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)

        Utils.checkUser(pref, listOf(storeLayoutView, constraintLayout), viewLists)

        viewModelGrupa.getGrupe()

        viewModelArtikli.getKupovniArtikliLiveData().observe(this) { kupovniArtikli ->
            storePB.visibility = View.GONE

            if (kupovniArtikli != null) {
                kupovniArtiklList = kupovniArtikli
                rentaArtiklList = listOf()

                //updateKupovniArtikli(kupovniArtikli)
                generateRV(true)

            } else {
                Toast.makeText(
                    this,
                    "Pogreška kod učitavanja kupovnih artikala sa servera",
                    Toast.LENGTH_LONG
                ).show()

                /* kupovniArtiklList =
                     listOf(
                         KupovniArtikl(1, 1, "Bicikl 1", 1250f, "Mountain bike florence 12504", 4),
                         KupovniArtikl(
                             2,
                             1,
                             "Pomocni kotac 12' ",
                             55.50f,
                             "Gumeni kotac za  zamjenu",
                             3
                         ),
                         KupovniArtikl(3, 1, "Bicikl 1", 1250f, "Mountain bike florence 12504", 4),
                         KupovniArtikl(4, 1, "Bicikl 1", 1250f, "Mountain bike florence 12504", 4),
                         KupovniArtikl(5, 1, "Bicikl 1", 1250f, "Mountain bike florence 12504", 4)
                     )
                 //updateKupovniArtikli(kupovniArtikli)
                 generateRV(true)*/
            }
        }

        viewModelArtikli.getRentabilniArtikliLiveData().observe(this) { rentabilniArtikli ->
            storePB.visibility = View.GONE

            if (rentabilniArtikli != null) {
                rentaArtiklList = rentabilniArtikli
                kupovniArtiklList = listOf()
                //updateKupovniArtikli(kupovniArtikli)
                generateRV(false)
            } else {
                Toast.makeText(
                    this,
                    "Pogreška kod učitavanja rentabilnih artikala sa servera",
                    Toast.LENGTH_LONG
                ).show()

                /*kupovniArtiklList = listOf()
                rentaArtiklList =
                    listOf(
                        RentabilniArtikl(1, "Bicikl 1", 200f, "Mountain bike florence 12504", true),
                        RentabilniArtikl(
                            1,
                            "Pomocni kotac 12' ",
                            55.50f,
                            "Gumeni kotac za  zamjenu",
                            false
                        ),
                        RentabilniArtikl(
                            1,
                            "Bicikl 1",
                            100f,
                            "Mountain bike florence 12504",
                            false
                        ),
                        RentabilniArtikl(
                            1,
                            "Bicikl 1",
                            300f,
                            "Mountain bike florence 12504",
                            false
                        ),
                        RentabilniArtikl(1, "Bicikl 1", 400f, "Mountain bike florence 12504", true)
                    )

                generateRV(false)*/
            }
        }

        viewModelGrupa.getGrupaLiveData().observe(this) { grupe ->
            storePB.visibility = View.GONE

            if (grupe != null) {
                grupaList = grupe.map { SpinnerItem(it.idGrupa.toString(), it.nazivGrupa) }

                val adapter = ArrayAdapter(this, R.layout.custom_simple_spinner_item, grupaList)
                adapter.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item)
                grupaArtiklaSpinner.adapter = adapter
            } else {
                Toast.makeText(this, "Pogreška kod učitavanja grupa sa servera", Toast.LENGTH_LONG)
                    .show()
            }
        }

        viewModelGrupa.getPostGrupaResultLiveData().observe(this) { isSuccessful ->
            storePB.visibility = View.GONE

            if (isSuccessful) {
                Toast.makeText(this, "Uspješno zapisana grupa na serveru", Toast.LENGTH_LONG)
                    .show()

                //Refresh grupa
                viewModelGrupa.getGrupe()
            } else {
                Toast.makeText(this, "Pogreška kod zapisivanja grupe na serveru", Toast.LENGTH_LONG)
                    .show()
            }
        }

        viewModelArtikli.getPostKupovniResultLiveData().observe(this) { isSuccessful ->
            storePB.visibility = View.GONE

            if (isSuccessful) {
                Toast.makeText(
                    this,
                    "Uspješno zapisan kupovni artikl na serveru",
                    Toast.LENGTH_LONG
                ).show()

                //Refresh artikle
                viewModelArtikli.getKupovniArtikli()
            } else {
                Toast.makeText(
                    this,
                    "Pogreška kod zapisivanja kupovnog artikla na serveru",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        viewModelArtikli.getPostRentabilniResultLiveData().observe(this) { isSuccessful ->
            storePB.visibility = View.GONE

            if (isSuccessful) {
                Toast.makeText(
                    this,
                    "Uspješno zapisan rentabilni artikl na serveru",
                    Toast.LENGTH_LONG
                )
                    .show()

                //Refresh artikle
                viewModelArtikli.getRentabilniArtikli()
            } else {
                Toast.makeText(
                    this,
                    "Pogreška kod zapisivanja rentabilnog artikla na serveru",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }

        viewModelArtikli.getDeleteArtiklResultLiveData().observe(this) { isSuccessful ->
            storePB.visibility = View.GONE

            if (isSuccessful) {
                Toast.makeText(
                    this,
                    "Uspješno izbrisan artikl",
                    Toast.LENGTH_LONG
                )
                    .show()

                //Refresh artikle
                viewModelArtikli.getRentabilniArtikli()
            } else {
                Toast.makeText(
                    this,
                    "Pogreška kod brisanja artikla",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        }


    }

    private fun postaviSpinnere() {
        tipProizvodaList =
            mutableListOf(SpinnerItem("1", "Za kupovinu"), SpinnerItem("2", "Za rent"))
        val adapter = ArrayAdapter(this, R.layout.custom_simple_spinner_item, tipProizvodaList)
        adapter.setDropDownViewResource(R.layout.custom_simple_spinner_dropdown_item)
        tipArtiklaSpinner.adapter = adapter
        tipArtiklaSpinner2.adapter = adapter
    }

    /* private fun updateKupovniArtikli(artikli: List<KupovniArtikl>) {
         storeAdapter.setArtikli(artikli)
     }*/

    private fun addViews() {
        kupacViewList = mutableListOf(
            constraintLayout,
            textViewTip,
            cardViewTip,
            cardView,
            textView,
            storeRecyclerView,
            RVview, storePB
        )
        adminViewList = mutableListOf(
            constraintLayout,
            textViewTip,
            cardViewTip,
            textView,
            cardView,
            storeRecyclerView,
            RVview,
            zapisiGrupuButton,
            storePB,
            novaGrupaButton,
            noviArtiklButton,
        )

        viewLists =
            mutableListOf(mutableListOf(), kupacViewList, adminViewList)
    }

    fun generateRV(kupovni: Boolean) {
        //2 JE ADMIN, 1 JE KUPAC

        var role = pref.getInt(Constants.ROLE_ID, 1)
        viewManager = LinearLayoutManager(this)

        if (role == 2)
            storeRecyclerView.adapter =
                StoreAdminAdapter(kupovniArtiklList, rentaArtiklList, this, kupovni, this)
        else if (role == 1)
            storeRecyclerView.adapter =
                StoreKupacAdapter(kupovniArtiklList, rentaArtiklList, this, kupovni)

        storeRecyclerView.setHasFixedSize(true)
        storeRecyclerView.layoutManager = viewManager


    }

    override fun onIzbrisiClicked(artiklId: Int) {
        storePB.visibility = View.VISIBLE

        viewModelArtikli.deleteArtikl(artiklId)
    }

    fun addListeners() {
        zapisiGrupuButton.setOnClickListener {
            if (nazivGrupe.text.toString().equals("") ||
                grupaList.filter { item -> item.name.equals(nazivGrupe.text.toString()) }
                    .firstOrNull() != null
            ) {
                Toast.makeText(this, "Unesite novi naziv grupe!", Toast.LENGTH_LONG).show()
            } else {
                storePB.visibility = View.VISIBLE
                viewModelGrupa.postGrupa(nazivGrupe.text.toString())
            }
        }

        nazivGrupe.setOnFocusChangeListener { view, b ->
            if (b) {
                nazivArtiklTL.visibility = View.GONE
                cijenaArtiklTL.visibility = View.GONE
                opisArtiklTL.visibility = View.GONE
                zapisiArtiklButton.visibility=View.GONE
                updateArtiklButton.visibility = View.GONE
                kolicinaArtiklTL.visibility = View.GONE
                cardViewTip2.visibility = View.GONE
                noviArtiklButton.visibility = View.GONE
            } else {
                noviArtiklButton.visibility = View.VISIBLE
            }

        }

        zapisiArtiklButton.setOnClickListener {
            val item = tipArtiklaSpinner2.selectedItem as SpinnerItem
            if (item.name.equals("Za kupovinu")) {
                if (nazivArtikl.text.toString().equals("") || cijenaArtikl.text.toString()
                        .equals("") ||
                    kolArtikl.text.toString().equals("") || opisArtikl.text.toString().equals("")
                ) {
                    Toast.makeText(this, "Unesite sve podatke!", Toast.LENGTH_LONG).show()
                } else {
                    if (grupaArtiklaSpinner.selectedItem != null) {
                        val grupa = grupaArtiklaSpinner.selectedItem as SpinnerItem
                        storePB.visibility = View.VISIBLE
                        viewModelArtikli.postKupovniArtikl(
                            grupa.id.toInt(),
                            nazivArtikl.text.toString(),
                            cijenaArtikl.text.toString().toFloat(),
                            opisArtikl.text.toString(),
                            kolArtikl.text.toString().toInt()
                        )
                    } else {
                        Toast.makeText(this, "Pogrešna grupa", Toast.LENGTH_LONG).show()
                    }


                }
            } else if (item.name.equals("Za rent")) {
                if (nazivArtikl.text.toString().equals("") || cijenaArtikl.text.toString()
                        .equals("") || opisArtikl.text.toString().equals("")
                ) {
                    Toast.makeText(this, "Unesite sve podatke!", Toast.LENGTH_LONG).show()
                } else {
                    if (grupaArtiklaSpinner.selectedItem != null) {
                        storePB.visibility = View.VISIBLE
                        val grupa = grupaArtiklaSpinner.selectedItem as SpinnerItem
                        viewModelArtikli.postRentabilniArtikl(
                            grupa.id.toInt(),
                            nazivArtikl.text.toString(),
                            cijenaArtikl.text.toString().toFloat(),
                            opisArtikl.text.toString()
                        )
                    } else {
                        Toast.makeText(this, "Pogrešna grupa", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        tipArtiklaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (tipProizvodaList[position].name.equals("Za kupovinu")) {
                    storePB.visibility = View.VISIBLE
                    viewModelArtikli.getKupovniArtikli()
                } else if (tipProizvodaList[position].name.equals("Za rent")) {
                    storePB.visibility = View.VISIBLE
                    viewModelArtikli.getRentabilniArtikli()
                }
            }
        }

        tipArtiklaSpinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (tipProizvodaList[position].name.equals("Za kupovinu")) {
                    if (cijenaArtiklTL.visibility == View.VISIBLE)
                        kolicinaArtiklTL.visibility = View.VISIBLE
                } else if (tipProizvodaList[position].name.equals("Za rent")) {
                    kolicinaArtiklTL.visibility = View.GONE
                }
            }
        }

        novaGrupaButton.setOnClickListener {
            if (nazivGrupeTL.visibility == View.VISIBLE) {
                nazivGrupeTL.visibility = View.GONE
                novaGrupaButton.setText("DODAJ NOVU GRUPU")
            } else {
                nazivGrupeTL.visibility = View.VISIBLE
                novaGrupaButton.setText("SAKRIJ NOVU GRUPU")
            }
        }

        noviArtiklButton.setOnClickListener {
            if (nazivArtiklTL.visibility == View.GONE) {
                nazivArtiklTL.visibility = View.VISIBLE
                cijenaArtiklTL.visibility = View.VISIBLE
                opisArtiklTL.visibility = View.VISIBLE
                updateArtiklButton.visibility = View.VISIBLE

                val item = tipArtiklaSpinner2.selectedItem as SpinnerItem
                if (item.name.equals("Za kupovinu"))
                    kolicinaArtiklTL.visibility = View.VISIBLE
                else
                    kolicinaArtiklTL.visibility = View.GONE

                cardViewTip2.visibility = View.VISIBLE
                zapisiArtiklButton.visibility = View.VISIBLE
                noviArtiklButton.setText("SAKRIJ NOVI ARTIKL")
            } else {
                nazivArtiklTL.visibility = View.GONE
                cijenaArtiklTL.visibility = View.GONE
                opisArtiklTL.visibility = View.GONE
                updateArtiklButton.visibility = View.GONE
                kolicinaArtiklTL.visibility = View.GONE
                cardViewTip2.visibility = View.GONE
                zapisiArtiklButton.visibility = View.GONE
                noviArtiklButton.setText("DODAJ NOVI ARTIKL")
            }
        }
    }


}