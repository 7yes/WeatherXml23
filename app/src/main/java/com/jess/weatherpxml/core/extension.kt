package com.jess.weatherpxml.core

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment

// Some of my personal fun extension helpful
// check int nullability
fun Any?.isNull() = this == null

//Toast
fun Activity.toast(text:String, length:Int = Toast.LENGTH_SHORT){
   Toast.makeText(this,text,length).show()
}

fun Fragment.hideKeyboard() {
   view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
   hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
   val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
   inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}