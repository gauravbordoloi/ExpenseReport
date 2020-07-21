package me.gauravbordoloi.expensereport.transaction

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Keep
@Entity
data class Transaction(
    @PrimaryKey
    @NonNull
    val id: Int,
    val address: String,
    val date: Long,
    val amount: Double,
    val type: String, //CREDIT, DEBIT
    var tag: String? = null,
    var tagColor: String? = "#F44336"
): Parcelable