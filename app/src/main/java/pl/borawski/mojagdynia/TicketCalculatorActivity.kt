package pl.borawski.mojagdynia

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TicketCalculatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ticket_calculator)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    companion object TicketPrices{
        val shortTerm  = 4.8
        val longTerm = 6.0
    }

    fun calcPrice(view: View){
        val shortTermFull = findViewById<EditText>(R.id.oneTimeFullEdit).text.toString()
        val shortTermDiscount = findViewById<EditText>(R.id.oneTimeDiscEdit).text.toString()
        val longTermFull = findViewById<EditText>(R.id.longTimeFullEdit).text.toString()
        val longTermDiscount = findViewById<EditText>(R.id.longTimeDiscEdit).text.toString()
        val shortTermFullAmount = if (shortTermFull.isEmpty()) 0 else shortTermFull.toInt()
        val shortTermDiscountAmount = if (shortTermDiscount.isEmpty()) 0 else shortTermDiscount.toInt()
        val longTermFullAmount = if (longTermFull.isEmpty()) 0 else longTermFull.toInt()
        val longTermDiscountAmount = if (longTermDiscount.isEmpty()) 0 else longTermDiscount.toInt()
        val sum = shortTerm * shortTermFullAmount + 0.5 * shortTerm * shortTermDiscountAmount + longTerm * longTermFullAmount + longTerm * 0.5 * longTermDiscountAmount
        val score = findViewById<TextView>(R.id.priceScore)
        score.text = "Cena: $sum złotych"
    }
}