package com.larorr.todo_app.controllers

import com.larorr.todo_app.entities.Task
import com.larorr.todo_app.entities.TodoList
import com.larorr.todo_app.entities.User
import com.larorr.todo_app.repositories.TaskRepository
import com.larorr.todo_app.repositories.TodoListRepository
import com.larorr.todo_app.repositories.UserRepository
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tasks")
class TasksController(private val taskRepository: TaskRepository,
                      private val todoListRepository: TodoListRepository) {
//    TODO repos


    // C: TODO POST new task, ? post new list, post new user
    // R:   TODO Get all (for user), get all for list, get by id,
    // U: PUT new info to task + list? + user?
    // D: DELETE user (=> all his lists and tasks), task, list (=> all tasks)


    @GetMapping("/id/{taskId}")
    fun getTaskById(@PathVariable taskId: Long) : Task? {
        return taskRepository.findById(taskId).orElse(null)
    }

    @GetMapping("/list/{listId}")
    fun getAllByListId(@PathVariable listId: Long) : List<Task> {
        return taskRepository.findAllByTodoList_ListId(listId).toList()
    }

    @GetMapping("/user/{userId}")
    fun getAllByUserId(@PathVariable userId: Long) : List<Task> {
        return taskRepository.findAllByTodoList_User_UserId(userId).toList()
    }
//    fun findAll() = taskRepository.findAll()

//    User("log","pass")

//    @GetMapping("/")
//    fun findAll() = repository.findAllByOrderByAddedAtDesc()
//
//    @GetMapping("/{slug}")
//    fun findOne(@PathVariable slug: String) =
//            repository.findBySlug(slug) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "This article does not exist")

}

@RestController
@RequestMapping("/users")
class UserController(private val userRepository: UserRepository) {

    @GetMapping("/")
    fun findAll() = userRepository.findAll()

    /**
     * If there is no such user return null
     */
    @GetMapping("/id")
    fun findIdByLoginAndPassword(@RequestBody loginInfo: LoginInfo): User? {
        return userRepository.findByLoginAndPassword(loginInfo.login, loginInfo.password)
    }
    // Если использовать юзера то ломается из-за JsonIgnore
    class LoginInfo(var login: String, var password: String)

}

@RestController
@RequestMapping("/lists")
class TodoListController(private val repository: TodoListRepository) {

    @GetMapping("/user/{userId}")
    fun getTasksByList(@PathVariable userId: Long) : List<TodoList> {
        return repository.findAllByUser_UserId(userId).toList()
    }


}
