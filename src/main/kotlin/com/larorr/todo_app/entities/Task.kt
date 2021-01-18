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
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
        var deadline: Date? = null,
        var isCompleted: Boolean = false,
        @CreationTimestamp
        @Temporal(TemporalType.TIMESTAMP)
        @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
        var creationDate: Date = Date(),
        @ManyToOne
        @JoinColumn(name = "list_id")
        @RestResource
        @JsonIgnore
//        @JsonIdentityReference(alwaysAsId = true)
//        @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class)
        var todoList: TodoList?,
        @Id @GeneratedValue var taskId: Long? = null) {

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
