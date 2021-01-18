package com.larorr.todo_app.controllers

import com.larorr.todo_app.entities.Task
import com.larorr.todo_app.entities.TodoList
import com.larorr.todo_app.repositories.TaskRepository
import com.larorr.todo_app.repositories.TodoListRepository
import com.larorr.todo_app.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/lists")
//@RepositoryRestController
class TodoListController(private val todoListRepository: TodoListRepository,
                         private val taskRepository: TaskRepository,
                         private val userRepository: UserRepository) {

    @GetMapping("/{listId}")
    fun getListById(@PathVariable listId: Long): TodoList? {
        return todoListRepository.findById(listId).orElse(null)
    }

    @GetMapping
    fun getListByUserId(@RequestParam(required = false) userId: Long?): Iterable<TodoList> {
        return when (userId) {
            null -> {
                todoListRepository.findAll()
            }
            else -> {
                todoListRepository.findAllByUser_UserId(userId)
            }
        }
    }

    @GetMapping("/{listId}/tasks")
    fun getTasksOfList(@PathVariable listId: Long): Iterable<Task> {
        val list = todoListRepository.findById(listId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "List with such id $listId doesn't exist")
        }
        return list.tasks
    }

    @PostMapping("/{listId}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    fun postTaskToList(@PathVariable listId: Long, @RequestBody task: Task): Task {
        task.todoList = todoListRepository.findById(listId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "List with such id $listId doesn't exist")
        }
        return taskRepository.save(task)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createNewList(@RequestBody newList: TodoList): TodoList {
        if (newList.userId < 0) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "\"userId\" for list object must be present in the request")
        }
        newList.user = userRepository.findById(newList.userId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with such id ${newList.userId} doesn't exist")
        }
        return todoListRepository.save(newList)
    }

    @PutMapping("/{listId}")
    fun updateTask(@PathVariable listId: Long, @RequestBody list: TodoList): TodoList? {
        todoListRepository.findById(listId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "ListId with such id $listId doesn't exist")
        }

        list.user = userRepository.findById(list.userId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with such id ${list.userId} doesn't exist")
        }

        return todoListRepository.save(list.copy(listId = listId))
    }

    @DeleteMapping("/{listId}")
    fun updateTask(@PathVariable listId: Long) {
        todoListRepository.deleteById(listId)
    }
}