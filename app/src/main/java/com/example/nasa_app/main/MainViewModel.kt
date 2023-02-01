package com.example.nasa_app.main

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.nasa_app.R
import com.example.nasa_app.api.NasaAPI
import com.example.nasa_app.api.parseAsteroidsJsonResult
import com.example.nasa_app.database.NasaData
import com.example.nasa_app.database.NasaDatabase
import com.example.nasa_app.databinding.FragmentMainBinding
import com.example.nasa_app.worker.RefreshDataWorker
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.*
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class MainViewModel : ViewModel() {


    private var _nasaRecordsList:MutableLiveData<List<NasaData>>  = MutableLiveData<List<NasaData>>().apply{
        value = listOf()
    }



    val nasaRecordedList : LiveData<List<NasaData>> get() = _nasaRecordsList

     fun getToDayImage(binder : FragmentMainBinding) {
         NasaAPI.retrofitService.getImageData().enqueue( object:
            Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {

            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                val dataList  = JSONObject(response.body())

                binder.activityMainImageOfTheDay.contentDescription = dataList["explanation"].toString()
                binder.activityMainImageOfTheDay
                Picasso.with(binder.root.context)
                    .load(Uri.parse(dataList["url"] as String?))
                    .fit()
                    .into(binder.activityMainImageOfTheDay)
            }
        })
    }

// this is used to load data as soon as possible
    fun getCachedData( binder : FragmentMainBinding){
        val dataSource = NasaDatabase.getInstance(binder.root.context).NasaDatabaseDao

        val job: Job = Job()
        val uiScope = CoroutineScope( job)
        uiScope.launch {
            val data : List<NasaData> = dataSource.getAllRecords()
            _nasaRecordsList.postValue(data)
            getNextSevenDaysData(binder)

        }
    }
    // this is used update the data in database and then update it on screen
    private fun getNextSevenDaysData(binder : FragmentMainBinding){
        val startDate = LocalDate.now();
        val endDate = LocalDate.now().plusDays(7)
        val dataSource = NasaDatabase.getInstance(binder.root.context).NasaDatabaseDao

        val job: Job = Job()
        val uiScope = CoroutineScope( job)
        NasaAPI.retrofitService.getRecords(startDate.toString() , endDate.toString()).enqueue( object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("Failure: " + t.message)

            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                val dataList : List<NasaData> = parseAsteroidsJsonResult(JSONObject(response.body()!!))

                uiScope.launch {
                    dataSource.insert(dataList)
                    dataSource.clearOutDatedData(LocalDate.now().toString())
                    _nasaRecordsList.postValue(listOf())
                    _nasaRecordsList.postValue(dataSource.getAllRecords())
                }


            }
        })
    }
    // this is used to filter data according to the selected filtration
    // please be noted that in week selection it will start from tomorrow to be different than show saved data
    // it's just for visual demonstration to see UI update with different values
    fun filterData(binder: FragmentMainBinding, itemId: Int ){
        val startDate = LocalDate.now();
        val endDate = LocalDate.now().plusDays(7)
        val dataSource = NasaDatabase.getInstance(binder.root.context).NasaDatabaseDao

        val job: Job = Job()
        val uiScope = CoroutineScope( job)

        uiScope.launch {
            dataSource.clearOutDatedData(LocalDate.now().toString())
            when(itemId){
                R.id.show_week_menu -> _nasaRecordsList.postValue(dataSource.getWeekRecords(startDate.toString() , endDate.toString()))
                R.id.show_today_menu -> _nasaRecordsList.postValue(dataSource.getTodayRecords(startDate.toString()))
                R.id.show_saved_menu ->  _nasaRecordsList.postValue(dataSource.getAllRecords())
            }


        }
        }


// i managed to follow the tutorial but i can't test it to see if it works or no
    fun setupWorker(context: Context){

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .apply {
                setRequiresDeviceIdle(true)
            }.build()


        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(2, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()


        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            RefreshDataWorker.refreshDatabaseRecords,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest)
    }
    }



