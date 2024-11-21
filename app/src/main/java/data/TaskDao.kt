package data

import androidx.room.*

@Dao
interface TaskDao {
    @Insert
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<Task>

    @Transaction
    @Query("SELECT * FROM tasks")
    suspend fun getTasksWithTypeTasks(): List<TaskWithTypeTask> // Relación con TypeTask

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}