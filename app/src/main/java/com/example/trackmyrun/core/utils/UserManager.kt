package com.example.trackmyrun.core.utils

import com.example.trackmyrun.core.domain.model.UserModel
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.capitalize
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import com.example.trackmyrun.dataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.core.net.toUri
import android.content.Context
import android.net.Uri

class UserManager(
    @ApplicationContext private val context: Context
) {

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private var _currentUser: MutableStateFlow<UserModel> = MutableStateFlow(UserModel())
    val currentUser = _currentUser.asStateFlow()

    init {
        coroutineScope.launch {
            context.dataStore.data.collect {
                _currentUser.value = _currentUser.value.copy(
                    profilePicUri = it[Constants.PROFILE_PIC_URI_USER_DATASTORE_KEY]?.toUri(),
                    height = it[Constants.HEIGHT_USER_DATASTORE_KEY] ?: 0,
                    weight = it[Constants.WEIGHT_USER_DATASTORE_KEY] ?: 0,
                    name = it[Constants.NAME_USER_DATASTORE_KEY] ?: "",
                )
            }
        }
    }

    suspend fun saveUserInPreferences(user: UserModel) {
        context.dataStore.edit { preferences ->
            preferences[Constants.NAME_USER_DATASTORE_KEY] = user.name.toLowerCase(Locale.current).capitalize(Locale.current)
            preferences[Constants.HEIGHT_USER_DATASTORE_KEY] = user.height
            preferences[Constants.WEIGHT_USER_DATASTORE_KEY] = user.weight
        }
    }

    suspend fun saveProfilePicUserInPreferences(uri: Uri?) {
        context.dataStore.edit { preferences ->
            preferences[Constants.PROFILE_PIC_URI_USER_DATASTORE_KEY] = uri.toString()
        }
    }

    fun checkUser(): Boolean = _currentUser.value.name != "" && _currentUser.value.weight != 0 && _currentUser.value.height != 0

}