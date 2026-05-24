package ru.hse.mobile_game

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform