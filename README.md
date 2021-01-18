# TODO Web App backend
Mini project for HSE Kotlin Eletive 2020/2021

REST API for TODO web application built with Spring Boot and Kotlin

Code sections:
- [HTTP Controllers](src/main/kotlin/com/larorr/todo_app/controllers)
- [Entities](src/main/kotlin/com/larorr/todo_app/entities)

## API Call Example
There are 3 entities (resourses) - tasks, lists (todoLists) and users. Users have lists, lists have tasks.

### <a name="getListsTask"></a> Get all tasks in the list [GET /lists/{listId}/tasks]
+ Response 200 (application/json)
```
[
    {
        "name": "Buy apples",
        "description": null,
        "deadline": null,
        "isCompleted": false,
        "creationDate": "2021-01-18 08:54:25",
        "taskId": 3,
        "listId": 2
    },
    {
        "name": "Buy oranges ",
        "description": null,
        "deadline": null,
        "isCompleted": false,
        "creationDate": "2021-01-18 08:54:25",
        "taskId": 4,
        "listId": 2
    }
]
```

Other possible calls can be checked in [HTTP Controllers files](src/main/kotlin/com/larorr/todo_app/controllers) 