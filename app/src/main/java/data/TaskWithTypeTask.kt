package data
import androidx.room.Embedded
import androidx.room.Relation
data class TaskWithTypeTask(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "typeTaskId",
        entityColumn = "id"
    )
    val typeTask: TypeTask
)