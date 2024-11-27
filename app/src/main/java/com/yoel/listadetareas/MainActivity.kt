package com.yoel.listadetareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import data.AppDatabase
import view.*

class MainActivity : ComponentActivity() {
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicializar la base de datos
        database = AppDatabase.getDatabase(this)
        setContent {
            //TaskApp(database)
            NavigatorApp(database)
        }
    }
}
