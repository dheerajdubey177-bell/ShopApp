package com.example.shop

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import org.junit.Test

class CloudinaryVerifyTest {

    @Test
    fun verifyCloudinary() {
        // 1. Configure Cloudinary
        val config = mapOf(
            "cloud_name" to "ditxuvdrb",
            "api_key" to "246228667219247",
            "api_secret" to "YFoStknQZQrBdANlycOzewY3zf0"
        )
        val cloudinary = Cloudinary(config)

        println("--- STEP 1: Configuration Complete ---")

        try {
            // 2. Upload an image
            val sampleUrl = "https://res.cloudinary.com/demo/image/upload/sample.jpg"
            println("--- STEP 2: Uploading sample image from $sampleUrl... ---")
            
            val uploadResult = cloudinary.uploader().upload(sampleUrl, ObjectUtils.emptyMap())
            
            val secureUrl = uploadResult["secure_url"] as String
            val publicId = uploadResult["public_id"] as String
            
            println("Upload Successful!")
            println("Secure URL: $secureUrl")
            println("Public ID: $publicId")

            // 3. Get image details
            val width = uploadResult["width"]
            val height = uploadResult["height"]
            val format = uploadResult["format"]
            val size = uploadResult["bytes"]

            println("\n--- STEP 3: Image Metadata ---")
            println("Width: $width px")
            println("Height: $height px")
            println("Format: $format")
            println("File Size: $size bytes")

            // 4. Transform the image
            // f_auto: Automatic format selection (delivers best format like WebP/AVIF based on browser support)
            // q_auto: Automatic quality (optimizes compression to reduce size while maintaining visual quality)
            val transformedUrl = cloudinary.url()
                .transformation(com.cloudinary.Transformation<com.cloudinary.Transformation<*>>()
                    .fetchFormat("auto")
                    .quality("auto"))
                .generate(publicId)

            println("\n--- STEP 4: Transformation Complete ---")
            println("Done! Click link below to see optimized version of the image. Check the size and the format.")
            println("Transformed URL: $transformedUrl")

        } catch (e: Exception) {
            println("\nError occurred during Cloudinary operations:")
            e.printStackTrace()
        }
    }
}
