package ru.hse.mobile_game.domain.repository

import ru.hse.mobile_game.domain.entity.Scene

interface NarrativeRepository {
    suspend fun getScene(sceneId: String): Scene
    suspend fun getChapterIndex(chapter: Int): Map<String, String>
}
