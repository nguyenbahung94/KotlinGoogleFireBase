package com.example.asus.myapplication

import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.IOException
import java.util.*

/**
 * Created by asus on 9/7/17.
 */
class MapUserFragment : Fragment(), OnMapReadyCallback {
    val LOCATION_UPDATE_MIN_DISTANCE: Float = 1f
    val LOCATION_UPDATE_MIN_TIME: Long = 500
    private lateinit var database: DatabaseReference
    private lateinit var mMap: GoogleMap
    private var location: Location? = null
    private var locationManager: LocationManager? = null
    private var locationListener: LocationListener? = null
    private lateinit var prefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.map_user, container, false)
        locationManager = activity.getSystemService(android.content.Context.LOCATION_SERVICE) as LocationManager
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment).getMapAsync(this)
        database = FirebaseDatabase.getInstance().reference
        prefs = activity.getSharedPreferences(common.PREFS_FILENAME, 0)
        return view
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        // Add a marker in Sydney and move the camera

        // khi map khởi tạo xong ,sẵn sang để sử dụng thì mới check network
        //nếu như network ko mở thì nó sẽ lấy = gps
        if (Utils.isNetWorkConnnected(activity)) {
            if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return
            }
            location = locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            if (location != null)
                updateLocation(location!!)

        } else {
            if (checkGps()) {
                location = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location != null)
                    updateLocation(location!!)

            }
        }
        eventUpdateLocation()


    }

    fun checkGps(): Boolean {
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    fun updateLocation(locationMap: Location) {
        val c = Calendar.getInstance()
        val day: Int = c.get(Calendar.DAY_OF_MONTH)
        val month: Int = c.get(Calendar.MONTH)
        val year: Int = c.get(Calendar.YEAR)
        val key: String = day.toString() + (month + 1) + year.toString()
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(locationMap.latitude, locationMap.longitude), 15f))
        val latlng: LatLng = LatLng(locationMap.latitude, locationMap.longitude)
        val str: String = convertAddr(latlng)
        var user = AddLocation(locationMap.latitude, locationMap.longitude, str)
        Log.e("addddđ", locationMap.latitude.toString())
        mMap.addMarker(MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.location)).title(str).position(LatLng(locationMap.latitude, locationMap.longitude)))

        var keyUser: String = prefs.getString(common.KEY, "")
        Log.e("keyUser", keyUser)
        database.child("listlocation").child(keyUser).child("location").child(key).push().setValue(user)


    }

    fun convertAddr(lat: LatLng): String {
        var geocoder = Geocoder(activity, Locale.getDefault())

        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(lat.latitude, lat.longitude, 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var streetAddress: String = ""
        if (addresses != null) {
            val returnedAddress = addresses[0]
//            val strReturnedAddress = StringBuilder()
            streetAddress = returnedAddress.getAddressLine(0)

        }
        Log.e("add", streetAddress)
        return streetAddress
    }


    fun eventUpdateLocation() {
        locationListener = object : LocationListener {
            override fun onLocationChanged(p0: Location?) {
                if (p0 != null) {
                    updateLocation(p0)
                }

            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {

            }

            override fun onProviderEnabled(p0: String?) {
            }

            override fun onProviderDisabled(p0: String?) {
            }

        }
        if (ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, locationListener)

    }

    override fun onPause() {
        super.onPause()
        if (locationListener != null) locationManager!!.removeUpdates(locationListener)

    }
}