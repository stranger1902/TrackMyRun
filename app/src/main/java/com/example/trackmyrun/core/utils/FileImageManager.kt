package com.example.trackmyrun.core.utils

import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.graphics.BitmapFactory
import java.io.FileOutputStream
import android.graphics.Bitmap
import android.content.Context
import javax.inject.Inject
import java.io.File

class FileImageManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun loadImage(filename: String, filepath: String = "", format: String = "png"): Bitmap? {

        File(context.filesDir, "${Constants.IMAGE_FILE_PATH}/$filepath/$filename.$format").also { imageFile ->

            if (!imageFile.exists()) return null

            return withContext(Dispatchers.IO) {
                return@withContext BitmapFactory.decodeFile(imageFile.path)
            }
        }
    }

    suspend fun saveImage(bitmap: Bitmap, filename: String, filepath: String = "", format: String = "png") {

        File(context.filesDir, "${Constants.IMAGE_FILE_PATH}/$filepath/$filename.$format").also { imageFile ->

            withContext(Dispatchers.IO) {

                if (imageFile.parentFile?.exists() == false) imageFile.parentFile?.mkdirs()

                FileOutputStream(imageFile).use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, Constants.MAPS_SNAPSHOT_QUALITY, outputStream)
                }
            }
        }
    }

}