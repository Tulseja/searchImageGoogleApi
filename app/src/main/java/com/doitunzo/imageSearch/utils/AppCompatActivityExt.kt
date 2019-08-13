package com.doitunzo.imageSearch.utils


import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity


fun AppCompatActivity.replaceFragment(@IdRes layoutId: Int, fragment: Fragment, allowStateLoss: Boolean = true) {
    val transaction = this.supportFragmentManager.beginTransaction().replace(layoutId, fragment)

    if (allowStateLoss) {
        transaction.commitAllowingStateLoss()
    } else {
        transaction.commit()
    }
}

fun Fragment.addFragment(@IdRes layoutId : Int , fragment : Fragment, allowStateLoss : Boolean = true){

    val ft = this.fragmentManager?.beginTransaction()?.add(layoutId,fragment)
    if (allowStateLoss) {
        ft?.commitAllowingStateLoss()
    } else {
        ft?.commit()
    }

}

fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(application)).get(viewModelClass)

fun <T : ViewModel> Fragment.obtainViewModel(viewModelClass: Class<T>) =
        ViewModelProviders.of(this, ViewModelFactory.getInstance(activity!!.application)).get(viewModelClass)

