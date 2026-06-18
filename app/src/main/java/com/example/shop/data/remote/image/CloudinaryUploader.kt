package com.example.shop.data.remote.image

import android.content.Context
import android.net.Uri
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import java.io.File
import java.io.FileOutputStream

interface CloudinaryService {
    @Multipart
    @POST("v1_1/{cloud_name}/image/upload")
    suspend fun uploadImage(
        @Path("cloud_name") cloudName: String,
        @Part image: MultipartBody.Part,
        @Part("upload_preset") uploadPreset: okhttp3.RequestBody
    ): CloudinaryResponse
}

data class CloudinaryResponse(
    val secure_url: String
)

object CloudinaryUploader {
    // IMPORTANT: Replace these with your actual Cloudinary credentials from cloudinary.com
    private const val CLOUD_NAME = "dxqszrvid" // Replace this
    private const val UPLOAD_PRESET = "shop_app_preset" // Replace this
    private const val BASE_URL = "https://api.cloudinary.com/"

    private val service: CloudinaryService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CloudinaryService::class.java)
    }

    suspend fun upload(context: Context, uri: Uri): String? {
        return try {
            val file = uriToFile(context, uri) ?: return null
            val requestFile = file.asRequestBody()
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val preset = UPLOAD_PRESET.toRequestBody(MultipartBody.FORM)
            
            val response = service.uploadImage(CLOUD_NAME, body, preset)
            file.delete() // Clean up temp file
            
            response.secure_url
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun uriToFile(context: Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val file = File(context.cacheDir, "upload_temp_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(file)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            file
        } catch (e: Exception) {
            null
        }
    }
}
