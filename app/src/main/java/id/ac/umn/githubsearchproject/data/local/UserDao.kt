package id.ac.umn.githubsearchproject.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import id.ac.umn.githubsearchproject.data.model.ResponseUser

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: ResponseUser.Item)

    @Query("SELECT * FROM user")
    fun loadAll() : LiveData<MutableList<ResponseUser.Item>>

    @Query("SELECT * FROM User WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): ResponseUser.Item

    @Delete
    fun delete(user: ResponseUser.Item)
}