package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import data.AppDatabase
import data.Task
import data.TaskWithTypeTask
import data.TypeTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Pantalla configuración de tarea
@Composable
fun TaskEdit() {

}




@Composable
fun TaskApp(database: AppDatabase) {
    val taskDao = database.taskDao()
    val typeTaskDao = database.typeTaskDao()
    val scope = rememberCoroutineScope()

    // Clases de datos
    var tasks by remember { mutableStateOf(listOf<TaskWithTypeTask>()) }
    var typeTasks by remember { mutableStateOf(listOf<TypeTask>()) }

    // Inputs
    var newTaskName by remember { mutableStateOf("") }
    var newTaskDesc by remember { mutableStateOf("") }

    // Tipos de tarea
    var TypeSelected by remember { mutableStateOf("- Tipo") }
    var expandedType by remember { mutableStateOf(false) }
    var newTypeid by remember { mutableStateOf(0) }


    // Cargar tareas al iniciar
    LaunchedEffect(Unit) {
        typeTasks = typeTaskDao.getAllTypeTasks()
        tasks = taskDao.getTasksWithTypeTasks()
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFEAA9FF))
            .fillMaxSize()
            .padding(15.dp)
            .verticalScroll(state = rememberScrollState()),
    ) {
        Row {
            OutlinedTextField(
                value = newTaskName,
                onValueChange = { newTaskName = it },
                label = {
                    Text(
                        text = "Tarea",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold
                        ),)

                },
                modifier = Modifier
                    .width(150.dp)
                    .padding(end = 3.dp)
//                    .background(Color(0xFFF1C7FF))
            )
            OutlinedTextField(
                value = newTaskDesc,
                onValueChange = { newTaskDesc = it },
                label = {
                    Text(
                        text = "Descripción",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold
                        ),)

                },
                modifier = Modifier
                    .width(250.dp))
//                    .background(Color(0xFFF1C7FF))
        }
        Row {
            OutlinedTextField(
                value = TypeSelected,
                onValueChange = { TypeSelected = it },
                readOnly = true,
                label = { Text("Tipo") },
                modifier = Modifier.weight(1f).padding(end = 5.dp)
            )
            Button(
                modifier = Modifier.height(65.dp).padding(top = 5.dp),
                onClick = { expandedType = true }
            ) {
                Text("Seleccionar")
            }
            DropdownMenu(
                expanded = expandedType,
                onDismissRequest = { expandedType = false }
            ) {
                typeTasks.forEach { type ->

                    DropdownMenuItem(
                        text = {
                            Text(type.titulo)
                        },
                        onClick = {
                            newTypeid = type.id
                            TypeSelected = type.titulo
                            expandedType = false
                        }
                    )
                }
            }

            Button(
                onClick = {
                },
                modifier = Modifier
                    .padding(start = 5.dp, top = 5.dp)
                    .height(60.dp)
                    .width(75.dp)
            ) {
                Text(
                    text = "Editar Tipos",
                    fontSize = 13.sp
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier
                    .width(170.dp)
                    .height(50.dp),
                onClick = {
                    scope.launch(Dispatchers.IO) {
                        taskDao.insertTask(
                            Task(
                                titulo = newTaskName,
                                descripcion = newTaskDesc,
                                typeTaskId = newTypeid
                            )
                        )
                        // Limpiar los cambios de input
                        newTaskName = ""
                        newTaskDesc = ""
                        newTypeid = 0

                        // Refresca las lista de tareas
                        tasks = taskDao.getTasksWithTypeTasks()
                    }
                }
            ) {
                Text("Añadir Tarea")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
             Column(
                 horizontalAlignment = Alignment.CenterHorizontally,
             ) {
                 Text(
                     text = "Lista de Tareas",
                     modifier = Modifier.padding(bottom = 35.dp),
                     fontWeight = FontWeight.Bold,
                     fontSize = 35.sp
                 )
                 Column {
                     // Mostrar lista de tareas
                     tasks.forEach { taskWithTypeTask ->
                         Box(
                             modifier = Modifier
                                 .padding(bottom = 10.dp)
                                 .clip(shape = RoundedCornerShape(10.dp))
                                 .background(Color(0xFFF1C7FF))
                                 .height(75.dp)
                         ) {
                             Row(
                                 modifier = Modifier.fillMaxSize(),
                                 horizontalArrangement = Arrangement.Center,
                                 verticalAlignment = Alignment.CenterVertically
                             ) {
                                 Column(
                                     modifier = Modifier
                                         .weight(0.25f)
                                         .padding(start = 5.dp, top = 10.dp)
                                 ) {
                                     Text(
                                         text = taskWithTypeTask.task.titulo,
                                         fontSize = 18.sp,
                                         fontWeight = FontWeight.Bold
                                     )
                                     Text(
                                         text = "${taskWithTypeTask.typeTask.titulo} #${taskWithTypeTask.typeTask.id}",
                                         fontSize = 12.sp,
                                         fontWeight = FontWeight.Bold
                                     )
                                 }

                                 Column(
                                     modifier = Modifier
                                         .weight(0.4f)
                                         .padding(start = 10.dp, end = 10.dp)
                                 ) {
                                     Text(taskWithTypeTask.task.descripcion)
                                 }
                                 Row(
                                     modifier = Modifier
                                         .weight(0.5f)
                                         .padding(end = 5.dp),
                                     horizontalArrangement = Arrangement.Center
                                 ) {
                                     Button(
                                         onClick = {
                                         },
                                         modifier = Modifier.weight(1f)
                                             .padding(end = 5.dp),
                                     ) {
                                         Text(
                                             text = "Editar",
                                             modifier = Modifier,
                                             fontSize = 10.sp
                                         )
                                     }
                                     Button(
                                         onClick = {
                                             scope.launch(Dispatchers.IO) {
                                                 taskDao.deleteTask(taskWithTypeTask.task)

                                                 tasks = taskDao.getTasksWithTypeTasks()
                                             }
                                         },
                                         modifier = Modifier.weight(1f)
                                     ) {
                                         Text(
                                             text = "Eliminar",
                                             modifier = Modifier,
                                             fontSize = 10.sp
                                         )
                                     }
                                 }
                             }
                         }
                     }
                 }
             }
        }
    }
}
