# TODO Web App backend
Mini project for HSE Kotlin Elective 2020/2021

REST API for TODO web application built with Spring Boot and Kotlin

To build application use ```./gradlew build```
To run application use ```./gradlew bootRun```

Code sections:
- [HTTP Controllers](src/main/kotlin/com/larorr/todo_app/controllers)
- [Entities](src/main/kotlin/com/larorr/todo_app/entities)

## API Call Example
There are 3 entities (resources) - /tasks, /lists (todoLists) and /users. Users have lists, lists have tasks.

Resources support all CRUD operations (POST, GET, PUT, DELETE). 

It is also possible to address some resources in this format "/lists/{listId}/tasks"

**Note**: to create list or task with POST /tasks or POST /lists you must also provide existing "listId" or "userId" 
respectively. That is because tasks can't exist without list and list can't exist without user.

Example call:
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