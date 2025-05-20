package com.example.appghichu.fragments.news

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.appghichu.R
import com.example.appghichu.model.Cloud
import com.example.appghichu.model.Statuswt
import com.example.appghichu.model.Weather
import com.example.appghichu.network.api.CallApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class WeatherActivity : AppCompatActivity() {
    var tvhanoi : TextView? = null
    var tvstatus : TextView? = null
    var tvnhietdoC : TextView? = null
    var imgthoitiet : ImageView? = null
    var tvdoam : TextView? = null
    var tvtocdogio : TextView? = null
    var tvdate : TextView? = null
    var tvtimedate : TextView? = null
    var tvchisodammay : TextView? = null
    var tvkhongkhi : TextView? = null
    var imgmenu : ImageView? = null
    var btnxacnhan : Button? = null
    var imgback : ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather)

        imgthoitiet = findViewById(R.id.img_weather)
        tvstatus = findViewById(R.id.tv_status)
        tvnhietdoC = findViewById(R.id.tv_nhietdoC)
        tvdoam = findViewById(R.id.tv_doamphantram)
        tvtocdogio = findViewById(R.id.tv_windspeed)
        tvdate = findViewById(R.id.tv_datehomnay)
        tvhanoi = findViewById(R.id.tv_hanoi)
        tvtimedate = findViewById(R.id.tv_datetime)
        tvchisodammay = findViewById(R.id.tv_chisodammay)
        tvkhongkhi = findViewById(R.id.tv_khongkhi)
        imgmenu = findViewById(R.id.img_menu)
        imgback = findViewById(R.id.img_backto)


        imgback?.setOnClickListener {
            finish()
        }
        val apiservice1 = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://api.openweathermap.org")
            .build()
            .create(CallApi::class.java)
        val data1 = apiservice1.getWeather("hanoi", "80f625de3b383de0c8bda765086dfd73", "metric")
        data1.enqueue(object : Callback<Weather>{
            override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                Toast.makeText(this@WeatherActivity, "success", Toast.LENGTH_SHORT).show()
                val weather = response.body()
                val cloud : Cloud? = weather?.clouds
                val date : Long? = weather?.dt
                val main = weather?.main
                val wind = weather?.wind
                val array : ArrayList<Statuswt>? = weather?.weather
                val timezone : Long? = weather?.timezone
                array?.forEach {
                    tvstatus?.text = it.description
                    val icon : String = it.icon
                    Log.d("icon", icon)
                    val requestOptions : RequestOptions = RequestOptions().centerCrop()
                    Glide.with(this@WeatherActivity)
                        .load("https://openweathermap.org/img/wn/$icon.png")
                        .into(imgthoitiet!!)
                }
                tvnhietdoC?.text = main?.temp.toString()
                tvdoam?.text = main?.humidity.toString()
                tvtocdogio?.text = wind?.speed.toString()
                tvhanoi?.text = weather?.name
                val datestring : String = android.text.format.DateFormat.format("MM/dd/yyyy", Date(date!! * 1000)).toString()
                tvdate?.text = datestring
                tvchisodammay?.text = cloud?.all.toString()
                tvkhongkhi?.text = wind?.deg.toString()
                val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
                simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
                val timenow : String = simpleDateFormat.format(timezone)
                tvtimedate?.text = timenow
            }

            override fun onFailure(call: Call<Weather>, t: Throwable) {
                Toast.makeText(this@WeatherActivity, "Failed", Toast.LENGTH_SHORT).show()
            }

        })
        imgmenu?.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.city)
            btnxacnhan = dialog.findViewById(R.id.btn_xacnhancity)
            val edtcity : EditText = dialog.findViewById(R.id.edt_city)
            dialog.show()
            btnxacnhan?.setOnClickListener {
                val apiservice = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://api.openweathermap.org")
                    .build()
                    .create(CallApi::class.java)
                val city : String = edtcity.text.toString()
                Log.e("hieu", city)
                val data = apiservice.getWeather(city, "80f625de3b383de0c8bda765086dfd73", "metric")
                data.enqueue(object : Callback<Weather> {
                    @SuppressLint("SimpleDateFormat")
                    override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                        val weather = response.body()
                        val cloud : Cloud? = weather?.clouds
                        val date : Long? = weather?.dt
                        val main = weather?.main
                        val wind = weather?.wind
                        val array : ArrayList<Statuswt>? = weather?.weather
                        val timezone : Long? = weather?.timezone
                        array?.forEach {
                            tvstatus?.text = it.description
                            val icon : String = it.icon
                            val requestOptions : RequestOptions = RequestOptions().centerCrop()
                            Glide.with(this@WeatherActivity).load("https://openweathermap.org/img/wn/$icon.png").apply(requestOptions).into(imgthoitiet!!)
                        }
                        tvnhietdoC?.text = main?.temp.toString()
                        tvdoam?.text = main?.humidity.toString()
                        tvtocdogio?.text = wind?.speed.toString()
                        tvhanoi?.text = weather?.name
//                        val datestring : String = android.text.format.DateFormat.format("MM/dd/yyyy", Date(date!! * 1000)).toString()
//                        tvdate?.text = datestring
                        tvchisodammay?.text = cloud?.all.toString()
                        tvkhongkhi?.text = wind?.deg.toString()
//                        val simpleDateFormat = SimpleDateFormat("hh:mm:ss")
//                        val timenow : String = simpleDateFormat.format(timezone)
//                        tvtimedate?.text = timenow
                    }

                    override fun onFailure(call: Call<Weather>, t: Throwable) {
                        Toast.makeText(this@WeatherActivity, "Failed", Toast.LENGTH_SHORT).show()
                    }

                })
                dialog.dismiss()
            }
        }
    }
}