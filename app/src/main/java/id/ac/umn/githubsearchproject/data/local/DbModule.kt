package id.ac.umn.githubsearchproject.data.local

import android.content.Context
import androidx.room.Room

class DbModule(private val context: Context) {
    private val db = Room.databaseBuilder(context, AppDatabase::class.java, "userdatabase.db")
        .allowMainThreadQueries()
        .build()

    val userDao = db.userDao()
}