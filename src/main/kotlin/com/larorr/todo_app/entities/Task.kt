package com.larorr.todo_app.entities
// TODO ПРОБЛЕМА - JPA ВОЗВРАЩАЕТ ВСЮ ИНФОРМАЦИЮ!! https://keepgrowing.in/java/springboot/how-to-get-json-response-only-with-an-id-of-the-related-entity/
//  + https://stackoverflow.com/questions/30589257/json-jpa-manytoone-and-onetomany-recursive
// TODO LAZY fetching! (not by default!! https://stackoverflow.com/questions/26601032/default-fetch-type-for-one-to-one-many-to-one-and-one-to-many-in-hibernate)
// TODO USE @CreatedDate
// TODO or try onetomany
// TODO как работает генератор id - для всех?
import com.fasterxml.jackson.annotation.JsonIdentityReference
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.annotation.CreatedDate
import java.util.*
import javax.persistence.*

// check https://www.google.com/search?q=todo+database+schema&newwindow=1&sxsrf=ALeKk023jP_j4OELe2BvVIhTS56ZbTbhDQ:1610482156799&source=lnms&tbm=isch&sa=X&ved=2ahUKEwjMtP3GmZfuAhVpmYsKHQKzCl8Q_AUoAXoECBEQAw&biw=1280&bih=578#imgrc=ELA5RM373CY5KM
// https://stackoverflow.com/questions/28835171/database-schema-for-task-management

// Classes are not 'data' because we use JPA
@Entity
class Task(
        var name: String,
        var description: String? = null,
        var deadline: Date? = null,
//        @CreatedDate var creationDate: Date? = null,

        // TODO add order?
        var isCompleted: Boolean = false,
        // if null -> use main for user?
//        @JsonIdentityReference(alwaysAsId = true)
//        @JsonManagedReference
        @ManyToOne var todoList: TodoList,
//        TODO change to tag or group (just name or Group entity)?
        // TODO проблема не может существовать пустых групп
//        var listName: String = "Main",
//        @ManyToOne var user: User,
        @Id @GeneratedValue var taskId: Long? = null) {

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    // TODO move it to front?
    var creationDate: Date = Date()
}

//@JsonIgnoreProperties(ignoreUnknown = true,
//        value = ["password"])
// TODO don't return password
@Entity
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties(ignoreUnknown = true)
class User(
        var login: String,
//        @get:JsonIgnore // @JsonProperty(value = "user_password")
//        @JsonProperty(access = Access.WRITE_ONLY)
        // @get:JsonIgnore
        @JsonIgnore
        var password: String,
        // using generated id https://youtrack.jetbrains.com/issue/KT-6653
        @Id @GeneratedValue var userId: Long? = null
)

@Entity
class TodoList(
        var name: String,
//        @JsonIdentityReference(alwaysAsId = true)
        @ManyToOne var user: User,
        @Id @GeneratedValue var listId: Long? = null
)