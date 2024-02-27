package br.com.menezesmarlon.conversormonetrio

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.menezesmarlon.conversormonetrio.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        runGif()

        binding.buttonConvert.setOnClickListener {
            converter()
        }
    }

    private fun runGif() {
        Glide.with(this)
            .load(R.drawable.coin)
            .apply(RequestOptions())
            .into(binding.imgCoin)
    }

    private fun converter() {
        val currency = when (binding.radioGroup.checkedRadioButtonId) {
            R.id.radioUSD -> "USD"
            R.id.radioEUR -> "EUR"
            else -> "CLP"
        }
        val value = binding.editValue.text.toString()
        if (value.isEmpty())
            return
        Thread {
            val url =
                URL("https://api.currencyapi.com/v3/latest?apikey=cur_live_mhlD3D56hBbGDK0EOPWYNUCFp82VRC2JzrlANIje&base_currency=$currency&currencies=BRL")
            val conn = url.openConnection() as HttpsURLConnection
            try {
                val data = conn.inputStream.bufferedReader().readText()
                val obj = JSONObject(data)
                runOnUiThread {
                    val res = obj.getJSONObject("data")
                        .getJSONObject("BRL")
                        .getDouble("value")

                    binding.textResult.visibility = View.VISIBLE
                    binding.textResult.text = "R$: ${"%.2f".format(value.toDouble() * res)}"

                }

            } finally {
                conn.disconnect()
            }


        }.start()

    }
}
