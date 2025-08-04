package com.tumble.kronoxtoapp.extensions.presentation.View

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

fun Activity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    currentFocus?.windowToken?.let {
        imm.hideSoftInputFromWindow(it, 0)
    }
}