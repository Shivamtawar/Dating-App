package com.example.datingapp.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.datingapp.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object Config {

    private var dialog : AlertDialog? = null

    fun showdialog(context: Context){
        dialog = MaterialAlertDialogBuilder(context)
            .setView(R.layout.loadinglayout)
            .setCancelable(false)
            .create()

        dialog!!.show()
    }

    fun hideDialog(){
        dialog!!.dismiss()
    }



}