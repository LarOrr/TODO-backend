package com.larorr.todo_app.entities
// TODO ПРОБЛЕМА - JPA ВОЗВРАЩАЕТ ВСЮ ИНФОРМАЦИЮ!! https://keepgrowing.in/java/springboot/how-to-get-json-response-only-with-an-id-of-the-related-entity/
//  + https://stackoverflow.com/questions/30589257/json-jpa-manytoone-and-onetomany-recursive
// TODO LAZY fetching! (not by default!! https://stackoverflow.com/questions/26601032/default-fetch-type-for-one-to-one-many-to-one-and-one-to-many-in-hibernate)
// TODO USE @CreatedDate
// TODO cascade
import com.fasterxml.jackson.annotation.*
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.rest.core.annotation.RestResource
import java.util.*
import javax.persistence.*
import kotlin.properties.Delegates

// check https://www.google.com/search?q=todo+database+schema&newwindow=1&sxsrf=ALeKk023jP_j4OELe2BvVIhTS56ZbTbhDQ:1610482156799&source=lnms&tbm=isch&sa=X&ved=2ahUKEwjMtP3GmZfuAhVpmYsKHQKzCl8Q_AUoAXoECBEQAw&biw=1280&bih=578#imgrc=ELA5RM373CY5KM
// https://stackoverflow.com/questions/28835171/database-schema-for-task-management

// Classes are not 'data' because we use JPA
@Entity
data class Task(
        var name: String,
        var description: String? = null,
//        var deadline: Date? = null,
        var isCompleted: Boolean = false,
        @ManyToOne
        @JoinColumn(name = "list_id")
        @RestResource
        @JsonIgnore
        var todoList: TodoList?,
        @Id @GeneratedValue var taskId: Long? = null) {

//    @CreationTimestamp
//    @Temporal(TemporalType.TIMESTAMP)
//    // TODO move it to front?
//    var creationDate: Date = Date()

    @Transient
    var listId: Long = -1
        @JsonGetter("listId")
        get() = todoList?.listId ?: field
        @JsonSetter("listId")
        set(value) {
            field = value
        }
}

@Entity
data class TodoList(
        var name: String,
        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        @JsonIgnore
        @RestResource(path = "user")
        var user: User?,
        @Id @GeneratedValue var listId: Long? = null
) {
    // TODO вернуть как было, почемуб его не возвращать, А СЕТТЕР ИГНОРИТЬ И ИСПОЛЬЗОВАТЬ
    // СВОЙ СЕТТЕР для этого
//    @JsonGetter("userId")
//    private fun getUserId() = user?.userId

    @Transient
    var userId: Long = -1
        @JsonGetter("userId")
        get() = user?.userId ?: field
        @JsonSetter("userId")
        set(value) {
            field = value
        }

    @JsonBackReference
    @OneToMany(mappedBy = "todoList", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    @RestResource(path = "tasks", rel = "tasks")
    var tasks: MutableList<Task> = mutableListOf()
}

@Entity
data class User(
        @Column(unique = true)
        var login: String,
        @JsonIgnore
        var password: String,
        @Id @GeneratedValue var userId: Long? = null,

        ) {
    @JsonBackReference
    @OneToMany(mappedBy = "user", cascade = [CascadeType.REMOVE], fetch = FetchType.LAZY)
    @RestResource(path = "lists", rel = "lists")
    var todoLists: MutableList<TodoList> = mutableListOf()
}

