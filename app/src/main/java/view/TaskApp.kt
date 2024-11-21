package view

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import data.AppDatabase
import data.Task
import data.TaskWithTypeTask
import data.TypeTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun TaskApp(database: AppDatabase) {
    val taskDao = database.taskDao()
    val typeTaskDao = database.typeTaskDao()
    val scope = rememberCoroutineScope()
    var tasks by remember { mutableStateOf(listOf<TaskWithTypeTask>()) }
    var typeTasks by remember { mutableStateOf(listOf<TypeTask>()) }
    var newTaskName by remember { mutableStateOf("") }
    var newTaskDesc by remember { mutableStateOf("") }
    var newTypeTaskName by remember { mutableStateOf("") }

    // Cargar tareas al iniciar
    LaunchedEffect(Unit) {
        typeTasks = typeTaskDao.getAllTypeTasks()
        tasks = taskDao.getTasksWithTypeTasks()
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),

    ) {
        Row {
            androidx.compose.material.OutlinedTextField(
                value = newTaskName,
                onValueChange = { newTaskName = it },
                label = { androidx.compose.material.Text("Tarea") },
                modifier = Modifier.fillMaxWidth()
            )
            androidx.compose.material.OutlinedTextField(
                value = newTaskDesc,
                onValueChange = { newTaskDesc = it },
                label = { androidx.compose.material.Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
        }






    }










//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        // Campo de texto para agregar una nueva tarea
//        androidx.compose.material.OutlinedTextField(
//            value = newTaskName,
//            onValueChange = { newTaskName = it },
//            label = { androidx.compose.material.Text("Tarea") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        androidx.compose.material.OutlinedTextField(
//            value = newTaskDesc,
//            onValueChange = { newTaskDesc = it },
//            label = { androidx.compose.material.Text("Descripción") },
//            modifier = Modifier.fillMaxWidth()
//        )
//        androidx.compose.material.OutlinedTextField(
//            value = newTypeTaskName,
//            onValueChange = { newTypeTaskName = it },
//            label = { androidx.compose.material.Text("Tipo") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        // Botón para agregar tarea
//        androidx.compose.material.Button(
//            onClick = {
//                scope.launch(Dispatchers.IO) {
//                    val newType = TypeTask(titulo = newTypeTaskName)
//                    typeTaskDao.insertTypeTask(newType)
//
//                    val insertedTypeId = typeTaskDao.getAllTypeTasks().last().id // Obtener el ID del nuevo TypeTask
//                    val newTask = Task(titulo = newTaskName, descripcion = newTaskDesc, typeTaskId = insertedTypeId)
//                    taskDao.insertTask(newTask)
//
//                    tasks = taskDao.getTasksWithTypeTasks() // Actualizar lista con relación completa
//                    newTaskName = ""
//                    newTaskDesc = ""
//                    newTypeTaskName = ""
//                }
//            }
//        ) {
//            androidx.compose.material.Text("Add Task")
//        }
//
//        // Mostrar lista de tareas
//        tasks.forEach { taskWithTypeTask ->
//            val task = taskWithTypeTask.task
//            val typeTask = taskWithTypeTask.typeTask
//            androidx.compose.material.Text(
//                text = "${task.titulo} - ${task.descripcion} (${typeTask.titulo})",
//                modifier = Modifier.padding(vertical = 8.dp)
//            )
//        }
//    }
}
