package com.example.islamichub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.*

class Show_Jamat_Time : AppCompatActivity() {

    private lateinit var tvCountry: Spinner
    private lateinit var tvDistrict: Spinner
    private lateinit var tvUpazila: Spinner
    private  lateinit var UpdateJamatImage: ImageView
    private lateinit var tvMosque: Spinner

    private lateinit var btn: Button
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_jamat_time)
        database = FirebaseDatabase.getInstance().reference

        tvCountry = findViewById(R.id.country_name)
        tvDistrict = findViewById(R.id.District_Name)
        tvUpazila = findViewById(R.id.Upazila_Name)
        tvMosque = findViewById(R.id.Mosque_Name)

        // Fetch data from Firebase Realtime Database for country names
        val countries = ArrayList<String>()
        countries.add("Select Country") // Add a hint item
        database.child("User").get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (childSnapshot in dataSnapshot.children) {
                    val countryName = childSnapshot.key // Get the country name
                    countryName?.let {
                        countries.add(countryName)
                    }
                }
            }
            val countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
            countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tvCountry.adapter = countryAdapter

            // Set a prompt for the country Spinner
            tvCountry.prompt = "Select Country"

            // Set OnItemSelectedListener for the country Spinner
            tvCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    if (position != 0) { // Exclude the hint item
                        val selectedCountry = tvCountry.selectedItem.toString()
                        fetchDistrictsForCountry(selectedCountry)
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Do nothing
                }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
        }

        btn = findViewById(R.id.get_button)

        btn.setOnClickListener {
            // Proceed to the next activity if all spinners are selected
            if (tvCountry.selectedItemPosition != 0 &&
                tvDistrict.selectedItemPosition != 0 &&
                tvUpazila.selectedItemPosition != 0 &&
                tvMosque.selectedItemPosition != 0
            ) {
                navigateToNextActivity()
            } else {
                Toast.makeText(this, "Please select all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Find the insert_mosque ImageView
        val insertMosqueImageView = findViewById<ImageView>(R.id.insert_mosque)

        // Set OnClickListener on the insertMosqueImageView
        insertMosqueImageView.setOnClickListener {
            navigateToInsertNamazTime()
        }
        UpdateJamatImage = findViewById(R.id.update_btn)


        UpdateJamatImage.setOnClickListener {
            navigateToInsertNamazTimeTwo()
        }
    }



    private fun fetchDistrictsForCountry(country: String) {
        database.child("User").child(country).get().addOnSuccessListener { dataSnapshot ->
            val districts = ArrayList<String>()
            districts.add("Select District") // Add a hint item
            if (dataSnapshot.exists()) {
                for (childSnapshot in dataSnapshot.children) {
                    val districtName = childSnapshot.key // Get the district name
                    districtName?.let {
                        districts.add(districtName)
                    }
                }
                val districtAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districts)
                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                tvDistrict.adapter = districtAdapter

                // Set a prompt for the district Spinner
                tvDistrict.prompt = "Select District"

                // Set OnItemSelectedListener for the district Spinner
                tvDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (position != 0) { // Exclude the hint item
                            val selectedDistrict = tvDistrict.selectedItem.toString()
                            fetchUpazilasForDistrict(country, selectedDistrict)
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Do nothing
                    }
                }
            } else {
                Toast.makeText(this, "No districts found for $country", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUpazilasForDistrict(country: String, district: String) {
        database.child("User").child(country).child(district).get().addOnSuccessListener { dataSnapshot ->
            val upazilas = ArrayList<String>()
            upazilas.add("Select Upazila") // Add a hint item
            if (dataSnapshot.exists()) {
                for (childSnapshot in dataSnapshot.children) {
                    val upazilaName = childSnapshot.key // Get the upazila name
                    upazilaName?.let {
                        upazilas.add(upazilaName)
                    }
                }
                val upazilaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, upazilas)
                upazilaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                tvUpazila.adapter = upazilaAdapter

                // Set a prompt for the upazila Spinner
                tvUpazila.prompt = "Select Upazila"

                // Set OnItemSelectedListener for the upazila Spinner
                tvUpazila.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (position != 0) { // Exclude the hint item
                            val selectedUpazila = tvUpazila.selectedItem.toString()
                            fetchMosquesForUpazila(country, district, selectedUpazila)
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Do nothing
                    }
                }
            } else {
                Toast.makeText(this, "No upazilas found for $district in $country", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchMosquesForUpazila(country: String, district: String, upazila: String) {
        database.child("User").child(country).child(district).child(upazila).get().addOnSuccessListener { dataSnapshot ->
            val mosques = ArrayList<String>()
            mosques.add("Select Mosque") // Add a hint item
            if (dataSnapshot.exists()) {
                for (childSnapshot in dataSnapshot.children) {
                    val mosqueName = childSnapshot.key // Get the mosque name
                    mosqueName?.let {
                        mosques.add(mosqueName)
                    }
                }
                val mosqueAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mosques)
                mosqueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                tvMosque.adapter = mosqueAdapter

                // Set a prompt for the mosque Spinner
                tvMosque.prompt = "Select Mosque"
            } else {
                Toast.makeText(this, "No mosques found for $upazila in $district, $country", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToInsertNamazTime() {
        val intent = Intent(this, Insert_Jamat_Time::class.java)
        startActivity(intent)
    }

    private fun navigateToInsertNamazTimeTwo() {
        val intent = Intent(this, UpdateJamatTime::class.java)
        startActivity(intent)
    }
    private fun navigateToNextActivity() {
        val intent = Intent(this, Actually_Show_Jamat_Time::class.java)
        intent.putExtra("country", tvCountry.selectedItem.toString().trim())
        intent.putExtra("district", tvDistrict.selectedItem.toString().trim())
        intent.putExtra("upazila", tvUpazila.selectedItem.toString().trim())
        intent.putExtra("mosque", tvMosque.selectedItem.toString().trim())
        startActivity(intent)
    }
}
