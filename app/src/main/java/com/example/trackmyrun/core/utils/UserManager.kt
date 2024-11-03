package com.example.trackmyrun.core.utils

import com.example.trackmyrun.core.utils.Constants.Companion.WEIGHT_USER_DATASTORE_KEY
import com.example.trackmyrun.core.utils.Constants.Companion.HEIGHT_USER_DATASTORE_KEY
import com.example.trackmyrun.core.utils.Constants.Companion.NAME_USER_DATASTORE_KEY
import com.example.trackmyrun.core.utils.Constants.Companion.USER_DATASTORE_KEY
import androidx.datastore.preferences.preferencesDataStore
import com.example.trackmyrun.core.domain.model.UserModel
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.capitalize
import kotlinx.coroutines.flow.asStateFlow
import androidx.datastore.core.DataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.content.Context

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(USER_DATASTORE_KEY)

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
                    height = it[HEIGHT_USER_DATASTORE_KEY] ?: 0,
                    weight = it[WEIGHT_USER_DATASTORE_KEY] ?: 0,
                    name = it[NAME_USER_DATASTORE_KEY] ?: "",
                )
            }
        }
    }

    suspend fun saveUserInPreferences(user: UserModel) {
        context.dataStore.edit { preferences ->
            preferences[NAME_USER_DATASTORE_KEY] = user.name.toLowerCase(Locale.current).capitalize(Locale.current)
            preferences[HEIGHT_USER_DATASTORE_KEY] = user.height
            preferences[WEIGHT_USER_DATASTORE_KEY] = user.weight
        }
    }

    fun checkUser(): Boolean = _currentUser.value.name != "" && _currentUser.value.weight != 0 && _currentUser.value.height != 0

}