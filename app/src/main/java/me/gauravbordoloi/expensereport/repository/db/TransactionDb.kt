package me.gauravbordoloi.expensereport.repository.db

import android.content.Context
import androidx.room.*
import me.gauravbordoloi.expensereport.transaction.Transaction

@Database(entities = [Transaction::class], version = 1, exportSchema = false)
abstract class TransactionDb : RoomDatabase() {

    companion object {

        private const val DB_NAME = "expensereport"

        @Volatile
        private var instance: TransactionDb? = null

        fun getInstance(context: Context): TransactionDb = instance ?: synchronized(this) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, TransactionDb::class.java, DB_NAME)
                .fallbackToDestructiveMigration().build()

    }

    abstract fun dao(): Dao

}