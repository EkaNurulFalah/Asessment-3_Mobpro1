package com.ekanurulfalah0018.asessment3.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekanurulfalah0018.asessment3.model.Sepatu
import com.ekanurulfalah0018.asessment3.network.ApiStatus
import com.ekanurulfalah0018.asessment3.network.SepatuApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import android.util.Base64

class MainViewModel : ViewModel() {

    var data = mutableStateOf(emptyList<Sepatu>())
        private set

    var status = MutableStateFlow(ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set
fun retrieveData(userId: String) {
    viewModelScope.launch(Dispatchers.IO) {
        status.value = ApiStatus.LOADING

        try {
            val list = SepatuApi.service.getSepatu()
            data.value = if (userId.isEmpty()) {
                list.filter { it.userId.isEmpty() }
            } else {
                list.filter {
                    it.userId.isEmpty() || it.userId == userId
                }
            }

            status.value = ApiStatus.SUCCESS

        } catch (e: Exception) {
            Log.e("API", "Error", e)
            status.value = ApiStatus.FAILED
        }
    }
}

    private fun Bitmap.toBase64(): String {
        val resized = Bitmap.createScaledBitmap(
            this,
            300,
            300,
            true
        )
        val stream = ByteArrayOutputStream()
        resized.compress(
            Bitmap.CompressFormat.JPEG,
            60,
            stream
        )
        val byteArray = stream.toByteArray()
        return Base64.encodeToString(
            byteArray,
            Base64.NO_WRAP
        )
    }

    fun clearMessage() {
        errorMessage.value = null
    }

    fun saveData(
        userId: String,
        brand: String,
        name: String,
        bitmap: Bitmap
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val base64 = bitmap.toBase64()
                Log.d("BASE64", "Length = ${base64.length}")
                Log.d("BASE64", base64.take(100))

                val result = SepatuApi.service.postSepatu(
                    Sepatu(
                        userId = userId,
                        brand = brand,
                        name = name,
                        imageUrl = bitmap.toBase64()
                    )
                )
                Log.d("UPLOAD", "Success: $result")
                retrieveData(userId)
            } catch (e: Exception) {
                Log.e("UPLOAD", "Failure", e)
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun deleteData(userId: String, id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                SepatuApi.service.deleteSepatu(id)

                retrieveData(userId)

            } catch (e: Exception) {
                errorMessage.value = e.message
            }
        }
    }
}