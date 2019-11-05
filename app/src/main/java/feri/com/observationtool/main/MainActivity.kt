package feri.com.observationtool.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import feri.com.observationtool.R
import feri.com.observationtool.main.fragment.MenuListKelasFragment
import feri.com.observationtool.main.fragment.MenuProfilFragment
import feri.com.observationtool.main.fragment.MenuRekamFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navView.selectedItemId=R.id.nav_rekam
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_rekam ->loadFragment(MenuRekamFragment())
            R.id.nav_listkelas->loadFragment(MenuListKelasFragment())
            R.id.nav_profil -> loadFragment(MenuProfilFragment())
        }
        true
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
            return true
        }
        return false
    }
}
