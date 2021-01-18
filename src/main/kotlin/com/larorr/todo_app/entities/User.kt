package com.larorr.todo_app.entities

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.rest.core.annotation.RestResource
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany


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