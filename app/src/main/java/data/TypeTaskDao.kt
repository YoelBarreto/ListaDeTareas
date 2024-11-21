package data

import androidx.room.*

@Dao
interface TypeTaskDao {
    @Insert
    suspend fun insertTypeTask(typeTask: TypeTask)

    @Query("SELECT * FROM type_tasks")
    suspend fun getAllTypeTasks(): List<TypeTask>

    @Update
    suspend fun updateTypeTask(typeTask: TypeTask)

    @Delete
    suspend fun deleteTypeTask(typeTask: TypeTask)
}