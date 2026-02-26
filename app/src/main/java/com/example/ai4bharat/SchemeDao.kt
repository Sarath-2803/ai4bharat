package com.example.ai4bharat

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy

@Dao
interface SchemeDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertScheme(scheme: Scheme)

    @Query("SELECT * FROM schemes")
    suspend fun getAllSchemes(): List<Scheme>

    @Query("SELECT * FROM schemes WHERE state = :state")
    suspend fun getSchemesByState(state: String): List<Scheme>

    @Query("SELECT * FROM schemes WHERE name LIKE '%' || :query || '%'")
    suspend fun searchSchemes(query: String): List<Scheme>

    @Query("SELECT * FROM schemes WHERE state = :state OR state = 'All'")
    suspend fun getSchemesForRegion(state: String): List<Scheme>
}