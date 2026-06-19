package com.example.shop.data.repository

import com.example.shop.data.local.dao.AddressDao
import com.example.shop.data.local.entity.AddressEntity
import com.example.shop.data.remote.CloudAddressRepository
import kotlinx.coroutines.flow.Flow

class AddressRepository(
    private val addressDao: AddressDao,
    private val cloudAddressRepository: CloudAddressRepository
) {
    fun getAddresses(email: String): Flow<List<AddressEntity>> = 
        addressDao.getAddressesForUser(email)

    suspend fun saveAddress(address: AddressEntity) {
        addressDao.insertAddress(address)
        try {
            cloudAddressRepository.syncAddress(address)
        } catch (e: Exception) {}
    }

    suspend fun deleteAddress(address: AddressEntity) {
        addressDao.deleteAddress(address)
        try {
            cloudAddressRepository.deleteAddress(address.id)
        } catch (e: Exception) {}
    }

    suspend fun setDefault(addressId: Int, email: String) {
        addressDao.setAsDefault(addressId, email)
    }
}
