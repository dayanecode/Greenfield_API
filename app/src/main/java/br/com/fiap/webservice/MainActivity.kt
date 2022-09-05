
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.fiap.webservice.CEP
import br.com.fiap.webservice.R
import br.com.fiap.webservice.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pesquisaCEP: Button = findViewById(R.id.pesquisaCEP)
        val cep: EditText = findViewById(R.id.cep)
        val progress_bar: ProgressBar = findViewById(R.id.progress_bar)
        val rua: EditText = findViewById(R.id.rua)
        val cidade: EditText = findViewById(R.id.cidade)
        val uf: EditText = findViewById(R.id.uf)
        val pesquisaRCE: Button = findViewById(R.id.pesquisaRCE)

        //-- Ao clicar no botão número 1
        //-- Será pesquisado o logradouro com o número do CEP
        pesquisaCEP.setOnClickListener {

            progress_bar.visibility = View.VISIBLE

            val call = RetrofitFactory().retrofitService().getCEP(cep.text.toString())

            call.enqueue(object : Callback<CEP> {

                override fun onResponse(call: Call<CEP>, response: Response<CEP>) {

                    response.body()?.let {
                        Log.i("CEP", it.toString())
                        Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_LONG).show()
                        progress_bar.visibility = View.INVISIBLE
                    } ?: Toast.makeText(this@MainActivity, "CEP não localizado", Toast.LENGTH_LONG)
                        .show()

                }

                override fun onFailure(call: Call<CEP>?, t: Throwable?) {
                    t?.message?.let { it1 -> Log.e("Erro", it1) }
                    progress_bar.visibility = View.INVISIBLE
                }
            })
        }

        //-- Ao clicar no botão número 2
        //-- Será pesquisado o logradouro com os dados:
        //-- RUA, CIDADE e ENDEREÇO
        pesquisaRCE.setOnClickListener {

            progress_bar.visibility = View.VISIBLE

            val call = RetrofitFactory().retrofitService().getRCE(
                uf.text.toString(),
                cidade.text.toString(),
                rua.text.toString()
            )

            call.enqueue(object : Callback<List<CEP>> {

                override fun onResponse(call: Call<List<CEP>>?, response: Response<List<CEP>>?) {

                    response?.body()?.let {
                        Log.i("CEP", it.toString())
                        Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_LONG).show()
                        progress_bar.visibility = View.INVISIBLE
                    } ?: Toast.makeText(
                        this@MainActivity,
                        "Endereço não localizado ",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onFailure(call: Call<List<CEP>>?, t: Throwable?) {
                    t?.message?.let { it1 -> Log.e("Erro", it1) }
                    progress_bar.visibility = View.INVISIBLE
                }
            })
        }
    }
}