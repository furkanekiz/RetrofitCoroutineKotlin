package com.furkanekiz.retrofitcoroutinekotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.furkanekiz.retrofitcoroutinekotlin.R
import com.furkanekiz.retrofitcoroutinekotlin.adapter.AdapterCrypto
import com.furkanekiz.retrofitcoroutinekotlin.model.CryptoModel
import com.furkanekiz.retrofitcoroutinekotlin.service.CryptoAPI
import kotlinx.android.synthetic.main.ac_main.*
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ACMain : AppCompatActivity(), AdapterCrypto.Listener {

    private val BASE_URL = "https://api.github.com/"
    private var cryptoModels: ArrayList<CryptoModel>? = null
    private var adapterCrypto: AdapterCrypto? = null

    private var job : Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ac_main)

        rvCrypto.layoutManager = LinearLayoutManager(this)

        loadData()
    }

    private fun loadData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build().
            create(CryptoAPI::class.java)

        job = CoroutineScope(Dispatchers.IO).launch {

            val response = retrofit.getData()

            withContext(Dispatchers.Main){
                if(response.isSuccessful) {
                    response.body()?.let {
                        cryptoModels = ArrayList(it)
                        cryptoModels?.let {
                            adapterCrypto = AdapterCrypto(it,this@ACMain)
                            rvCrypto.adapter = adapterCrypto
                        }
                    }
                }
            }
        }

        /*
        val call = retrofit.getData()

        call.enqueue(object : Callback<List<CryptoModel>> {
            override fun onResponse(call: Call<List<CryptoModel>>, response: Response<List<CryptoModel>>) {
                if (response.isSuccessful){
                    response.body()?.let  {it ->
                        cryptoModels = ArrayList(it)

                        cryptoModels?.let {
                            adapterCrypto = AdapterCrypto(it,this@ACMain)
                            rvCrypto.adapter=adapterCrypto
                        }

                    }
                }
            }


            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })

         */
    }

    override fun onItemClicked(cryptoModel: CryptoModel) {
        Toast.makeText(this, cryptoModel.currency, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

}