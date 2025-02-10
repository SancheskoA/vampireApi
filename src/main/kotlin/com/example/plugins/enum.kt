package com.example.plugins

enum class STATUS(val description: String) {
    DONE("Выполнен"),
    NEW("Новый"),
    RESIDENT_FOUND("Житель найден"),
    SEARCH_FOR_RESIDENT("Поиск жителя"),
    ACCEPTED("Принят"),
    CANCELED("Отменен"),
    СOURIER("У курьера")
}