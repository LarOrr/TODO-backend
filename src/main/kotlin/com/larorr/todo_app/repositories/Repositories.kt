package com.larorr.todo_app.repositories

import com.larorr.todo_app.entities.Task
import com.larorr.todo_app.entities.TodoList
import com.larorr.todo_app.entities.User
import org.springframework.data.repository.CrudRepository

interface TaskRepository : CrudRepository<Task, Long> {
//    fun findAllByTodoList(todoList: TodoList): Iterable<Task>
    fun findAllByTodoList_ListId(listId: Long): Iterable<Task>
    fun findAllByTodoList_User_UserId(userId: Long): Iterable<Task>
}

interface TodoListRepository : CrudRepository<TodoList, Long> {
    fun findAllByUser_UserId(user_userId: Long): Iterable<TodoList>
}

// TODO change ID
interface UserRepository : CrudRepository<User, Long> {
    fun findByLoginAndPassword(login: String, password: String): User
}