package com.example.trackmyrun.tmp_bluetooth.data.chat

import com.example.trackmyrun.tmp_bluetooth.domain.chat.BluetoothMessage
import android.bluetooth.BluetoothSocket
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException

class TransferFailedException: IOException("Reading incoming data failed")

class BluetoothDataTranferService(
    private val socket: BluetoothSocket
) {

    fun listenForIncomingMessages(): Flow<BluetoothMessage> = flow {

        if (!socket.isConnected) return@flow

        val buffer = ByteArray(1024)

        while (true) {

            val byteCount = try {
                socket.inputStream.read(buffer)
            } catch (e: IOException) {
                throw TransferFailedException()
            }

            emit(
                buffer.decodeToString(
                    endIndex = byteCount
                ).toBluetoothMessage(
                    isFromLocalUser = false
                )
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