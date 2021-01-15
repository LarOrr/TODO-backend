package com.larorr.todo_app.controllers

// TODO ResponseStatus(HttpStatus.CREATED)
// TODO add service?

import com.larorr.todo_app.entities.Task
import com.larorr.todo_app.entities.TodoList
import com.larorr.todo_app.entities.User
import com.larorr.todo_app.repositories.TaskRepository
import com.larorr.todo_app.repositories.TodoListRepository
import com.larorr.todo_app.repositories.UserRepository
import org.springframework.data.rest.webmvc.RepositoryRestController
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
//@RepositoryRestController
@RequestMapping("/tasks")
class TasksController(private val taskRepository: TaskRepository,
                      private val todoListRepository: TodoListRepository) {

    @GetMapping("/{taskId}")
    fun getTaskById(@PathVariable taskId: Long): Task? {
        return taskRepository.findById(taskId).orElse(null)
    }

//    @GetMapping("/{taskId}/list")
//    fun getListByTask(@PathVariable taskId: Long) : Task? {
//        return taskRepository.findById(taskId).orElse(null)
//    }

    @GetMapping
    fun getTasks(@RequestParam(required = false) userId: Long?): Iterable<Task> {
        //TODO other way / move to user / delete for all
        return when (userId) {
            null -> {
                taskRepository.findAll()
            }
            else -> {
                taskRepository.findAllByTodoList_User_UserId(userId)
            }
        }
    }

    @PostMapping
    fun createTask(@RequestBody task: Task): Task? {
        // TODO check for -1
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

        task.todoList = todoListRepository.findById(task.listId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "List with such id ${task.listId} doesn't exist")
        }

        return taskRepository.save(task.copy(taskId = taskId))
    }

    @DeleteMapping("/{taskId}")
    fun deleteTask(@PathVariable taskId: Long) = taskRepository.deleteById(taskId)

}


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
        return taskRepository.findAllByTodoList_ListId(listId)
    }

    @PostMapping("/{listId}/tasks")
    fun postTaskToList(@PathVariable listId: Long, @RequestBody task: Task): Task {
        task.todoList = todoListRepository.findById(listId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "List with such id $listId doesn't exist")
        }
        return taskRepository.save(task)
    }

    @PostMapping
    fun createNewList(@RequestBody newList: TodoList): TodoList {
        newList.user = userRepository.findById(newList.userId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with such id ${newList.userId} doesn't exist")
        }
        return todoListRepository.save(newList)
    }

    @PutMapping("/{taskId}")
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

// TODO change to auth
@RestController
//@RepositoryRestController
@RequestMapping("/users")
class UserController(private val userRepository: UserRepository,
                     private val todoListRepository: TodoListRepository) {

    @GetMapping
    fun findAll() = userRepository.findAll()

    @GetMapping("/{id}")
    fun findUser(@PathVariable id: Long) = userRepository.findById(id)

    @GetMapping("/{userId}/lists")
    fun findUsersLists(@PathVariable userId: Long): List<TodoList> {
        val user = userRepository.findById(userId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with such id ${userId} doesn't exist")
        }
        return user.todoLists
    }

    /**
     * If there is no such user return null
     */
    @PostMapping("/login")
    fun findIdByLoginAndPassword(@RequestBody loginInfo: LoginInfo): User? {
        return userRepository.findByLoginAndPassword(loginInfo.login, loginInfo.password)
    }

    //    @PostMapping
    @PostMapping(value = ["", "/register"])
    fun createNewUser(@RequestBody loginInfo: LoginInfo): User? {
        val user = User(loginInfo.login, loginInfo.password)
        val mainList = TodoList("Main", user)
        user.todoLists.add(mainList)
        todoListRepository.save(mainList)
        return userRepository.save(user)
    }

    // Если использовать юзера то ломается из-за JsonIgnore
    class LoginInfo(var login: String, var password: String)

    @DeleteMapping("/{id}")
    fun createNewUser(@PathVariable id: Long) {
        userRepository.deleteById(id)
    }
}

// TODO разделить по файлам
// TODO reformat