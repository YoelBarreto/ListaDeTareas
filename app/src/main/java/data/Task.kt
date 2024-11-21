package data

import androidx.room.Embedded
import androidx.room.Entity import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(
    tableName = "tasks",
    foreignKeys = [
        androidx.room.ForeignKey(
            entity = TypeTask::class,
            parentColumns = ["id"],
            childColumns = ["typeTaskId"],
            onDelete = androidx.room.ForeignKey.CASCADE // Opcional: para borrar tareas relacionadas al eliminar un tipo.
        )
    ],
    indices = [androidx.room.Index(value = ["typeTaskId"])] // Mejora las consultas.
)

data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    val typeTaskId: Int // Relación con la tabla type_tasks
)