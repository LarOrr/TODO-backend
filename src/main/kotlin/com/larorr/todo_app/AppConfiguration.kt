package com.larorr.todo_app

import com.larorr.todo_app.entities.Task
import com.larorr.todo_app.entities.TodoList
import com.larorr.todo_app.entities.User
import com.larorr.todo_app.repositories.TaskRepository
import com.larorr.todo_app.repositories.TodoListRepository
import com.larorr.todo_app.repositories.UserRepository
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BlogConfiguration {

    @Bean
    fun databaseInitializer(userRepository: UserRepository,
                            todoListRepository: TodoListRepository,
                            taskRepository: TaskRepository) = ApplicationRunner {

        val user1 = userRepository.save(User("user1", "my_pass1"))
        val todoList = todoListRepository.save(TodoList("Main", user1))
        println(user1.userId)
//
        taskRepository.save(Task(
                name = "Buy apples",
//                creationDate = java.util.Calendar.getInstance().time,
                todoList = todoList
        ))

        taskRepository.save(Task(
                name = "Buy oranges ",
//                creationDate = java.util.Calendar.getInstance().time,
                todoList = todoList
        ))

        val todoList2 = todoListRepository.save(TodoList("Big Project", user1))
        taskRepository.save(Task(
                name = "Create github repo",
                description = "Yes",
//                creationDate = java.util.Calendar.getInstance().time,
                todoList = todoList2
        ))

        taskRepository.save(Task(
                name = "Code stuff",
//                creationDate = java.util.Calendar.getInstance().time,
                todoList = todoList2
        ))

        taskRepository.save(Task(
                name = "Test everything",
//                creationDate = java.util.Calendar.getInstance().time,
                todoList = todoList2
        ))

//        taskRepository.save(Task(
//                name = "Test everything 2",
////                creationDate = java.util.Calendar.getInstance().time,
//                todoList = todoList2
//        ))


        val user2 = userRepository.save(User("user2", "my_pass2"))
        val todoList21 = todoListRepository.save(TodoList("Main", user2))
//        println(user1.userId)
//
        taskRepository.save(Task(
                name = "Buy cool apples",
//                creationDate = java.util.Calendar.getInstance().time,
                todoList = todoList21
        ))
    }
}