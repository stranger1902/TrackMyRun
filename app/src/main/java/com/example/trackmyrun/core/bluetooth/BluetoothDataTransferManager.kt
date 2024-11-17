package com.example.trackmyrun.core.bluetooth

import com.example.trackmyrun.core.bluetooth.domain.model.BluetoothMessageModel
import com.example.trackmyrun.core.bluetooth.domain.model.toBluetoothMessage
import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class BluetoothDataTransferManager(
    private val socket: BluetoothSocket
) {

    fun listenForIncomingMessages(): Flow<BluetoothMessageModel> = flow {

        if (!socket.isConnected) return@flow

        val buffer = ByteArray(1024)

        while (true) {

            val byteCount = socket.inputStream.read(buffer)

            emit(
                buffer.decodeToString(
                    endIndex = byteCount
                ).toBluetoothMessage(false)
            )
        }
    }.flowOn(Dispatchers.IO)

    suspend fun sendMessage(bytes: ByteArray): Boolean = withContext(Dispatchers.IO) {
        try {
            socket.outputStream.write(bytes)
            true
        }
        catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

}