package com.larorr.todo_app.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonSetter
import org.springframework.data.rest.core.annotation.RestResource
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Transient

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

    /**
     * Property for resolving json problem
     */
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
