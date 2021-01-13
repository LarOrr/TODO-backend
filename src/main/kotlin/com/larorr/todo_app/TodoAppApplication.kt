package com.larorr.todo_app

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam


@SpringBootApplication
class TodoAppApplication

fun main(args: Array<String>) {
    runApplication<TodoAppApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}

//@GetMapping("/hello")
//fun sayHello(@RequestParam(value = "myName", defaultValue = "World") name: String?): String? {
//    return String.format("Hello %s!", name)
//}
