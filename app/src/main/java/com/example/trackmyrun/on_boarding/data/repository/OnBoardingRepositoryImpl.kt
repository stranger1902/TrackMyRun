package com.example.trackmyrun.on_boarding.data.repository

import com.example.trackmyrun.on_boarding.domain.repository.OnBoardingRepository
import com.example.trackmyrun.on_boarding.data.local.OnBoardingModel
import dagger.hilt.android.qualifiers.ApplicationContext
import com.example.trackmyrun.R
import android.content.Context

class OnBoardingRepositoryImpl(
    @ApplicationContext private val context: Context
): OnBoardingRepository {

    override fun getData(): List<OnBoardingModel> = listOf(

        OnBoardingModel(
            description = "Preparati a migliorare le tue performance di corsa e travviare i tuoi progressi.\n${context.getString(R.string.app_name)} è qui per aiutarti a raggiungere i tuoi obiettivi di fitness.",
            title = "Benvenuto su ${context.getString(R.string.app_name)}",
            resImage = R.drawable.screen_1
        ),

        OnBoardingModel(
            description = "Inserisci le tue informazioni essenziali per personalizzzare la tua esperienza e tenere traccia dei tuoi progressi",
            title = "Creiamo il tuo profilo",
            resImage = R.drawable.screen_2
        ),

        OnBoardingModel(
            description = "Per registrare le tue corse, abbiamo richiesto permesso di accedere alla tua posizione in tempo reale e il permesso di visualizzare la notifica del servizio di tracking della posizione in tempo reale.\nNon preoccuparti, i tuoi dati saranno al sicuro!",
            title = "Traccia il tuo percorso",
            resImage = R.drawable.screen_3
        ),

        OnBoardingModel(
            title = "Sei pronto a partire!",
            description = "Ottimo! Hai configurato tutto. Ora puoi iniziare a tracciare i tuoi percorsi di corsa e monitorare i tuoi progressi.",
            resImage = R.drawable.screen_4
        )
    )

}