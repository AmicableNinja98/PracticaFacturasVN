package com.example.core.extensions

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

fun String.toFormattedDisplayDateOrNull(): String? {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())
        val localDate = LocalDate.parse(this, inputFormatter)

        val outputFormatter = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.getDefault())
        val formatted = localDate.format(outputFormatter)

        val parts = formatted.split(" ")
        if (parts.size == 3) {
            val day = parts[0]
            val month = parts[1].replaceFirstChar { it.uppercaseChar() }
            val year = parts[2]
            "$day $month $year"
        } else {
            formatted // fallback por si falla el split
        }
    } catch (_: DateTimeParseException) {
        null
    }
}

fun String.toLocalDateOrNull(): LocalDate? {
    return try {
        LocalDate.parse(this, DateTimeFormatter.ofPattern("dd-MM-yyyy"))
    } catch (_: Exception) {
        null
    }
}

fun String.toMillis(pattern: String = "dd/MM/yyyy"): Long? {
    return try {
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        formatter.parse(this)?.time
    } catch (_: Exception) {
        null
    }
}