package me.gauravbordoloi.expensereport.repository.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import me.gauravbordoloi.expensereport.transaction.Transaction

@Dao
interface Dao {

    @Query("SELECT * FROM `transaction` ORDER BY date ASC")
    fun getTransactions(): List<Transaction>?

    @Query("SELECT * FROM `transaction` WHERE type = :type ORDER BY date ASC")
    fun getFilteredTransactions(type: String): List<Transaction>?

    @Query("SELECT * FROM `transaction` WHERE tag IS NOT NULL AND tag != '' ORDER BY date ASC")
    fun getTransactionsWithTag(): List<Transaction>?

    @Query("SELECT tag FROM `transaction` WHERE tag IS NOT NULL AND tag != '' ORDER BY date ASC")
    fun getAllTags(): List<String>?

    @Insert(onConflict = IGNORE)
    fun setTransactions(list: List<Transaction>?)

    @Insert(onConflict = REPLACE)
    fun setTransaction(transaction: Transaction?)

    @Delete
    fun delete(vararg list: Transaction?)

}