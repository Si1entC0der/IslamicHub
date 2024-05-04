package com.example.islamichub

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Actually_Insert_Jamat_Time : AppCompatActivity() {
    private lateinit var back_btnIN : Button
    private lateinit var  fajrbtn : ImageView
    private lateinit var  dhurbtn : ImageView
    private lateinit var  asarbtn : ImageView
    private lateinit var  magribbtn : ImageView
    private lateinit var  ishabtn : ImageView
    private lateinit var  Btn : Button
    private var  sFajr  : String? = null
    private var  sDhur  : String? = null
    private var  sAsar  : String? = null
    private var  sMaghrib  : String? = null
    private var  sIsha  : String? = null
    private lateinit var  sCountry: String
    private lateinit var  sDistrict: String
    private lateinit var  sUpazila: String
    private lateinit var  sMosque: String
    private lateinit var  database   : DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actually_insert_jamat_time)
        database = Firebase.database.reference

        fajrbtn = findViewById(R.id.ufajr_btn)
        dhurbtn = findViewById(R.id.udhur_btn)
        asarbtn = findViewById(R.id.uasar_btn)
        magribbtn = findViewById(R.id.umaghrib_btn)
        ishabtn = findViewById(R.id.uisha_btn)
        back_btnIN = findViewById(R.id.back_in_btn)

        // Retrieve values from Intent extras
        sCountry = intent.getStringExtra("country") ?: ""
        sDistrict = intent.getStringExtra("district") ?: ""
        sUpazila = intent.getStringExtra("upazila") ?: ""
        sMosque = intent.getStringExtra("mosque") ?: ""


        fajrbtn.setOnClickListener {
            showTimePickerDialog { selectedTime -> sFajr = selectedTime }
        }
        dhurbtn.setOnClickListener {
            showTimePickerDialog { selectedTime -> sDhur = selectedTime }
        }
        asarbtn.setOnClickListener {
            showTimePickerDialog { selectedTime -> sAsar = selectedTime }
        }
        magribbtn.setOnClickListener {
            showTimePickerDialog { selectedTime -> sMaghrib = selectedTime }
        }
        ishabtn.setOnClickListener {
            showTimePickerDialog { selectedTime -> sIsha = selectedTime }
        }

        Btn = findViewById(R.id.save_btn)
        Btn.setOnClickListener {
            saveData()
        }
        back_btnIN.setOnClickListener {
            val intent = Intent(this, Show_Jamat_Time::class.java)
            startActivity(intent)
        }


    }
    private fun showTimePickerDialog(fieldToUpdate: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
                calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
                calendar.set(Calendar.MINUTE, selectedMinute)
                val selectedTime = timeFormat.format(calendar.time)
                fieldToUpdate(selectedTime)
            },
            hour,
            minute,
            false
        )
        timePickerDialog.show()
    }

    private fun saveData() {
        // Check if any field is empty
        if (sCountry.isEmpty() || sDistrict.isEmpty() || sUpazila.isEmpty() || sMosque.isEmpty() || sFajr.isNullOrEmpty()
            || sDhur.isNullOrEmpty() || sAsar.isNullOrEmpty() || sMaghrib.isNullOrEmpty() || sIsha.isNullOrEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val user = UserMode(sFajr!!, sDhur!!, sAsar!!, sMaghrib!!, sIsha!!)

        database.child("User").child(sCountry).child(sDistrict).child(sUpazila)
            .child(sMosque).setValue(user)

        Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show()
    }
}
