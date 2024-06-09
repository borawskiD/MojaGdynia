package pl.borawski.mojagdynia

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.location.Location
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.OnSuccessListener

class MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button = findViewById<Button>(R.id.beachesButton)

        button.setOnClickListener {
            val intent = Intent(this, BeachActivity::class.java)
            startActivity(intent)
        }
        // Wstawianie przykładowych danych
        val dbHelper = DatabaseHelper(context = this)
        dbHelper.clearAndPopulateDatabase()
        //dbHelper.insertData(this, "Plaża 1", "Adres 1",54.5189, 18.5305, true)
        //dbHelper.insertData(this, "Plaża 2", "Adres 2",54.5010, 18.5498, false)

    }
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }


}