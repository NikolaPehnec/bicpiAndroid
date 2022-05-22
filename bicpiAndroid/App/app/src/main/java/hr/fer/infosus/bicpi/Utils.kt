package hr.fer.infosus.bicpi

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.ToggleButton
import androidx.core.view.children
import hr.fer.infosus.bicpi.Constants

object Utils {
    @SuppressLint("SetTextI18n")
    fun checkUser(pref: SharedPreferences, layoutView: List<ViewGroup>, viewLists: List<List<View>>){
        val name = pref.getString(Constants.NAME_ID, null)
        val role = pref.getInt(Constants.ROLE_ID, 0)
        Log.d("role", role.toString())
        for(lv in layoutView) {
            for (v in lv.children) {
                v.visibility = if (v in viewLists[role]) View.VISIBLE else View.GONE
            }
        }

    }

}