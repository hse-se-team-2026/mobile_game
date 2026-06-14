package ru.hse.mobile_game.data.narrative

import moblile_game.shared.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import ru.hse.mobile_game.domain.entity.Scene
import ru.hse.mobile_game.domain.repository.NarrativeRepository

@OptIn(ExperimentalResourceApi::class)
class NarrativeRepositoryImpl(private val parser: SceneJsonParser = SceneJsonParser()) :
    NarrativeRepository {

    override suspend fun getScene(sceneId: String): Scene {
        val index = loadGlobalIndex()
        val sceneRelativePath =
            index[sceneId]
                ?: throw IllegalArgumentException("Scene '$sceneId' not found in narrative index")
        val sceneContent = Res.readBytes("files/narrative/$sceneRelativePath").decodeToString()
        return parser.mapToDomain(parser.parseScene(sceneContent))
    }

    override suspend fun getChapterIndex(chapter: Int): Map<String, String> {
        val chapterTag = "chapter_${chapter.toString().padStart(2, '0')}"
        val chapterIndexPath = "files/narrative/$chapterTag/index.json"

        return runCatching {
                val chapterIndexContent = Res.readBytes(chapterIndexPath).decodeToString()
                parser.parseChapterIndex(chapterIndexContent)
            }
            .getOrElse { loadGlobalIndex().filterValues { it.startsWith("$chapterTag/") } }
    }

    private suspend fun loadGlobalIndex(): Map<String, String> {
        val content = Res.readBytes("files/narrative/index.json").decodeToString()
        return parser.parseChapterIndex(content)
    }
}
