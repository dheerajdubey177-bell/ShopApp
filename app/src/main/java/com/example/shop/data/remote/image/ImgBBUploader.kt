package com.example.shop.data.remote.image

import android.content.Context
import android.net.Uri
import android.util.Base64
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import java.io.File
import java.io.FileOutputStream

interface ImgBBService {
    @Multipart
    @POST("1/upload")
    suspend fun uploadImage(
        @Query("key") apiKey: String,
        @Part image: MultipartBody.Part
    ): ImgBBResponse
}

data class ImgBBResponse(
    val data: ImgBBData?,
    val success: Boolean,
    val status: Int
)

data class ImgBBData(
    val url: String
)

object ImgBBUploader {
    private const val API_KEY = "YOUR_IMGBB_API_KEY" // User needs to replace this
    private const val BASE_URL = "https://api.imgbb.com/"

    private val service: ImgBBService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ImgBBService::class.java)
    }

    suspend fun upload(context: Context, uri: Uri): String? {
        return try {
            val file = uriToFile(context, uri) ?: return null
            val requestFile = file.asRequestBody()
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
            
            val response = service.uploadImage(API_KEY, body)
            file.delete() // Clean up temp file
            
            if (response.success) response.data?.url else null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File? {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val file = File(context.cacheDir, "upload_temp_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)
        inputStream.close()
        outputStream.close()
        return file
    }
}
