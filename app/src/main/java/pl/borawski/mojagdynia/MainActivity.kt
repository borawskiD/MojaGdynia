package pl.borawski.mojagdynia

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Wstawianie przykładowych danych
        val dbHelper = DatabaseHelper(context = this)
        dbHelper.insertData(this, "Plaża 1", 54.5189, 18.5305, true)
        dbHelper.insertData(this, "Plaża 2", 54.5010, 18.5498, false)

        // Odczytywanie danych
        val data = dbHelper.readData(this)
        data.forEach {
            println("ID: ${it.id}, Name: ${it.name}, Latitude: ${it.latitude}, Longitude: ${it.longitude}, IsFree: ${it.isFree}")
        }

    }
}