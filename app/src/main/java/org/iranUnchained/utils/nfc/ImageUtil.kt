/*
 * Copyright 2016 Anton Tananaev (anton.tananaev@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.iranUnchained.utils.nfc

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.gemalto.jp2.JP2Decoder
import org.iranUnchained.utils.nfc.model.Image
import org.jmrtd.lds.AbstractImageInfo
import org.jnbis.WsqDecoder
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.io.IOException
import java.io.InputStream
import java.util.Locale

object ImageUtil {
    fun getImage(context: Context?, imageInfo: AbstractImageInfo): Image {
        val image = Image()
        val imageLength = imageInfo.imageLength
        val dataInputStream = DataInputStream(imageInfo.imageInputStream)
        val buffer = ByteArray(imageLength)
        try {
            dataInputStream.readFully(buffer, 0, imageLength)
            val inputStream: InputStream = ByteArrayInputStream(buffer, 0, imageLength)
            val bitmapImage = decodeImage(context, imageInfo.mimeType, inputStream)
            image.bitmapImage = bitmapImage
            val base64Image = Base64.encodeToString(buffer, Base64.DEFAULT)
            image.base64Image = base64Image
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return image
    }

    fun scaleImage(bitmap: Bitmap?): Bitmap? {
        var bitmapImage: Bitmap? = null
        if (bitmap != null) {
            val ratio = 400.0 / bitmap.height
            val targetHeight = (bitmap.height * ratio).toInt()
            val targetWidth = (bitmap.width * ratio).toInt()
            bitmapImage = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, false)
        }
        return bitmapImage
    }

    @Throws(IOException::class)
    fun decodeImage(context: Context?, mimeType: String, inputStream: InputStream?): Bitmap {
        val mimeTypeLower = mimeType.lowercase(Locale.getDefault())
        return if (mimeTypeLower == "image/jp2" || mimeTypeLower == "image/jpeg2000") {
            JP2Decoder(inputStream).decode()
        } else if (mimeTypeLower == "image/x-wsq") {
            val wsqDecoder = WsqDecoder()
            val bitmap = wsqDecoder.decode(inputStream)
            val byteData = bitmap.pixels
            val intData = IntArray(byteData.size)
            for (j in byteData.indices) {
                intData[j] = -0x1000000 or
                        (byteData[j].toInt() and (0xFF shl 16)) or
                        (byteData[j].toInt() and (0xFF shl 8)) or (byteData[j].toInt() and 0xFF)
            }
            Bitmap.createBitmap(
                intData,
                0,
                bitmap.width,
                bitmap.width,
                bitmap.height,
                Bitmap.Config.ARGB_8888
            )
        } else {
            BitmapFactory.decodeStream(inputStream)
        }
    }
}
