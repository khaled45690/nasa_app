package com.example.nasa_app.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.nasa_app.api.NasaAPI
import com.example.nasa_app.database.NasaDatabase
import com.example.nasa_app.repository.Repository
import retrofit2.HttpException
import retrofit2.await
import java.time.LocalDate

class RefreshDataWorker(private val workerContext: Context, private val  workerParameters: WorkerParameters) : CoroutineWorker(workerContext , workerParameters) {
    companion object {
        const val refreshDatabaseRecords = "refreshDatabaseRecords"
    }
    override suspend fun doWork(): Result {
        val dataSource = NasaDatabase.getInstance(workerContext).NasaDatabaseDao
        val repository = Repository(dataSource)
        return try {
            repository.refreshDatabaseRecords()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}