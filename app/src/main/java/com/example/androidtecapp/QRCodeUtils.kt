package com.example.androidtecapp

import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import java.util.Hashtable

fun generateQRCode(text: String, size: Int): Bitmap? {
    val hints = Hashtable<EncodeHintType, String>().apply {
        put(EncodeHintType.CHARACTER_SET, "UTF-8")
    }

    val writer = QRCodeWriter()
    return try {
        val bitMatrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size, hints)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888) // ARGB_8888 supports transparency

        // Color for QR code foreground (BLACK) and background (Transparent)
        val qrCodeColor = Color.BLACK
        val transparent = Color.TRANSPARENT // Transparent background

        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) qrCodeColor else transparent)
            }
        }
        bmp
    } catch (e: WriterException) {
        e.printStackTrace()
        null
    }
}
