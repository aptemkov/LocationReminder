package com.github.aptemkov.firebasetest

data class User(
    var name: String,
    var lastName: String,
    var age: Int,
    var sex: String,
) {
    constructor() : this("Name", "LastName", 0, "None")
}