/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.nasa_app.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import java.util.*

/**
 * Defines methods for using the SleepNight class with Room.
 */
@Dao
interface NasaDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(record: List<NasaData>)




    @Update
    suspend fun update(record: NasaData)

    @Query("SELECT * from nasa_table WHERE recordId = :key")
    suspend fun getRecordWithId(key: Long): NasaData

    @Query("DELETE from nasa_table")
    suspend fun clear()


    @Query("SELECT * from nasa_table ORDER BY closeApproachDate ASC")
    suspend fun getAllRecords(): List<NasaData>

    @Query("SELECT * from nasa_table  WHERE closeApproachDate > :currentDate AND closeApproachDate < :lastDate ORDER BY closeApproachDate ASC")
    suspend fun getWeekRecords(currentDate: String ,lastDate: String): List<NasaData>

    @Query("SELECT * from nasa_table WHERE closeApproachDate == :currentDate ORDER BY closeApproachDate ASC")
    suspend fun getTodayRecords(currentDate: String): List<NasaData>

    @Query("DELETE from nasa_table WHERE closeApproachDate < :currentDate")
    suspend fun clearOutDatedData(currentDate: String)
}

