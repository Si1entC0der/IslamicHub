package com.example.islamichub

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random

class Home : Fragment() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var qiabaImage : ImageView
    private lateinit var tasbeehImage : ImageView
    private lateinit var JamatIcon : ImageView

    private var currentlocation: TextView? = null

    private val hadiths = arrayOf(
        "And We have certainly made the Qur'an easy to remember. (Quran 54:17)",
        "Speak good or remain silent. (Hadith - Bukhari)",
        "Allah is beautiful and He loves beauty. (Hadith - Muslim)",
        "Do good to others as Allah has done good to you. (Hadith - Bukhari)",
        "Kindness is a mark of faith. (Hadith - Muslim)",
        "The best among you are those who are best to their wives. (Hadith - Tirmidhi)",
        "The reward of deeds depends upon the intentions. (Hadith - Bukhari)",
        "Patience is the key to paradise. (Hadith - Tirmidhi)",
        "Forgive, and you will be forgiven. (Hadith - Ibn Majah)",
        "Whoever believes in Allah and the Last Day, let him speak good or remain silent. (Hadith - Bukhari)",
        "Do not look down upon any Muslim. (Hadith - Muslim)",
        "The best among you are those who are best to their families. (Hadith - Tirmidhi)",
        "The one who guides to good will be rewarded equally. (Hadith - Muslim)",
        "Paradise lies at the feet of your mother. (Hadith - Ahmad)",
        "The best wealth is contentment of the heart. (Hadith - Ibn Majah)",
        "Allah does not look at your appearance or wealth, but at your hearts and actions. (Hadith - Muslim)",
        "Whoever fasts Ramadan with faith and seeking reward will have his past sins forgiven. (Hadith - Bukhari)",
        "The best of you are those who learn the Qur'an and teach it. (Hadith - Bukhari)",
        "Keep yourselves far from envy, for it eats up and takes away good actions. (Hadith - Abu Dawood)",
        "The believer does not taunt, curse, abuse, or talk indecently. (Hadith - Tirmidhi)",
        "The most beloved deed to Allah is to make a Muslim happy. (Hadith - Tabarani)",
        "Fear Allah wherever you are. (Hadith - Tirmidhi)",
        "Give charity without delay, for it stands in the way of calamity. (Hadith - Tirmidhi)",
        "Love for your brother what you love for yourself. (Hadith - Bukhari)",
        "He who does not thank people, does not thank Allah. (Hadith - Ahmad)",
        "A true Muslim is the one from whom others are safe. (Hadith - Bukhari)",
        "A man's true wealth is the good he does in this world. (Hadith - Bukhari)",
        "The most perfect believer in faith is the one whose character is finest. (Hadith - Abu Dawood)",
        "Do not be people without minds of your own, saying that if others treat you well you will treat them well. (Hadith - Al-Tirmidhi)",
        "The best of you are those who are best to their families. (Hadith - Tirmidhi)",
        "The best among you are those who are best to their wives. (Hadith - Tirmidhi)",
        "A believer does not taunt, curse, abuse, or talk indecently. (Hadith - Tirmidhi)",
        "Do not turn away a poor man...even if all you can give is half a date. (Hadith - Al-Bukhari)",
        "The deeds most loved by Allah are those done regularly, even if they are small. (Hadith - Bukhari)",
        "He who does not thank people, does not thank Allah. (Hadith - Ahmad)",
        "The best among you are those who are best to their families. (Hadith - Tirmidhi)",
        "Do not be people without minds of your own, saying that if others treat you well you will treat them well. (Hadith - Al-Tirmidhi)",
        "A believer does not taunt, curse, abuse, or talk indecently. (Hadith - Tirmidhi)",
        "Do not turn away a poor man...even if all you can give is half a date. (Hadith - Al-Bukhari)",
        "The deeds most loved by Allah are those done regularly, even if they are small. (Hadith - Bukhari)",
        "Whoever gives up something for the sake of Allah, Allah will compensate him with something better. (Hadith - Ahmad)",
        "Allah is beautiful and He loves beauty. (Hadith - Muslim)",
        "Kindness is a mark of faith. (Hadith - Muslim)",
        "The reward of deeds depends upon the intentions. (Hadith - Bukhari)",
        "Patience is the key to paradise. (Hadith - Tirmidhi)",
        "Forgive, and you will be forgiven. (Hadith - Ibn Majah)",
        "Whoever believes in Allah and the Last Day, let him speak good or remain silent. (Hadith - Bukhari)",
        "Do not look down upon any Muslim. (Hadith - Muslim)",
        "The best among you are those who are best to their families. (Hadith - Tirmidhi)",
        "The one who guides to good will be rewarded equally. (Hadith - Muslim)",
        "Paradise lies at the feet of your mother. (Hadith - Ahmad)",
        "The best wealth is contentment of the heart. (Hadith - Ibn Majah)",
        "Allah does not look at your appearance or wealth, but at your hearts and actions. (Hadith - Muslim)",
        "Whoever fasts Ramadan with faith and seeking reward will have his past sins forgiven. (Hadith - Bukhari)",
        "The best of you are those who learn the Qur'an and teach it. (Hadith - Bukhari)",
        "Keep yourselves far from envy, for it eats up and takes away good actions. (Hadith - Abu Dawood)",
        "The believer does not taunt, curse, abuse, or talk indecently. (Hadith - Tirmidhi)",
        "The most beloved deed to Allah is to make a Muslim happy. (Hadith - Tabarani)",
    )



    private fun getRandomHadith(): String {
        // Generate a random index to select a random hadith from the array
        val randomIndex = Random().nextInt(hadiths.size)
        return hadiths[randomIndex]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        currentlocation = view.findViewById(R.id.currentlocation)
        qiabaImage = view.findViewById(R.id.qiabla_button)
        tasbeehImage = view.findViewById(R.id.Tasbeeh_button)
        JamatIcon = view.findViewById(R.id.jamat_time_icon)

        // Get current date and day of the week
        val currentDate = getCurrentDate()
        val currentDayOfWeek = getCurrentDayOfWeek()

        // Display current date and day of the week
        val currentDateTextView: TextView = view.findViewById(R.id.currentDate)
        val currentBarTextView: TextView = view.findViewById(R.id.currentbar)
        currentDateTextView.text = "$currentDate"
        currentBarTextView.text = "$currentDayOfWeek"

        val textView9 = view.findViewById<TextView>(R.id.textView9)

        // Set a random hadith to the TextView
        textView9.text = getRandomHadith()

        qiabaImage.setOnClickListener {
            val intent = Intent(requireContext(), CompassLayout::class.java)
            startActivity(intent)
        }
        tasbeehImage.setOnClickListener {
            val intent = Intent(requireContext(), Tasbeeh::class.java)
            startActivity(intent)
        }

        JamatIcon.setOnClickListener {
            val intent = Intent(requireContext(), Show_Jamat_Time::class.java)
            startActivity(intent)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Call CurrentLocation to update latitude and longitude
        CurrentLocation()
    }

    private fun getCurrentDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    private fun getCurrentDayOfWeek(): String {
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> ""
        }
    }

    private fun CurrentLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(requireActivity()) { task->
                    val location: Location? = task.result
                    if (location == null) {
                        Toast.makeText(requireContext(), "System not working", Toast.LENGTH_SHORT).show()
                    } else {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        try {
                            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            if (addresses != null && addresses.isNotEmpty()) {
                                val cityName = addresses[0].locality
                                val countryName = addresses[0].countryName
                                val areaName = addresses[0].subLocality
                                val locationName = "$areaName, $cityName, $countryName"
                                currentlocation?.text = locationName
                            } else {
                                currentlocation?.text = "Location not found"
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                            currentlocation?.text = "Error: ${e.message}"
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_ACCESS_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "Granted", Toast.LENGTH_SHORT).show()
                CurrentLocation()
            } else {
                Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }
}
