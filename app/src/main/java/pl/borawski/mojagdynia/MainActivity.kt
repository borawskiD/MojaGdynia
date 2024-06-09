package pl.borawski.mojagdynia

import DatabaseHelper
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

        val beachesButton = findViewById<Button>(R.id.beachesButton)

        beachesButton.setOnClickListener {
            val intent = Intent(this, BeachActivity::class.java)
            startActivity(intent)
        }

        val aboutButton = findViewById<Button>(R.id.aboutButton)
        aboutButton.setOnClickListener {
            val intent = Intent(this, AboutCityActivity::class.java)
            startActivity(intent)
        }

        val calculatorButton = findViewById<Button>(R.id.ticketButton)
        calculatorButton.setOnClickListener {
            val intent = Intent(this, TicketCalculatorActivity::class.java)
            startActivity(intent)
        }

        val contactButton = findViewById<Button>(R.id.contactButton)
        contactButton.setOnClickListener {
            val intent = Intent(this, ContactActivity::class.java)
            startActivity(intent)
        }


        val dbHelper = DatabaseHelper(context = this)
        dbHelper.clearAndPopulateDatabase()

    }

}