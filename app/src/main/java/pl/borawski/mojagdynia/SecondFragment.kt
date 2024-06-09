package pl.borawski.mojagdynia

import DatabaseHelper
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import pl.borawski.mojagdynia.databinding.FragmentFirstBinding
import pl.borawski.mojagdynia.databinding.FragmentSecondBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {
    private var _binding: FragmentSecondBinding? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var myLatitude = 0.0
    private var myLongitude = 0.0
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            getLastKnownLocation()
        }

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getLastKnownLocation() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(100)
            .setMaxUpdateDelayMillis(200)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location: Location = locationResult.lastLocation!!
                myLatitude = location.latitude
                myLongitude = location.longitude
                if (_binding != null) {
                    updateTable()
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun updateTable() {
        val dbHelper = DatabaseHelper(context = requireContext())
        val data = dbHelper.readAttractionData(requireContext())
        val tableData = mutableListOf<List<Any>>()
        val coordinates = mutableListOf<Pair<Double, Double>>()
        data.forEach {
            val distance = calculateDistance(myLatitude, myLongitude, it.latitude, it.longitude) / 1000.0 //[km]
            val free = if(it.isFree) "Tak" else "Nie"
            tableData.add(listOf(it.name, it.address, free, distance))
            coordinates.add(Pair(it.latitude, it.longitude))
        }

        val tableLayout = binding.root.findViewById<TableLayout>(R.id.attractionsTable)
        val headerRow = tableLayout.getChildAt(0) as TableRow
        tableLayout.removeAllViews()
        tableLayout.addView(headerRow)

        var counter = 0
        for (row in tableData) {
            val tableRow = TableRow(requireContext())
            for ((index, cell) in row.withIndex()) {
                val rowTextView = TextView(requireContext()).apply {
                    text = cell.toString()
                    setPadding(16, 16, 16, 16)
                    isSingleLine = false
                    ellipsize = TextUtils.TruncateAt.END
                    layoutParams = TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
                    setBackgroundResource(R.drawable.cell_border)
                    if (index == 1 && counter < coordinates.size) {
                        val currentCounter = counter
                        println("Counter: $currentCounter")
                        println(coordinates[currentCounter])
                        setOnClickListener {
                            val latitude = coordinates[currentCounter].first
                            val longitude = coordinates[currentCounter].second
                            val gmmIntentUri = Uri.parse("geo:0,0?q=${latitude},${longitude}(${Uri.encode(row[1].toString())})")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            mapIntent.setPackage("com.google.android.apps.maps")
                            if (mapIntent.resolveActivity(requireActivity().packageManager) != null) {
                                startActivity(mapIntent)
                            } else {
                                Toast.makeText(requireContext(), "Google Maps is not installed", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                tableRow.addView(rowTextView)
            }

            tableRow.layoutParams = TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
            )
            tableLayout.addView(tableRow)
            counter++
        }

        counter = 0
    }


    private fun calculateDistance(startLatitude: Double, startLongitude: Double, endLatitude: Double, endLongitude: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results)
        return results[0]
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastKnownLocation()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSecondBinding.bind(view)
        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}