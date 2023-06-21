package id.ac.umn.githubsearchproject.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.ac.umn.githubsearchproject.data.model.ResponseUser

@Database(entities = [ResponseUser.Item::class], version=1, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {
    abstract fun userDao(): UserDao
}