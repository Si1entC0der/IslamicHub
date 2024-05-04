package com.example.islamichub

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Actually_Show_Jamat_Time : AppCompatActivity() {

    private lateinit var sCountry: String
    private lateinit var sDistrict: String
    private lateinit var sUpazila: String
    private lateinit var sMosque: String
    private lateinit var tvFajr: TextView
    private lateinit var tvDhur: TextView
    private lateinit var tvAsar: TextView
    private lateinit var tvMaghrib: TextView
    private lateinit var tvIsha: TextView
    private lateinit var database: DatabaseReference
 //   private lateinit var backbtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actually_show_jamat_time)

        database = Firebase.database.reference
        tvFajr = findViewById(R.id.get_fajr)
        tvDhur = findViewById(R.id.get_dhur)
        tvAsar = findViewById(R.id.get_asar)
        tvMaghrib = findViewById(R.id.get_maghrib)
        tvIsha = findViewById(R.id.get_isha)
       // backbtn = findViewById(R.id.back_button)

        // Retrieve values from Intent extras
        sCountry = intent.getStringExtra("country") ?: ""
        sDistrict = intent.getStringExtra("district") ?: ""
        sUpazila = intent.getStringExtra("upazila") ?: ""
        sMosque = intent.getStringExtra("mosque") ?: ""

        // Set mosque name to TextView
        val mosqueNameTextView = findViewById<TextView>(R.id.Mosjid_Name)
        mosqueNameTextView.text = sMosque

        // Retrieve prayer times from Firebase Database
        database.child("User").child(sCountry).child(sDistrict).child(sUpazila).child(sMosque).get().addOnSuccessListener { dataSnapshot ->
            val fajr = dataSnapshot.child("fajr").value?.toString() ?: ""
            val dhur = dataSnapshot.child("dhur").value?.toString() ?: ""
            val asar = dataSnapshot.child("asar").value?.toString() ?: ""
            val maghrib = dataSnapshot.child("magrib").value?.toString() ?: ""
            val isha = dataSnapshot.child("isha").value?.toString() ?: ""

            tvFajr.text = fajr
            tvDhur.text = dhur
            tvAsar.text = asar
            tvMaghrib.text = maghrib
            tvIsha.text = isha
        }.addOnFailureListener { exception ->
            Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
        }
        // Set OnClickListener for the back button
//        backbtn.setOnClickListener {
//            // Navigate back to the Settings fragment
//            val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.fragment_container, Settings())
//            fragmentTransaction.addToBackStack(null)
//            fragmentTransaction.commit()
//        }
    }
}
