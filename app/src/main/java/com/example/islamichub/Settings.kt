package com.example.islamichub

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.database.*

class Settings : Fragment() {
   // private lateinit var updateJamatImage: ImageView
    private lateinit var tvCountry: Spinner
    private lateinit var tvDistrict: Spinner
    private lateinit var tvUpazila: Spinner
    private lateinit var tvMosque: Spinner
    private lateinit var btn: Button
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().reference

        // Initialize views
        tvCountry = view.findViewById(R.id.country_name)
        tvDistrict = view.findViewById(R.id.District_Name)
        tvUpazila = view.findViewById(R.id.Upazila_Name)
        tvMosque = view.findViewById(R.id.Mosque_Name)
        btn = view.findViewById(R.id.get_button)

        // Fetch data for the country spinner
        fetchCountries()

        // Set click listener for the button
        btn.setOnClickListener {
            // Proceed to the Actually_Show_Jamat_Time activity if all spinners are selected
            if (tvCountry.selectedItemPosition != 0 &&
                tvDistrict.selectedItemPosition != 0 &&
                tvUpazila.selectedItemPosition != 0 &&
                tvMosque.selectedItemPosition != 0
            ) {
                navigateToActuallyShowJamatTime()
            } else {
                Toast.makeText(requireContext(), "Please select all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Find the insert_mosque ImageView
//        val insertMosqueImageView = view.findViewById<ImageView>(R.id.insert_mosque)
//
//        // Set OnClickListener on the insertMosqueImageView
//        insertMosqueImageView.setOnClickListener {
//            navigateToInsertNamazTime()
//        }

//        // Find the update_btn ImageView
//        updateJamatImage = view.findViewById(R.id.update_btn)
//
//        // Set OnClickListener on the updateJamatImage
//        updateJamatImage.setOnClickListener {
//            navigateToUpdateNamazTime()
//        }

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Display toast message if there was any error fetching data
        arguments?.getString("errorMessage")?.let {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

//    private fun navigateToUpdateNamazTime() {
//        // Perform navigation to UpdateJamatTime activity
//        val intent = Intent(requireContext(), UpdateJamatTime::class.java)
//        startActivity(intent)
//    }

//    private fun navigateToInsertNamazTime() {
//        // Perform navigation to Insert_Jamat_Time activity
//        val intent = Intent(requireContext(), Insert_Jamat_Time::class.java)
//        startActivity(intent)
//    }

    private fun navigateToActuallyShowJamatTime() {
        val intent = Intent(requireContext(), Actually_Show_Jamat_Time::class.java)
        intent.putExtra("country", tvCountry.selectedItem.toString().trim())
        intent.putExtra("district", tvDistrict.selectedItem.toString().trim())
        intent.putExtra("upazila", tvUpazila.selectedItem.toString().trim())
        intent.putExtra("mosque", tvMosque.selectedItem.toString().trim())
        startActivity(intent)
    }

    private fun fetchCountries() {
        val context = context ?: return // Check if the fragment is attached to a context
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
            val countryAdapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, countries)
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
            Toast.makeText(context, exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    private fun fetchDistrictsForCountry(country: String) {
        val districts = ArrayList<String>()
        districts.add("Select District") // Add a hint item
        database.child("User").child(country).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (childSnapshot in dataSnapshot.children) {
                    val districtName = childSnapshot.key // Get the district name
                    districtName?.let {
                        districts.add(districtName)
                    }
                }
            }
            val districtAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, districts)
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
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUpazilasForDistrict(country: String, district: String) {
        val upazilas = ArrayList<String>()
        upazilas.add("Select Upazila") // Add a hint item
        database.child("User").child(country).child(district).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (childSnapshot in dataSnapshot.children) {
                    val upazilaName = childSnapshot.key // Get the upazila name
                    upazilaName?.let {
                        upazilas.add(upazilaName)
                    }
                }
            }
            val upazilaAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, upazilas)
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
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchMosquesForUpazila(country: String, district: String, upazila: String) {
        val mosques = ArrayList<String>()
        mosques.add("Select Mosque") // Add a hint item
        database.child("User").child(country).child(district).child(upazila).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (childSnapshot in dataSnapshot.children) {
                    val mosqueName = childSnapshot.key // Get the mosque name
                    mosqueName?.let {
                        mosques.add(mosqueName)
                    }
                }
            }
            val mosqueAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, mosques)
            mosqueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            tvMosque.adapter = mosqueAdapter

            // Set a prompt for the mosque Spinner
            tvMosque.prompt = "Select Mosque"
        }.addOnFailureListener { exception ->
            Toast.makeText(requireContext(), exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }
}
