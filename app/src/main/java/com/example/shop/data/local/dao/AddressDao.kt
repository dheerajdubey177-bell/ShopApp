package com.example.shop.data.local.dao

import androidx.room.*
import com.example.shop.data.local.entity.AddressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {
    @Query("SELECT * FROM user_addresses WHERE userEmail = :email")
    fun getAddressesForUser(email: String): Flow<List<AddressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity)

    @Delete
    suspend fun deleteAddress(address: AddressEntity)

    @Query("UPDATE user_addresses SET isDefault = 0 WHERE userEmail = :email")
    suspend fun resetDefaults(email: String)

    @Transaction
    suspend fun setAsDefault(addressId: Int, email: String) {
        resetDefaults(email)
        updateDefault(addressId)
    }

    @Query("UPDATE user_addresses SET isDefault = 1 WHERE id = :addressId")
    suspend fun updateDefault(addressId: Int)
}
