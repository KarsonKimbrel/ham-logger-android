package com.kimbrelk.android.hamlogger.ui.mainactivity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.kimbrelk.android.hamlogger.R
import com.kimbrelk.android.hamlogger.ui.booklist.FragmentBooklist

class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.addOnBackStackChangedListener {
            updateUpIndicator()
        }
        if (supportFragmentManager.fragments.size == 0) {
            push(FragmentBooklist(), true)
        }
        viewModel // To force it's creation
    }

    override fun onSupportNavigateUp() : Boolean {
        pop()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragments = supportFragmentManager.fragments
        for (fragment in fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val fragments = supportFragmentManager.fragments
        for (fragment in fragments) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun updateUpIndicator() {
        val hasPreviousFragment = supportFragmentManager.backStackEntryCount == 0
        supportActionBar!!.setDisplayHomeAsUpEnabled(!hasPreviousFragment)
    }

    fun pop() {
        supportFragmentManager.popBackStack()
    }

    fun push(fragment: Fragment, isFirst: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frag, fragment)
        if (!isFirst) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

}