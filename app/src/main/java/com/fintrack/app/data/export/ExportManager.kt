package com.fintrack.app.data.export

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.fintrack.app.core.util.asMoney
import com.fintrack.app.core.util.toDisplayDate
import com.fintrack.app.domain.model.FinanceTransaction
import com.fintrack.app.domain.model.TransactionType
import java.io.File
import javax.inject.Inject

class ExportManager @Inject constructor(
    private val context: Context
) {
    fun exportCsv(transactions: List<FinanceTransaction>): Uri {
        val file = File(context.cacheDir, "fintrack-transactions.csv")
        file.writeText(buildString {
            appendLine("id,type,amount,category,note,paymentMethod,date,createdAt")
            transactions.forEach { transaction ->
                appendLine(
                    listOf(
                        transaction.id,
                        transaction.type.name,
                        transaction.amount,
                        transaction.category.csv(),
                        transaction.note.csv(),
                        transaction.paymentMethod.csv(),
                        transaction.date.toDisplayDate().csv(),
                        transaction.createdAt
                    ).joinToString(",")
                )
            }
        })
        return uriFor(file)
    }

    fun exportPdf(transactions: List<FinanceTransaction>): Uri {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val titlePaint = Paint().apply {
            textSize = 20f
            isFakeBoldText = true
        }
        val bodyPaint = Paint().apply { textSize = 11f }
        canvas.drawText("FinTrack Monthly Report", 40f, 50f, titlePaint)
        var y = 82f
        transactions.take(32).forEach { transaction ->
            val signed = if (transaction.type == TransactionType.EXPENSE) -transaction.amount else transaction.amount
            canvas.drawText(
                "${transaction.date.toDisplayDate()}  ${transaction.category}  ${signed.asMoney()}  ${transaction.note}",
                40f,
                y,
                bodyPaint
            )
            y += 22f
        }
        document.finishPage(page)
        val file = File(context.cacheDir, "fintrack-report.pdf")
        file.outputStream().use { document.writeTo(it) }
        document.close()
        return uriFor(file)
    }

    private fun uriFor(file: File): Uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

    private fun String.csv(): String = "\"${replace("\"", "\"\"")}\""
}
