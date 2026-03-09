package com.timstamping.app

import android.content.Context
import android.os.Environment
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FileManager(private val context: Context) {
    private val json = Json { prettyPrint = true }

    fun loadConfig(): Config? {
        return try {
            val file = File(context.filesDir, "config.json")
            if (!file.exists()) {
                // Carica da assets se esiste
                val assetContent = context.assets.open("config.json").bufferedReader().use { it.readText() }
                json.decodeFromString(assetContent)
            } else {
                val content = file.readText()
                json.decodeFromString(content)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun loadDipendenti(): Dipendenti? {
        return try {
            val file = File(context.filesDir, "dipendenti.json")
            if (!file.exists()) {
                val assetContent = context.assets.open("dipendenti.json").bufferedReader().use { it.readText() }
                json.decodeFromString(assetContent)
            } else {
                val content = file.readText()
                json.decodeFromString(content)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getDipendenteByCode(code: String): Dipendente? {
        val dipendenti = loadDipendenti() ?: return null
        return dipendenti.dipendenti.find { it.code == code }
    }

    fun getSpecialCodeFunction(code: String, config: Config?): String? {
        config ?: return null
        return config.specialCodes.find { it.code == code }?.function
    }

    fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getCurrentTime(): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getCurrentDateFormatted(): String {
        val sdf = SimpleDateFormat("dd/mm/yyyy", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getStampingsFile(): File {
        val stampingsDir = File(context.filesDir, "stampings")
        if (!stampingsDir.exists()) {
            stampingsDir.mkdirs()
        }
        val currentDate = getCurrentDate()
        val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date()).lowercase()
        return File(stampingsDir, "${currentDate}_$dayName.json")
    }

    fun loadStampings(): Stampings {
        val file = getStampingsFile()
        return try {
            if (file.exists()) {
                val content = file.readText()
                json.decodeFromString(content)
            } else {
                Stampings()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Stampings()
        }
    }

    fun saveStamping(code: String, type: String) {
        val stampings = loadStampings()
        stampings.stampings.add(
            Stamping(
                code = code,
                type = type,
                time = getCurrentTime()
            )
        )
        
        val file = getStampingsFile()
        file.writeText(json.encodeToString(stampings))
    }

    fun getVersionFromConfig(): String? {
        return try {
            val file = File(context.filesDir, "config.json")
            val content = if (!file.exists()) {
                context.assets.open("config.json").bufferedReader().use { it.readText() }
            } else {
                file.readText()
            }
            val config: Config = json.decodeFromString(content)
            config.version
        } catch (e: Exception) {
            null
        }
    }
}
