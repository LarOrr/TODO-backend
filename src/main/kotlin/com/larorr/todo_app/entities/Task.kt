package com.larorr.todo_app.entities

import com.fasterxml.jackson.annotation.*
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.rest.core.annotation.RestResource
import java.util.*
import javax.persistence.*

@Entity
data class Task(
        var name: String,
        var description: String? = null,
        // It's better to get date from frontend
        @JsonFormat(pattern = DATE_FORMAT)
        var deadline: Date? = null,
        var isCompleted: Boolean = false,
        @CreationTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        @JsonFormat(pattern = DATE_FORMAT)
        var creationDate: Date = Date(),
        @ManyToOne
        @JoinColumn(name = "list_id")
        @RestResource
        @JsonIgnore
//        @JsonIdentityReference(alwaysAsId = true)
//        @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class)
        var todoList: TodoList?,
        @Id @GeneratedValue var taskId: Long? = null) {

    companion object {
        const val DATE_FORMAT: String = "yyyy-MM-dd HH:mm:ss"
    }

    /**
     * Property for resolving json problem
     * If it's an existing task, takes existing listId from existing list,
     * otherwise takes field from json
     */
    @Transient
    var listId: Long = -1
        @JsonGetter("listId")
        get() = todoList?.listId ?: field
        @JsonSetter("listId")
        set(value) {
            field = value
        }
}
