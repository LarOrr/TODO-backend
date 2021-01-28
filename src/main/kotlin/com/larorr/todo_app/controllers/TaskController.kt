package com.larorr.todo_app.controllers

// TODO it's better to add Services layer and exception handler for controllers

import com.larorr.todo_app.entities.Task
import com.larorr.todo_app.repositories.TaskRepository
import com.larorr.todo_app.repositories.TodoListRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@CrossOrigin
@RequestMapping("/tasks")
//@RepositoryRestController
class TaskController(private val taskRepository: TaskRepository,
                     private val todoListRepository: TodoListRepository) {

    @GetMapping("/{taskId}")
    fun getTaskById(@PathVariable taskId: Long): Task? {
        return taskRepository.findById(taskId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Task with such id ${taskId} doesn't exist")
        }
    }

    @GetMapping
    fun getTasks(@RequestParam(required = false) userId: Long?): Iterable<Task> =
        when (userId) {
            null -> taskRepository.findAll()
            else -> taskRepository.findAllByTodoList_User_UserId(userId)
        }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createTask(@RequestBody task: Task): Task? {
        if (task.listId < 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "\"listId\" of task object must be present in the request")
        }
        task.todoList = todoListRepository.findById(task.listId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "List with such id ${task.listId} doesn't exist")
        }
        return taskRepository.save(task)
    }

    @PutMapping("/{taskId}")
    fun updateTask(@PathVariable taskId: Long, @RequestBody task: Task): Task? {
        val existingTask = taskRepository.findById(taskId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Task with such id ${taskId} doesn't exist")
        }

        if (task.listId < 0) {
            task.listId = existingTask.listId
        }
        // So it's possible to move list to another list
        task.todoList = todoListRepository.findById(task.listId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "List with such id ${task.listId} doesn't exist")
        }

        return taskRepository.save(task.copy(taskId = taskId))
    }

    @DeleteMapping("/{taskId}")
    fun deleteTask(@PathVariable taskId: Long) = taskRepository.deleteById(taskId)
}