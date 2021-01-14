package com.larorr.todo_app.controllers

// TODO add http.not_Found for not existing users and other instead of null
// TODO ResponseStatus(HttpStatus.CREATED)

//TODO add service?

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
@RequestMapping("/tasks")
class TasksController(private val taskRepository: TaskRepository,
                      private val todoListRepository: TodoListRepository) {
//    TODO repos
    // C: TODO POST new task, ? post new list, post new user
    // R:   TODO Get all (for user), get all for list, get by id,
    // U: PUT new info to task + list? + user?
    // D: DELETE user (=> all his lists and tasks), task, list (=> all tasks)


    @GetMapping("/{taskId}")
    fun getTaskById(@PathVariable taskId: Long) : Task? {
        return taskRepository.findById(taskId).orElse(null)
    }

    @GetMapping
    fun getTasks(@RequestParam(required = false) userId: Long?) : Iterable<Task> {
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

//    // TODO Move to list + OneToMany?
//    @GetMapping("/list/{listId}")
//    fun getAllByListId(@PathVariable listId: Long) : List<Task> {
//        return taskRepository.findAllByTodoList_ListId(listId).toList()
//    }
//
//    @GetMapping("/user/{userId}")
//    fun getAllByUserId(@PathVariable userId: Long) : List<Task> {
//        return taskRepository.findAllByTodoList_User_UserId(userId).toList()
//    }


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
@RequestMapping("/lists")
@RepositoryRestController
class TodoListController(private val todoListRepository: TodoListRepository,
                         private val taskRepository: TaskRepository,
                         private val userRepository: UserRepository) {
//// TODO moveto user?
//    @GetMapping("/user/{userId}")
//    fun getTasksByList(@PathVariable userId: Long) : List<TodoList> {
//        return todoListRepository.findAllByUser_UserId(userId).toList()
//    }

//    @GetMapping
//    fun getAllLists(): Iterable<TodoList> = todoListRepository.findAll()

//    class TodoListDTO() {
//
//    }

    @GetMapping("/{listId}")
    fun getListById(@PathVariable listId: Long) : TodoList? {
        // TODO error instead of null
        return todoListRepository.findById(listId).orElse(null)
    }

    @GetMapping
    fun getListByUserId(@RequestParam(required = false) userId: Long?) : Iterable<TodoList> {
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
    fun getTasksOfList(@PathVariable listId: Long) : Iterable<Task> {
        return taskRepository.findAllByTodoList_ListId(listId)
    }

    @PostMapping("/{listId}/tasks")
    fun postTaskToList(@PathVariable listId: Long, @RequestBody task: Task) : Task {
        // TODO fix
        task.todoList = todoListRepository.findById(listId).orElse(null)
        return taskRepository.save(task)
    }

//    @PostMapping("/{listId}/tasks")
//    fun createTask(@PathVariable listId: Long, @RequestBody task: Task) : Iterable<Task> {
////        return taskRepository.save(tas)
//    }

    @PostMapping
    fun createNewList(@RequestBody newList: TodoList): TodoList {
        // TODO а может забить и использовать просто userID: Long?
        // Проблема юзера нужно передовать обязательно с логином
//        val userId = newList.user?.userId
        newList.user = userRepository.findById(newList.userId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with such id ${newList.userId} doesn't exist")
        }
        return todoListRepository.save(newList)
    }

}

// TODO change to auth
@RestController
@RequestMapping("/users")
@RepositoryRestController
class UserController(private val userRepository: UserRepository) {

    @GetMapping
    fun findAll() = userRepository.findAll()

    /**
     * If there is no such user return null
     */
    @PostMapping("/login")
    fun findIdByLoginAndPassword(@RequestBody loginInfo: LoginInfo): User? {
        return userRepository.findByLoginAndPassword(loginInfo.login, loginInfo.password)
    }
    // Если использовать юзера то ломается из-за JsonIgnore
    class LoginInfo(var login: String, var password: String)

}

