package com.example.islamichub

import android.content.Context
import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import java.lang.Exception
import java.text.SimpleDateFormat
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class All_Namaz_Time : Fragment() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    var fajr: TextView? = null
    var duhr: TextView? = null
    var asar: TextView? = null
    var maghrib: TextView? = null
    var isha: TextView? = null
    var sunrise: TextView? = null
    var sunset: TextView? = null
    var latitude: Double? = null
    var longitude: Double? = null // Changed from TextView to Double
    var loctionname: TextView? = null

    var searchEditText: AppCompatEditText? = null
    var Searchbtn: ImageView? = null
    var simpleDateFormat = SimpleDateFormat("hh:mm")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_all__namaz__time, container, false)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        loctionname = view.findViewById(R.id.location)
        fajr = view.findViewById(R.id.get_fajr)
        sunrise = view.findViewById(R.id.sunrise)
        duhr = view.findViewById(R.id.get_dhur)
        asar = view.findViewById(R.id.get_asar)
        sunset = view.findViewById(R.id.sunset)
        maghrib = view.findViewById(R.id.get_maghrib)
        isha = view.findViewById(R.id.get_isha)
        searchEditText = view.findViewById(R.id.Searchcity)
        Searchbtn = view.findViewById(R.id.searchbtn)
        CurrentLocation()


        Searchbtn?.setOnClickListener {
            LoadPrayerTime()
        }

        return view
    }

    private fun CurrentLocation() {

        if(checkPermission())
        {
            if(isLocationEnabled())
            {
                //final Latitude and longitude code here
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(requireActivity()) { task->
                    val location: Location? = task.result
                    if (location == null) {
                        Toast.makeText(requireContext(), "system not working", Toast.LENGTH_SHORT).show()
                    } else {
                        //Toast.makeText(requireContext(), "Get Success", Toast.LENGTH_SHORT).show()
                        DLoadPrayerTime(location.latitude, location.longitude)
                    }
                }


            }
            else
            {
                //setting open here
                Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)



            }
        }
        else
        {
            //request permission here
            requestPermission()
        }

    }
    private fun DLoadPrayerTime(latitude: Double?, longitude: Double?)
    {
        if (latitude != null && longitude != null) {
            val queue = Volley.newRequestQueue(requireContext())
            val url = "https://api.aladhan.com/v1/calendar/2024/2?latitude=$latitude&longitude=$longitude"
            val jsonObjectRequest = JsonObjectRequest(
                com.android.volley.Request.Method.GET, url, null,
                Response.Listener { response ->
                    try {
                        val jsonData: JSONArray = response.getJSONArray("data")
                        val timings = jsonData.getJSONObject(0)
                        val tim = timings.getJSONObject("timings")
                        fajr?.text = simpleDateFormat.format(simpleDateFormat.parse(tim.getString("Fajr"))) + " AM"
                        sunrise?.text = simpleDateFormat.format(simpleDateFormat.parse(tim.getString("Sunrise"))) + " AM"
                        duhr?.text = simpleDateFormat.format(simpleDateFormat.parse(tim.getString("Dhuhr"))) + " PM"
                        asar?.text = simpleDateFormat.format(simpleDateFormat.parse(tim.getString("Asr"))) + " PM"
                        sunset?.text = simpleDateFormat.format(simpleDateFormat.parse(tim.getString("Sunset"))) + " PM"
                        maghrib?.text = simpleDateFormat.format(simpleDateFormat.parse(tim.getString("Maghrib"))) + " PM"
                        isha?.text = simpleDateFormat.format(simpleDateFormat.parse(tim.getString("Isha"))) + " PM"
                    } catch (e: Exception) {
                        Log.e("PrayerTimeError", "Error fetching prayer times: ${e.message}", e)
                    }
                },
                { error ->
                    Log.e("PrayerTimeError", "Volley Error: ${error.message}", error)
                    // Handle Volley error here, if necessary
                })
            queue.add(jsonObjectRequest)
        } else {
            // Handle case where latitude or longitude is null
            Log.e("PrayerTimeError", "Latitude or longitude is null")
        }

    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    private fun requestPermission() {
        requestPermissions(
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_ACCESS_LOCATION
        )
    }

    companion object
    {
        private  const val PERMISSION_REQUEST_ACCESS_LOCATION = 100
    }
    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_REQUEST_ACCESS_LOCATION)
        {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(requireContext(), "Granted", Toast.LENGTH_SHORT).show()
                CurrentLocation()
            }
            else
            {
                Toast.makeText(requireContext(), "Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun LoadPrayerTime() {

        val geocoder = Geocoder(requireContext())
        val addressList: List<Address>?
        try {
            addressList = geocoder.getFromLocationName(searchEditText?.text.toString(), 5)

            if (addressList != null) {
                val searchedAddress = addressList[0]

                loctionname?.text = searchedAddress.getAddressLine(0)

                val doubleLat = addressList[0].latitude
                val doubleLone = addressList[0].longitude
                val queue = Volley.newRequestQueue(requireContext())
                val url ="https://api.aladhan.com/v1/calendar/2024/2?latitude=$doubleLat&longitude=$doubleLone"
                val jsonObjectRequest = JsonObjectRequest(
                    com.android.volley.Request.Method.GET, url, null,
                    Response.Listener { response ->

                        val jsonData: JSONArray = response.getJSONArray("data")
                        val timings = jsonData.getJSONObject(0)
                        val tim = timings.getJSONObject("timings")
                        fajr?.text = simpleDateFormat.format(simpleDateFormat.parse(tim.getString("Fajr"))) + " AM"
                        sunrise?.text = simpleDateFormat.format(simpleDateFormat.parse(tim.getString("Sunrise"))) + " AM"
                        duhr?.text = simpleDateFormat.format(simpleDateFormat.parse(tim.getString("Dhuhr"))) + " PM"
                        asar?.text = simpleDateFormat.format(simpleDateFormat.parse(tim.getString("Asr"))) + " PM"
                        sunset?.text = simpleDateFormat.format(simpleDateFormat.parse(tim.getString("Sunset"))) + " PM"
                        maghrib?.text = simpleDateFormat.format(simpleDateFormat.parse(tim.getString("Maghrib"))) + " PM"
                        isha?.text = simpleDateFormat.format(simpleDateFormat.parse(tim.getString("Isha"))) + " PM"

                    },
                    { error -> Log.d("Error", error.message!!) })
                queue.add(jsonObjectRequest)

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}