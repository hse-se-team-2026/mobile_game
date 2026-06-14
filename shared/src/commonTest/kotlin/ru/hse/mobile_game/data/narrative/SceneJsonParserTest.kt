package ru.hse.mobile_game.data.narrative

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SceneJsonParserTest {

    private val parser = SceneJsonParser()

    @Test
    fun parseSceneWithAllFields() {
        val json =
            """
            {
              "id": "scene_01",
              "chapter": 1,
              "background": "market_dusk",
              "text": "You arrive at the market.",
              "choices": [
                {
                  "id": "c1",
                  "text": "Talk to the guard",
                  "requires": { "stat_min": { "cunning": 2 } },
                  "effects": { "relations": { "npc_guard": 1 } },
                  "next_scene": "scene_02"
                }
              ]
            }
            """
                .trimIndent()

        val scene = parser.parseScene(json)
        assertEquals("scene_01", scene.id)
        assertEquals(1, scene.chapter)
        assertEquals("market_dusk", scene.background)
        assertEquals("You arrive at the market.", scene.text)
        assertEquals(1, scene.choices.size)
        assertEquals("c1", scene.choices[0].id)
        assertEquals("scene_02", scene.choices[0].nextSceneId)
        assertEquals(2, scene.choices[0].requires?.statMin?.get("cunning"))
        assertEquals(1, scene.choices[0].effects?.relations?.get("npc_guard"))
    }

    @Test
    fun parseSceneWithNoChoices() {
        val json =
            """
            {
              "id": "scene_end",
              "chapter": 1,
              "background": "sunset",
              "text": "The end."
            }
            """
                .trimIndent()

        val scene = parser.parseScene(json)
        assertEquals("scene_end", scene.id)
        assertTrue(scene.choices.isEmpty())
    }

    @Test
    fun parseSceneWithNoRequirements() {
        val json =
            """
            {
              "id": "scene_01",
              "chapter": 1,
              "background": "bg",
              "text": "Text",
              "choices": [
                {
                  "id": "c1",
                  "text": "Go",
                  "effects": { "stats": { "cunning": 1 } },
                  "next_scene": "scene_02"
                }
              ]
            }
            """
                .trimIndent()

        val scene = parser.parseScene(json)
        assertNull(scene.choices[0].requires)
    }

    @Test
    fun parseChapterIndex() {
        val json =
            """
            {
              "scene_01": "chapter_01/scene_01.json",
              "scene_02": "chapter_01/scene_02.json"
            }
            """
                .trimIndent()

        val index = parser.parseChapterIndex(json)
        assertEquals(2, index.size)
        assertEquals("chapter_01/scene_01.json", index["scene_01"])
    }

    @Test
    fun mapToDomainMapsCorrectly() {
        val sceneJson =
            SceneJson(
                id = "s1",
                chapter = 2,
                background = "bg",
                text = "Hello",
                choices =
                    listOf(
                        ChoiceJson(
                            id = "c1",
                            text = "Go",
                            requires =
                                RequirementsJson(
                                    statMin = mapOf("strength" to 3),
                                    flagsRequired = setOf("flag1"),
                                ),
                            effects =
                                EffectsJson(
                                    stats = mapOf("cunning" to 1),
                                    relations = mapOf("npc" to 5),
                                ),
                            nextSceneId = "s2",
                        )
                    ),
            )

        val domain = parser.mapToDomain(sceneJson)
        assertEquals("s1", domain.id)
        assertEquals(2, domain.chapter)
        assertEquals("bg", domain.backgroundAsset)
        assertEquals("Hello", domain.text)
        assertEquals(1, domain.choices.size)
        assertEquals("c1", domain.choices[0].id)
        assertEquals(3, domain.choices[0].requires?.statMin?.get("strength"))
        assertEquals(1, domain.choices[0].effects.stats["cunning"])
        assertEquals(5, domain.choices[0].effects.relations["npc"])
        assertEquals("s2", domain.choices[0].nextSceneId)
    }

    @Test
    fun parseInvalidJsonThrows() {
        assertFailsWith<IllegalArgumentException> { parser.parseScene("not json") }
    }

    @Test
    fun parseInvalidChapterIndexThrows() {
        assertFailsWith<IllegalArgumentException> { parser.parseChapterIndex("not json") }
    }
}
