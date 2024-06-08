package pl.borawski.mojagdynia

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import pl.borawski.mojagdynia.databinding.ActivityBeachBinding

class BeachActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityBeachBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBeachBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_beach)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }

        val dbHelper = DatabaseHelper(context = this)
        // Odczytywanie danych
        val data = dbHelper.readData(this)
        var textView = findViewById<TextView>(R.id.textview_first)
        data.forEach {
            var text = textView.text.toString()
            val row = "ID: ${it.id}, Name: ${it.name}, Latitude: ${it.latitude}, Longitude: ${it.longitude}, IsFree: ${it.isFree} \n"
            text = text + row
            textView.text = text
            println(row)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_beach)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}