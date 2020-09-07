package com.kimbrelk.android.hamlogger.data.dao

import androidx.room.*

@Dao
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(obj: T) : Long

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertOrFail(obj: T) : Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(obj: List<T>) : List<Long>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertOrFail(obj: List<T>) : List<Long>

    @Update
    suspend fun update(obj: T)

    @Update
    suspend fun update(obj: List<T>)

    @Delete
    suspend fun delete(obj: T)

}