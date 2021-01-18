package com.larorr.todo_app.controllers

import com.larorr.todo_app.entities.TodoList
import com.larorr.todo_app.entities.User
import com.larorr.todo_app.repositories.TodoListRepository
import com.larorr.todo_app.repositories.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
//@RepositoryRestController
@RequestMapping("/users")
class UserController(private val userRepository: UserRepository,
                     private val todoListRepository: TodoListRepository) {

    // Если использовать юзера то ломается из-за JsonIgnore
    class LoginInfo(var login: String, var password: String)

    @GetMapping
    fun findAll() = userRepository.findAll()

    @GetMapping("/{id}")
    fun findUser(@PathVariable id: Long) = userRepository.findById(id).orElseThrow {
        ResponseStatusException(HttpStatus.NOT_FOUND,
                "User with such id")
    }

    @GetMapping("/{userId}/lists")
    fun findUsersLists(@PathVariable userId: Long): List<TodoList> {
        val user = userRepository.findById(userId).orElseThrow {
            ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User with such id ${userId} doesn't exist")
        }
        return user.todoLists
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
        // Create Main list for every user
        val mainList = TodoList("Main", user)
        user.todoLists.add(mainList)
        todoListRepository.save(mainList)
        return userRepository.save(user)
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