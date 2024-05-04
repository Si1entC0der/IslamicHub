package com.example.islamichub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Insert_Jamat_Time : AppCompatActivity() {

    private lateinit var tvCountry: Spinner
    private lateinit var tvDistrict: Spinner
    private lateinit var tvUpazila: Spinner
    private lateinit var tvMosque: TextView
    private lateinit var btn: Button

    private lateinit var selectedCountry: String
    private lateinit var selectedDistrict: String
    private lateinit var selectedUpazila: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_jamat_time)

        tvCountry = findViewById(R.id.sp_country)
        tvDistrict = findViewById(R.id.sp_district)
        tvUpazila = findViewById(R.id.sp_upazila)
        tvMosque = findViewById(R.id.et_mosque)
        btn = findViewById(R.id.save_button)

        // Fetch data for the country spinner from Firebase
        fetchCountryData()

        btn.setOnClickListener {
            saveData()
        }
    }

    private fun fetchCountryData() {
        val databaseReference = Firebase.database.reference.child("CountryDetail")
        val countries = ArrayList<String>()
        countries.add("Select Country") // Add a hint item
        databaseReference.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (countrySnapshot in dataSnapshot.children) {
                    val countryName = countrySnapshot.key // Get the country name
                    countryName?.let {
                        countries.add(countryName)
                    }
                }
                val countryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countries)
                countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                tvCountry.adapter = countryAdapter

                // Set OnItemSelectedListener for the country Spinner
                tvCountry.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (position != 0) { // Exclude the hint item
                            selectedCountry = tvCountry.selectedItem.toString()
                            fetchDistrictsForCountry(selectedCountry)
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Do nothing
                    }
                }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error fetching countries: $exception", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchDistrictsForCountry(country: String) {
        val databaseReference = Firebase.database.reference.child("CountryDetail").child(country)
        val districts = ArrayList<String>()
        districts.add("Select District") // Add a hint item
        databaseReference.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (districtSnapshot in dataSnapshot.children) {
                    val districtName = districtSnapshot.key // Get the district name
                    districtName?.let {
                        districts.add(districtName)
                    }
                }
                val districtAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districts)
                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                tvDistrict.adapter = districtAdapter

                // Set OnItemSelectedListener for the district Spinner
                tvDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (position != 0) { // Exclude the hint item
                            selectedDistrict = tvDistrict.selectedItem.toString()
                            fetchUpazilasForDistrict(country, selectedDistrict)
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Do nothing
                    }
                }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error fetching districts: $exception", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUpazilasForDistrict(country: String, district: String) {
        val databaseReference = Firebase.database.reference.child("CountryDetail").child(country).child(district)
        val upazilas = ArrayList<String>()
        upazilas.add("Select Upazila") // Add a hint item
        databaseReference.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (upazilaSnapshot in dataSnapshot.children) {
                    val upazilaName = upazilaSnapshot.key // Get the upazila name
                    upazilaName?.let {
                        upazilas.add(upazilaName)
                    }
                }
                val upazilaAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, upazilas)
                upazilaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                tvUpazila.adapter = upazilaAdapter

                // Set OnItemSelectedListener for the upazila Spinner
                tvUpazila.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        if (position != 0) { // Exclude the hint item
                            selectedUpazila = tvUpazila.selectedItem.toString()
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Do nothing
                    }
                }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Error fetching upazilas: $exception", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveData() {
        // Proceed to the next activity if all spinners are selected
        if (selectedCountry != "Select Country" &&
            selectedDistrict != "Select District" &&
            selectedUpazila != "Select Upazila" &&
            tvMosque.text.isNotEmpty()
        ){
            // Create an Intent to start the Actually_Insert_Jamat_Time activity
            val intent = Intent(this, Actually_Insert_Jamat_Time::class.java)

            // Add extras to the intent
            intent.putExtra("country", selectedCountry)
            intent.putExtra("district", selectedDistrict)
            intent.putExtra("upazila", selectedUpazila)
            intent.putExtra("mosque", tvMosque.text.toString()) // Pass the mosque name

            // Start the Actually_Insert_Jamat_Time activity
            startActivity(intent)

            // Clear the selected values
            selectedCountry = ""
            selectedDistrict = ""
            selectedUpazila = ""
        } else {
            Toast.makeText(this, "Please select all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
