package hr.fer.infosus.bicpi.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import hr.fer.infosus.bicpi.R
import kotlinx.android.synthetic.main.activity_purchase.*

class PurchaseActivity : Activity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)


        /**
         * Prikazati narudzbu
         */
        backBtn.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MenuActivity::class.java
                ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            )
        }
    }

    override fun onBackPressed() {
        startActivity(
            Intent(
                this,
                MenuActivity::class.java
            ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

}