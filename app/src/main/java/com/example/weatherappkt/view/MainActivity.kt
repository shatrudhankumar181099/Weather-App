package com.example.weatherappkt.view

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.weatherappkt.R
import com.example.weatherappkt.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"

 class MainActivity : AppCompatActivity(){
   private lateinit var viewModel: MainViewModel

    //needed
    private lateinit var GET: SharedPreferences
     private lateinit var SET: SharedPreferences.Editor


     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_main)

         GET = getSharedPreferences(packageName, MODE_PRIVATE)
         SET = GET.edit()

         viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

         val cName = GET.getString("cityName", "Mumbai")
         edt_city_name.setText(cName)
         viewModel.refreshData(cName!!)

         getLiveData()

         swipe_refresh_layout.setOnRefreshListener {
             ll_data.visibility = View.GONE
             tv_error.visibility = View.GONE
             pb_loading.visibility = View.GONE

             val cityName = GET.getString("cityName", cName)
             edt_city_name.setText(cityName)
             viewModel.refreshData(cityName!!)
             swipe_refresh_layout.isRefreshing = false
         }

         img_search_city.setOnClickListener {
             val cityName = edt_city_name.text.toString()
             SET.putString("cityName", cityName)
             SET.apply()
             viewModel.refreshData(cityName)
             getLiveData()
             Log.i(TAG, "onCreate: " + cityName)
         }
     }
     private fun getLiveData() {
        viewModel.weather_data.observe(this) { data ->
            data?.let {
                ll_data.visibility = View.VISIBLE
                pb_loading.visibility = View.GONE
                Glide.with(this)
                    .load("http://openweathermap.org/img/wn/" + data.weather.get(0).icon + "@2x.png")
                    .into(img_weather_pictures)

                tv_degree.text = data.main.temp.toString() + "°C"
                tv_city_code.text = data.sys.country.toString()
                tv_city_name.text = data.name.toString()
                tv_humidity.text =  data.main.humidity.toString() + "%"
                tv_wind_speed.text = data.wind.speed.toString() + "km/h"
                tv_lat.text = data.coord.lat.toString() + "°N"
                tv_lon.text = data.coord.lon.toString() +"°E"

            }
        }
         viewModel.weather_error.observe(this) { error ->
             error?.let {
                 if (error) {
                     tv_error.visibility = View.VISIBLE
                     pb_loading.visibility = View.GONE
                     ll_data.visibility = View.GONE
                 } else {
                     tv_error.visibility = View.GONE
                 }
             }
         }

         viewModel.weather_loading.observe(this) { loading ->
            loading?.let {
                if (loading) {
                    pb_loading.visibility = View.VISIBLE
                    tv_error.visibility = View.GONE
                    ll_data.visibility = View.GONE
                } else {
                    pb_loading.visibility = View.GONE
                }
            }
        }
     }
}

