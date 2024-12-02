package com.blankcat.teva.models

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import io.github.alexzhirkevich.qrose.QrCodePainter
import io.github.alexzhirkevich.qrose.oned.BarcodePainter
import io.github.alexzhirkevich.qrose.oned.BarcodeType
import io.github.alexzhirkevich.qrose.options.QrOptions
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

enum class TevaCollection {
    Card,
    Coupon
}

@Entity(
    tableName = "barcodes",
    indices = [
        Index("name"),
        Index("code"),
        Index("type"),
        Index("collection")
    ]
)
@Immutable
data class TevaBarcode(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String = "",

    val code: String = "",

    val type: Int,

    val collection: TevaCollection,
    ) {

    val barcodePainter: Painter get() = generateBarcode(this.code, this.type)
}


enum class CodeType { QR, Barcode }

val barcodeMap = mapOf(
    Barcode.FORMAT_CODABAR to BarcodeType.Codabar,
    Barcode.FORMAT_CODE_39 to BarcodeType.Code39,
    Barcode.FORMAT_CODE_93 to BarcodeType.Code93,
    Barcode.FORMAT_CODE_128 to BarcodeType.Code128,
    Barcode.FORMAT_EAN_8 to BarcodeType.EAN8,
    Barcode.FORMAT_EAN_13 to BarcodeType.EAN13,
    Barcode.FORMAT_ITF to BarcodeType.ITF,
    Barcode.FORMAT_UPC_A to BarcodeType.UPCA,
    Barcode.FORMAT_UPC_E to BarcodeType.UPCE,
)

fun generateBarcode(code: String, format: Int): Painter {
    val barcodeType = getBarcodeType(format)

    if (barcodeType == CodeType.QR) {
        return QrCodePainter(data = code, options = QrOptions())
    } else if (barcodeType == CodeType.Barcode) {
        return BarcodePainter(data = code, type = barcodeMap[format]!!)
    }
    throw Exception("Cannot handle barcode type ${format}, casted into null")
}

fun getBarcodeType(format: Int): CodeType? {
    return when (format) {
        Barcode.FORMAT_CODE_128 -> CodeType.Barcode
        Barcode.FORMAT_CODE_39 -> CodeType.Barcode
        Barcode.FORMAT_CODE_93 -> CodeType.Barcode
        Barcode.FORMAT_CODABAR -> CodeType.Barcode
        Barcode.FORMAT_EAN_13 -> CodeType.Barcode
        Barcode.FORMAT_EAN_8 -> CodeType.Barcode
        Barcode.FORMAT_ITF -> CodeType.Barcode
        Barcode.FORMAT_UPC_A -> CodeType.Barcode
        Barcode.FORMAT_UPC_E -> CodeType.Barcode
        Barcode.FORMAT_QR_CODE -> CodeType.QR
        Barcode.FORMAT_DATA_MATRIX -> CodeType.QR
        Barcode.FORMAT_PDF417 -> CodeType.QR
        Barcode.FORMAT_AZTEC -> CodeType.QR
        // Barcode.TYPE_CONTACT_INFO -> CodeType.QR
        // Barcode.TYPE_EMAIL -> CodeType.QR
        // Barcode.TYPE_ISBN -> CodeType.QR
        // Barcode.TYPE_PHONE -> CodeType.QR
        // Barcode.TYPE_PRODUCT -> CodeType.QR
        // Barcode.TYPE_SMS -> CodeType.QR
        // Barcode.TYPE_TEXT -> CodeType.QR
        // Barcode.TYPE_URL -> CodeType.QR
        // Barcode.TYPE_WIFI -> CodeType.QR
        // Barcode.TYPE_GEO -> CodeType.QR
        // Barcode.TYPE_CALENDAR_EVENT -> CodeType.QR
        // Barcode.TYPE_DRIVER_LICENSE -> CodeType.QR
        else -> null
    }
}

suspend fun scanBarcode(inputImage: InputImage?): Barcode? {
    val scanner = BarcodeScanning.getClient()
    val result = inputImage?.let {
        return suspendCoroutine<Barcode> { continuation ->
            scanner.process(it).addOnSuccessListener { barcodes ->
                try {
                    continuation.resume(barcodes.first())
                } catch (e: Exception) {
                    println(e.toString())
                }
            }.addOnFailureListener {
                println("Failed to find barcodes")
            }
        }
    }
    return result
}