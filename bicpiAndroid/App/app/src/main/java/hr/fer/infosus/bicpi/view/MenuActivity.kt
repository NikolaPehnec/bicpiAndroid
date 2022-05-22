package hr.fer.infosus.bicpi.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import hr.fer.infosus.bicpi.Constants
import hr.fer.infosus.bicpi.R
import hr.fer.infosus.bicpi.Utils
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : Activity() {

    private var viewLists: List<List<View>> = mutableListOf()

    private var notLoggedViewList: List<View> = mutableListOf()
    private var kupacViewList: List<View> = mutableListOf()
    private var adminViewList: List<View> = mutableListOf()

    private lateinit var pref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_menu)
        pref = getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)


        addViews()

        Utils.checkUser(
            pref,
            listOf(menuLayoutView, bottomLayout, menuScrollView, menuLinearLayout),
            viewLists
        )

        /*businessSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.d("Menu", isChecked.toString())

            requestBtn.visibility = if(isChecked) View.VISIBLE else View.GONE
        }*/

        storeBtn.setOnClickListener {
            startActivityForResult(
                generateIntent(StoreActivity::class.java),
                Constants.STORE_REQUEST
            )
        }

        loginBtn.setOnClickListener {
            startActivityForResult(
                generateIntent(LoginActivity::class.java),
                Constants.LOGIN_REQUEST
            )
        }

        registerBtn.setOnClickListener {
            startActivityForResult(
                generateIntent(RegisterActivity::class.java),
                Constants.LOGIN_REQUEST
            )
        }


        logoutBtn.setOnClickListener {
            pref.edit().clear().apply()
            Utils.checkUser(
                pref,
                listOf(menuLayoutView, menuScrollView, menuLinearLayout, bottomLayout),
                viewLists
            )
        }


        transactionHistoryBtn.setOnClickListener {
            startActivityForResult(
                generateIntent(
                    TransactionHistoryActivity::class.java
                ), Constants.TRANSACTION_REQUEST
            )
        }

    }

    private fun generateIntent(cls: Class<out Activity>): Intent {
        val intent = Intent(this, cls)
        // intent.putExtra(Constants.TOGGLE_STATE, businessSwitch.isChecked)

        return intent
    }

    private fun addViews() {

         notLoggedViewList = mutableListOf(
             menuScrollView, menuLinearLayout,
             bottomLayout, registerBtn, loginBtn, textTitleView
         )

        kupacViewList = mutableListOf(
            transactionHistoryBtn, menuScrollView, menuLinearLayout,
            storeBtn, basketBtn, bottomLayout, logoutBtn, textTitleView
        )

        adminViewList = mutableListOf(
            transactionHistoryBtn,
            storeBtn,
            menuScrollView,
            menuLinearLayout,
            bottomLayout,
            narudzbeListButton,
            logoutBtn,
            textTitleView
        )

        viewLists =
            mutableListOf(notLoggedViewList,kupacViewList, adminViewList)
    }

}