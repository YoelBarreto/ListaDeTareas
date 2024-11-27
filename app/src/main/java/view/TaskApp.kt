package view

import android.app.AlertDialog
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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


@OptIn(ExperimentalMaterial3Api::class)
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

    //Editar tarea
    var newTypeSelected by remember { mutableStateOf("- Tipo") }
    var newExpandedType by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var TaskName by remember { mutableStateOf("") }
    var TaskDesc by remember { mutableStateOf("") }
    var Typeid by remember { mutableStateOf(0) }



    // Cargar tareas al iniciar
    LaunchedEffect(Unit) {
        try {
            typeTasks = typeTaskDao.getAllTypeTasks()
            tasks = taskDao.getTasksWithTypeTasks()
        } catch (e: Exception) {
            Log.i("Error", e.toString())
        }
    }

    // Pantalla principal
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
                    // Corrutina para añadir tarea
                    scope.launch(Dispatchers.IO) {
                        try {
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
                        } catch (e: Exception) {
                            Log.i("Error", e.toString())
                        }
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
                     // Mostrar la lista de tareas
                     tasks.forEach { taskWithTypeTask ->
                         // Tarjeta de tarea
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
                                         .weight(0.35f)
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
                                         .weight(0.6f)
                                         .padding(start = 10.dp, end = 10.dp)
                                 ) {
                                     Text(taskWithTypeTask.task.descripcion)
                                 }
                                 Row(
                                     modifier = Modifier
                                         .weight(0.4f)
                                         .padding(end = 5.dp),
                                     horizontalArrangement = Arrangement.Center
                                 ) {
                                     androidx.compose.material.IconButton(
                                         modifier = Modifier,
                                         onClick = {
                                             showDialog = true
                                         }
                                     ) {
                                         Icon(Icons.Default.Edit, contentDescription = "Editar")
                                     }
                                     // Ventana edición tarea
                                     if (showDialog) {
                                         AlertDialog(
                                             onDismissRequest = { showDialog = false },
                                             title = { Text("Editar Tarea") },
                                             text = {
                                                 Column(
                                                     verticalArrangement = Arrangement.Center,
                                                     horizontalAlignment = Alignment.CenterHorizontally
                                                 ) {
                                                     OutlinedTextField(
                                                         value = TaskName,
                                                         onValueChange = { TaskName = it },
                                                         label = {
                                                             Text(
                                                                 text = "Tarea",
                                                                 style = TextStyle(
                                                                     fontWeight = FontWeight.Bold
                                                                 ),)

                                                         },
                                                         modifier = Modifier
                                                     )
                                                     OutlinedTextField(
                                                         value = TaskDesc,
                                                         onValueChange = { TaskDesc = it },
                                                         label = {
                                                             Text(
                                                                 text = "Descripción",
                                                                 style = TextStyle(
                                                                     fontWeight = FontWeight.Bold
                                                                 ),)

                                                         },
                                                         modifier = Modifier
                                                     )
                                                     Row {
                                                         OutlinedTextField(
                                                             value = newTypeSelected,
                                                             onValueChange = { newTypeSelected = it },
                                                             readOnly = true,
                                                             label = { Text("Tipo") },
                                                             modifier = Modifier.width(160.dp)
                                                         )
                                                         Button(
                                                             modifier = Modifier
                                                                 .width(120.dp)
                                                                 .height(60.dp)
                                                                 .padding(top = 5.dp, start = 5.dp),
                                                             onClick = { newExpandedType = true }
                                                         ) {
                                                             Text("Seleccionar")
                                                         }
                                                         DropdownMenu(
                                                             expanded = newExpandedType,
                                                             onDismissRequest = { newExpandedType = false }
                                                         ) {
                                                             typeTasks.forEach { type ->

                                                                 DropdownMenuItem(
                                                                     text = {
                                                                         Text(type.titulo)
                                                                     },
                                                                     onClick = {
                                                                         Typeid = type.id
                                                                         newTypeSelected = type.titulo
                                                                         newExpandedType = false
                                                                     }
                                                                 )
                                                             }
                                                         }
                                                     }
                                                 }
                                             },
                                             confirmButton = {
                                                 TextButton(
                                                     onClick = {
                                                         try {
                                                             scope.launch(Dispatchers.IO) {
                                                                 taskDao.updateTask(
                                                                     Task(
                                                                         titulo = TaskName,
                                                                         descripcion = TaskDesc,
                                                                         typeTaskId = Typeid
                                                                     )
                                                                 )
                                                                 showDialog = false
                                                             }
                                                         } catch (e: Exception) {
                                                             Log.i("Error", e.toString())
                                                         }
                                                     }
                                                 ) {
                                                     Text("Aztualizar")
                                                 }
                                             },
                                             dismissButton = {
                                                 TextButton(onClick = { showDialog = false }) {
                                                     Text("Cancelar")
                                                 }
                                             }
                                         )
                                         // Clickear fuera limpia los campos
                                     } else {
                                         TaskName = ""
                                         TaskDesc = ""
                                     }
                                     androidx.compose.material.IconButton(
                                         modifier = Modifier,
                                         onClick = {
                                             scope.launch(Dispatchers.IO) {
                                                 try {
                                                     taskDao.deleteTask(taskWithTypeTask.task)

                                                     tasks = taskDao.getTasksWithTypeTasks()
                                                 } catch (e: Exception) {
                                                     Log.i("Error", e.toString())

                                                 }
                                             }
                                         }
                                     ) {
                                         Icon(Icons.Default.Delete, contentDescription = "Eliminar")
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
