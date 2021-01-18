package com.larorr.todo_app.repositories

import com.larorr.todo_app.entities.Task
import com.larorr.todo_app.entities.TodoList
import com.larorr.todo_app.entities.User
import org.springframework.data.repository.CrudRepository
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "tasks", path = "tasks")
interface TaskRepository : CrudRepository<Task, Long> {
    fun findAllByTodoList_ListId(listId: Long): Iterable<Task>
    fun findAllByTodoList_User_UserId(userId: Long): Iterable<Task>
}

@RepositoryRestResource(collectionResourceRel = "lists", path = "lists")
interface TodoListRepository : CrudRepository<TodoList, Long> {
    fun findAllByUser_UserId(user_userId: Long): Iterable<TodoList>
}

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
interface UserRepository : CrudRepository<User, Long> {
    fun findByLoginAndPassword(login: String, password: String): User?
}