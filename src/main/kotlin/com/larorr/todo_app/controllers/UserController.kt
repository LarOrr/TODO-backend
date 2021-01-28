package com.larorr.todo_app.controllers

import com.larorr.todo_app.entities.TodoList
import com.larorr.todo_app.entities.User
import com.larorr.todo_app.repositories.TodoListRepository
import com.larorr.todo_app.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@CrossOrigin
@RequestMapping("/users")
//@RepositoryRestController
class UserController(private val userRepository: UserRepository,
                     private val todoListRepository: TodoListRepository) {

    companion object {
        const val ID_FORMAT: String = "[0-9]+"
        // At least one latin character
        const val LOGIN_FORMAT: String = ".*[a-zA-Z]+.*"
        const val MAIN_LIST_NAME: String = "Main"
    }

    // Если использовать юзера то ломается из-за JsonIgnore
    class LoginInfo(var login: String, var password: String)

    @GetMapping
    fun findAll() = userRepository.findAll()

    @GetMapping("/{id:$ID_FORMAT}")
    fun findUserById(@PathVariable id: Long) = userRepository.findById(id).orElseThrow {
        ResponseStatusException(HttpStatus.NOT_FOUND,
                "User with such id doesn't exist")
    }

    @RequestMapping(value = ["/{id:$ID_FORMAT}"], method = [RequestMethod.HEAD])
    fun existsById(@PathVariable id: Long) = if (userRepository.existsById(id)) null else
        throw ResponseStatusException(HttpStatus.NOT_FOUND,
                "User with such id  doesn't exist")


    @GetMapping("/{login:$LOGIN_FORMAT}")
    fun findUserByLogin(@PathVariable login: String) = userRepository.findByLogin(login) ?:
        throw ResponseStatusException(HttpStatus.NOT_FOUND,
                "Login with such id doesn't exist")


    @RequestMapping(value = ["/{login:$LOGIN_FORMAT}"], method = [RequestMethod.HEAD])
    fun existsByLogin(@PathVariable login: String) = if (userRepository.existsByLogin(login)) null else
        throw ResponseStatusException(HttpStatus.NOT_FOUND,
                "User with such id  doesn't exist")

    @GetMapping("/{userId}/lists")
    fun findUsersLists(@PathVariable userId: Long): List<TodoList> {
        val user = userRepository.findById(userId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with such id ${userId} doesn't exist")
        }
        return user.todoLists
    }

    @GetMapping("/{userId}/mainList")
    fun findUsersMainList(@PathVariable userId: Long): TodoList {
        val user = userRepository.findById(userId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with such id ${userId} doesn't exist")
        }
        // If such list doesn't exist it's server problem
        return user.todoLists.find{ it.name == MAIN_LIST_NAME }!!
    }

    @PostMapping("/login")
    // TODO change to auth with JWT
    fun findIdByLoginAndPassword(@RequestBody loginInfo: LoginInfo): User? {
        return userRepository.findByLoginAndPassword(loginInfo.login, loginInfo.password)
                ?: throw  ResponseStatusException(HttpStatus.NOT_FOUND,
                        "User with such login and password")
    }

    @PostMapping(value = ["", "/register"])
    @ResponseStatus(HttpStatus.CREATED)
    fun createNewUser(@RequestBody loginInfo: LoginInfo): User? {
        val user = User(loginInfo.login, loginInfo.password)
        if (userRepository.existsByLogin(user.login)) {
            throw throw  ResponseStatusException(HttpStatus.CONFLICT,
                    "User with such login already exists")
        }
        // Create Main list for every user
        val mainList = TodoList(MAIN_LIST_NAME, user)
        user.todoLists.add(mainList)
        // First save user because task without user can't exist
        val saveResult = userRepository.save(user)
        todoListRepository.save(mainList)
        return saveResult
    }

    // TODO solve problem with password
//    @PutMapping("/{id}")
//    fun updateUser(@PathVariable id: Long, @RequestBody loginInfo: LoginInfo): User? {
//        val existingUser = userRepository.findById(id).orElseThrow {
//            ResponseStatusException(HttpStatus.NOT_FOUND,
//                    "User with such id")
//        }
//        val user = User(loginInfo.login, loginInfo.password, userId=id)
//        return userRepository.save(user)
//    }

    @DeleteMapping("/{id}")
    fun createNewUser(@PathVariable id: Long) {
        userRepository.deleteById(id)
    }
}