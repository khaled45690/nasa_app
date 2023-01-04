package com.example.nasa_app.repository

import com.example.nasa_app.api.NasaAPI
import com.example.nasa_app.api.parseAsteroidsJsonResult
import com.example.nasa_app.database.NasaData
import com.example.nasa_app.database.NasaDatabase
import com.example.nasa_app.database.NasaDatabaseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Response
import retrofit2.await
import retrofit2.awaitResponse
import java.time.LocalDate


class Repository(val database: NasaDatabaseDao) {

   suspend fun refreshDatabaseRecords(){
       withContext(Dispatchers.IO){
           val startDate = LocalDate.now();
           val endDate = LocalDate.now().plusDays(7)
           val response : Response<String> = NasaAPI.retrofitService.getRecords(startDate.toString() ,endDate.toString()).awaitResponse()
           if (response.isSuccessful){
               val responseJson = JSONObject(response.body())
               val asteroidList : List<NasaData> = parseAsteroidsJsonResult(responseJson)
               database.insert(asteroidList)
           }
       }
    }
}