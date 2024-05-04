package com.example.islamichub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.islamichub.databinding.ActivityMainBinding
import kotlin.collections.Map

class MainActivity : AppCompatActivity() {

      private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Home())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home_page -> replaceFragment(Home())
                R.id.all_namaz_time -> replaceFragment(All_Namaz_Time())
                R.id.setting_Page -> replaceFragment(Settings())
                else -> {



                }
            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager =  supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}