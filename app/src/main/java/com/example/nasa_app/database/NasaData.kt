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

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nasa_table")
data class NasaData(
        @PrimaryKey(autoGenerate = true)
        var recordId: Long = 0L,

        @ColumnInfo(name = "codename")
        val codename: String,

        @ColumnInfo(name = "closeApproachDate")
        var closeApproachDate: String,

        @ColumnInfo(name = "absoluteMagnitude")
        var absoluteMagnitude: Double,

        @ColumnInfo(name = "estimatedDiameter")
        var estimatedDiameter: Double,
        @ColumnInfo(name = "relativeVelocity")
        var relativeVelocity: Double,
        @ColumnInfo(name = "distanceFromEarth")
        var distanceFromEarth: Double,
        @ColumnInfo(name = "isPotentiallyHazardous")
        var isPotentiallyHazardous: Boolean
): Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readLong(),
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readDouble(),
                parcel.readDouble(),
                parcel.readDouble(),
                parcel.readDouble(),
                parcel.readByte() != 0.toByte()
        ) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeLong(recordId)
                parcel.writeString(codename)
                parcel.writeString(closeApproachDate)
                parcel.writeDouble(absoluteMagnitude)
                parcel.writeDouble(estimatedDiameter)
                parcel.writeDouble(relativeVelocity)
                parcel.writeDouble(distanceFromEarth)
                parcel.writeByte(if (isPotentiallyHazardous) 1 else 0)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<NasaData> {
                override fun createFromParcel(parcel: Parcel): NasaData {
                        return NasaData(parcel)
                }

                override fun newArray(size: Int): Array<NasaData?> {
                        return arrayOfNulls(size)
                }
        }
}