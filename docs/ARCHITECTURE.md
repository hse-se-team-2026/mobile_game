# Архитектура

## Паттерн

**Clean Architecture + MVVM.** Три слоя: Presentation, Domain, Data. Весь код живёт в единственном Gradle-модуле `shared` (commonMain / androidMain / iosMain). UI на Android запускается через `androidApp`, на iOS — через `iosApp` (Xcode).

## Структура модулей

```
project/
├── shared/                     # KMP модуль — вся логика, данные и UI
│   └── src/
│       ├── commonMain/         # Domain + Data + Presentation (UI)
│       ├── androidMain/        # Android expect/actual (SQLite driver, DataStore, currentTimeMillis)
│       ├── iosMain/            # iOS expect/actual (SQLite driver, DataStore, currentTimeMillis)
│       ├── commonTest/         # Общие unit-тесты
│       ├── androidHostTest/    # Android-специфичные тесты
│       └── iosTest/            # iOS-специфичные тесты
├── androidApp/                 # Android entry point (MainActivity, GameApplication)
└── iosApp/                     # Xcode-проект (обёртка для iOS)
```

Модуль `shared` содержит всю бизнес-логику, данные и UI-код в `commonMain`. Платформо-зависимые реализации (драйверы БД, DataStore, получение текущего времени) вынесены в `androidMain` и `iosMain` через механизм `expect/actual`. `androidApp` — минимальная обёртка с `MainActivity` и `GameApplication` (инициализация Koin). `iosApp` — Xcode-проект, использующий `ComposeUIViewController` для запуска Compose UI.

## Слои внутри shared/commonMain

```
Presentation (screen/, navigation/)
   ↓ зависит от
Domain (entity, repository interfaces, usecases)
   ↑ зависит от
Data (repository impl, SQLDelight DAO, JSON parser)
```

Presentation-слой (Compose-экраны, ViewModels) обращается к Domain-слою через use cases. Data-слой реализует интерфейсы, объявленные в Domain. Domain не зависит ни от платформы, ни от фреймворков — только чистый Kotlin и kotlinx.serialization для аннотации `@Serializable`.

## Структура пакетов

```
// shared/src/commonMain/kotlin/ru/hse/mobile_game/
ru.hse.mobile_game/
├── domain/
│   ├── entity/
│   │   ├── GameState.kt
│   │   ├── Character.kt
│   │   └── Scene.kt
│   ├── repository/
│   │   ├── NarrativeRepository.kt
│   │   ├── SaveRepository.kt
│   │   └── SettingsRepository.kt
│   └── usecase/
│       ├── LoadSceneUseCase.kt
│       ├── MakeChoiceUseCase.kt
│       ├── EvaluateConditionsUseCase.kt
│       ├── SaveGameUseCase.kt
│       └── LoadGameUseCase.kt
├── data/
│   ├── narrative/
│   │   ├── NarrativeRepositoryImpl.kt
│   │   ├── NarrativeModels.kt
│   │   └── SceneJsonParser.kt
│   ├── database/
│   │   ├── SaveRepositoryImpl.kt
│   │   └── DatabaseDriverFactory.kt
│   └── settings/
│       ├── DataStoreFactory.kt
│       └── SettingsRepositoryImpl.kt
├── di/
│   └── AppModule.kt
├── navigation/
│   ├── NavGraph.kt
│   └── Screen.kt
└── screen/
    ├── mainmenu/MainMenuScreen.kt
    ├── origin/OriginSelectScreen.kt
    ├── game/
    │   ├── GameScreen.kt
    │   ├── GameViewModel.kt
    │   ├── Glossary.kt
    │   ├── FlagRegistry.kt
    │   ├── NpcRegistry.kt
    │   ├── RichTextParser.kt
    │   └── CurrentTimeMillis.kt
    ├── character/CharacterSheet.kt
    ├── save/
    │   ├── SaveLoadScreen.kt
    │   └── SaveLoadViewModel.kt
    └── model/
        ├── GameUiState.kt
        └── SaveLoadUiState.kt
```

Пакет `domain/entity` содержит чистые data-классы игрового состояния. `domain/repository` — интерфейсы, через которые Domain-слой абстрагирует доступ к данным. `domain/usecase` — бизнес-логика (каждый use case — один класс, один `operator fun invoke`). `data/` — конкретные реализации репозиториев (JSON-парсер, SQLDelight, DataStore). `screen/` — Compose UI и ViewModels, сгруппированные по экранам. `navigation/` — граф переходов и sealed-class маршрутов.

---

## Domain Layer — Сущности

### GameState

```kotlin
@Serializable
data class GameState(
    val character: Character,
    val currentSceneId: String,
    val chapter: Int,
    val choiceHistory: List<String> = emptyList(),
    val timestamp: Long,
)
```

Полное состояние игры. `character` — текущий персонаж с характеристиками и флагами. `currentSceneId` — ID сцены, на которой находится игрок. `choiceHistory` — список ID всех сделанных выборов (для аналитики и сюжетных развилок). `timestamp` — время последнего действия, используется для сортировки сохранений. Аннотация `@Serializable` позволяет сериализовать весь стейт в JSON для хранения в SQLite.

### Character и Stats

```kotlin
@Serializable
data class Character(
    val id: String,
    val origin: String,
    val stats: Stats,
    val relations: Map<String, Int> = emptyMap(),
    val factionStandings: Map<String, Int> = emptyMap(),
    val flags: Set<String> = emptySet(),
    val taint: Int = 0,
)

@Serializable
data class Stats(
    val strength: Int = 0,
    val cunning: Int = 0,
    val wisdom: Int = 0,
    val charisma: Int = 0,
)
```

`Character` — модель персонажа. `origin` — одно из четырёх происхождений (noble, merchant, soldier, scholar), определяет стартовые `Stats` и доступность отдельных выборов. `relations` — отношения с NPC: ключ — ID из `NpcRegistry` (например `"guard_captain"`), значение — числовой уровень. `flags` — набор сюжетных флагов, который растёт по мере прохождения: определяет видимость выборов, разблокировку глоссарных записей и ветвление сюжета. `taint` — уровень «порчи» персонажа (зарезервирован для будущих механик). `Stats` — четыре характеристики, каждая может увеличиваться или уменьшаться через эффекты выборов.

### Scene, Choice, Requirements, Effects

```kotlin
data class Scene(
    val id: String,
    val title: String,
    val chapter: Int,
    val backgroundAsset: String,
    val text: String,
    val choices: List<Choice> = emptyList(),
)

data class Choice(
    val id: String,
    val text: String,
    val requires: Requirements?,
    val effects: Effects,
    val nextSceneId: String,
)

data class Requirements(
    val statMin: Map<String, Int> = emptyMap(),
    val flagsRequired: Set<String> = emptySet(),
    val flagsForbidden: Set<String> = emptySet(),
    val originRequired: Set<String> = emptySet(),
)

data class Effects(
    val stats: Map<String, Int> = emptyMap(),
    val relations: Map<String, Int> = emptyMap(),
    val factionStandings: Map<String, Int> = emptyMap(),
    val flags: Set<String> = emptySet(),
)
```

`Scene` — нарративная сцена. `title` — название (отображается в верхней панели), `backgroundAsset` — имя фона без префикса `bg_` (например `"city_gate"` → `bg_city_gate.webp`), `text` — нарративный текст с markdown-разметкой (`*italic*` для рассказчика, `Speaker: "dialogue"` для диалогов). `choices` — варианты выбора.

`Choice` — вариант действия. `requires` — условия доступности (если null — доступен всем). `effects` — последствия. `nextSceneId` — ID следующей сцены (или `"end"` для окончания).

`Requirements` — четыре типа условий: `statMin` (минимальные характеристики — блокируют выбор, но он видим), `flagsRequired` / `flagsForbidden` (скрывают выбор целиком), `originRequired` (скрывает, если происхождение не совпадает).

`Effects` — дельты: `stats` (`"wisdom": 1` → +1 к мудрости), `relations` (`"guard_captain": -2` → ухудшение отношений), `flags` — новые флаги, добавляемые в `character.flags`.

---

## Domain Layer — Интерфейсы репозиториев

### NarrativeRepository

```kotlin
interface NarrativeRepository {
    suspend fun getScene(sceneId: String): Scene
    suspend fun getChapterIndex(chapter: Int): Map<String, String>
}
```

Абстракция доступа к нарративному контенту. `getScene` загружает сцену по ID. `getChapterIndex` возвращает маппинг `sceneId → путь к JSON-файлу` для указанной главы. Обе операции `suspend`, т.к. работают с файловой системой через Compose Resources.

### SaveRepository

```kotlin
interface SaveRepository {
    suspend fun getAll(): List<SaveSlot>
    suspend fun upsert(slot: SaveSlot)
    suspend fun deleteById(id: Long)
    fun serializeState(state: GameState): String
    fun deserializeState(json: String): GameState
}

data class SaveSlot(
    val id: Long,
    val name: String,
    val gameState: GameState,
    val chapter: Int,
    val timestamp: Long,
    val previewText: String?,
)
```

Контракт для работы с сохранениями. `getAll` возвращает все слоты, отсортированные по времени. `upsert` — вставка или обновление (INSERT OR REPLACE). `serializeState` / `deserializeState` — конвертация `GameState ↔ JSON` (GameState хранится в столбце `game_state` как текст). `SaveSlot` — доменная модель слота: `id = 0` зарезервирован для автосохранения, ручные используют `currentTimeMillis()`.

### SettingsRepository

```kotlin
interface SettingsRepository {
    suspend fun getMusicVolume(): Float
    suspend fun setMusicVolume(volume: Float)
    suspend fun getSfxVolume(): Float
    suspend fun setSfxVolume(volume: Float)
    suspend fun getLanguage(): String
    suspend fun setLanguage(lang: String)
}
```

Интерфейс пользовательских настроек (громкость, язык). Подготовлен для будущего экрана настроек. Реализуется через DataStore KMP.

---

## Domain Layer — Use Cases

### LoadSceneUseCase

```kotlin
class LoadSceneUseCase(private val narrativeRepository: NarrativeRepository) {
    suspend operator fun invoke(sceneId: String): Scene {
        return narrativeRepository.getScene(sceneId)
    }
}
```

Тонкая обёртка над `NarrativeRepository.getScene()`. Существует как отдельный класс для единообразия инъекции через Koin и соблюдения принципа единой ответственности: если в будущем понадобится кэширование или валидация — логика добавляется сюда, не затрагивая репозиторий.

### MakeChoiceUseCase

```kotlin
class MakeChoiceUseCase(private val evaluateConditions: EvaluateConditionsUseCase) {

    operator fun invoke(currentState: GameState, choice: Choice): GameState {
        check(evaluateConditions.isChoiceAvailable(choice, currentState.character)) {
            "Character does not meet requirements for choice '${choice.id}'"
        }

        val updatedCharacter = applyEffects(currentState.character, choice.effects)

        return currentState.copy(
            character = updatedCharacter,
            currentSceneId = choice.nextSceneId,
            choiceHistory = currentState.choiceHistory + choice.id,
        )
    }

    private fun applyEffects(character: Character, effects: Effects): Character {
        return character.copy(
            stats = applyStatEffects(character.stats, effects.stats),
            relations = mergeIntMaps(character.relations, effects.relations),
            factionStandings = mergeIntMaps(character.factionStandings, effects.factionStandings),
            flags = character.flags + effects.flags,
        )
    }

    private fun applyStatEffects(stats: Stats, statEffects: Map<String, Int>): Stats {
        if (statEffects.isEmpty()) return stats
        return Stats(
            strength = stats.strength + (statEffects["strength"] ?: 0),
            cunning = stats.cunning + (statEffects["cunning"] ?: 0),
            wisdom = stats.wisdom + (statEffects["wisdom"] ?: 0),
            charisma = stats.charisma + (statEffects["charisma"] ?: 0),
        )
    }

    private fun mergeIntMaps(base: Map<String, Int>, delta: Map<String, Int>): Map<String, Int> {
        if (delta.isEmpty()) return base
        val result = base.toMutableMap()
        for ((key, value) in delta) {
            result[key] = (result[key] ?: 0) + value
        }
        return result
    }
}
```

Центральная бизнес-логика обработки выбора игрока. Работает синхронно (не `suspend`), т.к. не обращается к IO.

1. **Валидация** — `check()` проверяет все условия через `EvaluateConditionsUseCase.isChoiceAvailable()`. Если игрок каким-то образом нажмёт на заблокированный выбор, use case бросит `IllegalStateException`.
2. **Применение эффектов** — `applyEffects()` создаёт копию персонажа с обновлёнными полями:
   - `applyStatEffects()` — прибавляет дельты к каждой из 4 характеристик
   - `mergeIntMaps()` — аддитивно мержит дельты отношений и фракций (если ключ уже есть — суммирует, если нет — добавляет)
   - `flags` — объединение множеств (Set + Set)
3. **Новый GameState** — копия с обновлённым персонажем, новым `currentSceneId` и добавленным в `choiceHistory` ID выбора.

### EvaluateConditionsUseCase

```kotlin
class EvaluateConditionsUseCase {

    fun isChoiceVisible(choice: Choice, character: Character): Boolean {
        val requirements = choice.requires ?: return true
        return checkOriginRequirement(requirements, character.origin) &&
            checkRequiredFlags(requirements, character.flags) &&
            checkForbiddenFlags(requirements, character.flags)
    }

    fun isChoiceAvailable(choice: Choice, character: Character): Boolean {
        val requirements = choice.requires ?: return true
        return checkStatRequirements(requirements, character.stats) &&
            checkRequiredFlags(requirements, character.flags) &&
            checkForbiddenFlags(requirements, character.flags) &&
            checkOriginRequirement(requirements, character.origin)
    }

    fun failsOnlyOnStats(choice: Choice, character: Character): Boolean {
        val requirements = choice.requires ?: return false
        val statOk = checkStatRequirements(requirements, character.stats)
        val originOk = checkOriginRequirement(requirements, character.origin)
        val flagsOk = checkRequiredFlags(requirements, character.flags) &&
                checkForbiddenFlags(requirements, character.flags)
        return !statOk && originOk && flagsOk
    }

    private fun checkStatRequirements(requirements: Requirements, stats: Stats): Boolean {
        return requirements.statMin.all { (statName, minValue) ->
            getStatValue(stats, statName) >= minValue
        }
    }

    private fun getStatValue(stats: Stats, statName: String): Int {
        return when (statName.lowercase()) {
            "strength" -> stats.strength
            "cunning" -> stats.cunning
            "wisdom" -> stats.wisdom
            "charisma" -> stats.charisma
            else -> 0
        }
    }
}
```

Разделяет два уровня проверки условий:

- **`isChoiceVisible()`** — определяет, видит ли игрок этот вариант. Проверяет только origin и флаги. Если не проходит — выбор полностью скрыт. Это нужно, чтобы солдат не видел аристократических вариантов, а игрок без определённого знания — вариантов, завязанных на этом знании.
- **`isChoiceAvailable()`** — определяет, может ли игрок выбрать вариант. Проверяет всё, включая `statMin`. Если stat-условия не выполнены — вариант виден, но заблокирован с подсказкой (например «charisma ≥ 4»).
- **`failsOnlyOnStats()`** — вспомогательный метод: возвращает true, если единственная причина блокировки — недостаточные характеристики. Используется для решения, показывать ли stat-хинт.

`getStatValue()` маппит строковое имя характеристики на поле `Stats`. Неизвестные имена дают 0, что эквивалентно «условие не выполнено».

### SaveGameUseCase

```kotlin
class SaveGameUseCase(private val saveRepository: SaveRepository) {
    suspend operator fun invoke(
        slotId: Long,
        name: String,
        gameState: GameState,
        previewText: String? = null,
    ) {
        val slot = SaveSlot(
            id = slotId,
            name = name,
            gameState = gameState,
            chapter = gameState.chapter,
            timestamp = gameState.timestamp,
            previewText = previewText,
        )
        saveRepository.upsert(slot)
    }
}
```

Формирует `SaveSlot` из переданных параметров и вызывает `upsert`. `previewText` — первые 100 символов текста текущей сцены, для отображения в списке сохранений. Авто-сохранение вызывает этот use case с `slotId = 0`, ручные — с `currentTimeMillis()`.

### LoadGameUseCase

```kotlin
class LoadGameUseCase(private val saveRepository: SaveRepository) {
    suspend operator fun invoke(): List<SaveSlot> {
        return saveRepository.getAll()
    }

    suspend fun deleteSlot(slotId: Long) {
        saveRepository.deleteById(slotId)
    }
}
```

Загружает все сохранения (отсортированы по `timestamp DESC` на уровне SQL). Также предоставляет удаление слота. Фильтрация конкретного слота по ID происходит в ViewModel.

---

## Data Layer — Narrative

### NarrativeModels

```kotlin
@Serializable
data class SceneJson(
    val id: String,
    val title: String = "",
    val chapter: Int,
    val background: String,
    val text: String,
    val choices: List<ChoiceJson> = emptyList(),
)

@Serializable
data class ChoiceJson(
    val id: String,
    val text: String,
    val requires: RequirementsJson? = null,
    val effects: EffectsJson? = null,
    @SerialName("next_scene") val nextSceneId: String,
)

@Serializable
data class RequirementsJson(
    @SerialName("stat_min") val statMin: Map<String, Int> = emptyMap(),
    @SerialName("flags_required") val flagsRequired: Set<String> = emptySet(),
    @SerialName("flags_forbidden") val flagsForbidden: Set<String> = emptySet(),
    @SerialName("origin_required") val originRequired: Set<String> = emptySet(),
)

@Serializable
data class EffectsJson(
    val stats: Map<String, Int> = emptyMap(),
    val relations: Map<String, Int> = emptyMap(),
    @SerialName("faction_standings") val factionStandings: Map<String, Int> = emptyMap(),
    val flags: Set<String> = emptySet(),
)
```

DTO-классы для десериализации JSON-файлов сцен. Используют `@SerialName` для маппинга snake_case полей JSON на camelCase Kotlin-свойства (например `"next_scene"` → `nextSceneId`). Все поля с коллекциями имеют значения по умолчанию (`emptyMap()`, `emptySet()`), чтобы в JSON можно было опускать необязательные секции — `effects` или `requires` могут отсутствовать.

### SceneJsonParser

```kotlin
class SceneJsonParser(private val json: Json = Json { ignoreUnknownKeys = true }) {

    fun parseScene(jsonString: String): SceneJson {
        return try {
            json.decodeFromString(SceneJson.serializer(), jsonString)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to parse scene JSON: ${e.message}", e)
        }
    }

    fun parseChapterIndex(jsonString: String): Map<String, String> {
        return try {
            json.decodeFromString<Map<String, String>>(jsonString)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to parse chapter index JSON: ${e.message}", e)
        }
    }

    fun mapToDomain(sceneJson: SceneJson): Scene {
        return Scene(
            id = sceneJson.id,
            title = sceneJson.title.ifEmpty { sceneJson.id },
            chapter = sceneJson.chapter,
            backgroundAsset = sceneJson.background,
            text = sceneJson.text,
            choices = sceneJson.choices.map { mapChoiceToDomain(it) },
        )
    }

    private fun mapChoiceToDomain(choiceJson: ChoiceJson): Choice { ... }
    private fun mapRequirementsToDomain(requirementsJson: RequirementsJson): Requirements { ... }
    private fun mapEffectsToDomain(effectsJson: EffectsJson): Effects { ... }
}
```

Два этапа обработки: парсинг (JSON → DTO) и маппинг (DTO → Domain entity). `ignoreUnknownKeys = true` защищает от поломки при добавлении новых полей в JSON. `parseScene` и `parseChapterIndex` оборачивают исключения десериализации в `IllegalArgumentException` с понятным сообщением. `mapToDomain` конвертирует `SceneJson → Scene`, при этом если `title` пустой — используется `id` сцены как fallback.

### NarrativeRepositoryImpl

```kotlin
@OptIn(ExperimentalResourceApi::class)
class NarrativeRepositoryImpl(private val parser: SceneJsonParser = SceneJsonParser()) :
    NarrativeRepository {

    override suspend fun getScene(sceneId: String): Scene {
        val index = loadGlobalIndex()
        val sceneRelativePath = index[sceneId]
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
```

Реализация `NarrativeRepository`, работающая через Compose Resources (`Res.readBytes()`). Этот API предоставляет единый доступ к файлам из `composeResources/files/` на обеих платформах.

`getScene()`: загружает глобальный `index.json` (маппинг `sceneId → "chapter_01/scene_01.json"`), находит путь к файлу, читает содержимое и конвертирует через парсер.

`getChapterIndex()`: пытается загрузить `chapter_XX/index.json`, при ошибке (файл не найден) использует fallback — фильтрует глобальный индекс по префиксу главы.

---

## Data Layer — Persistence (SQLDelight)

### Схема БД

```sql
-- shared/src/commonMain/sqldelight/com/example/ashesofgods/SaveSlot.sq

CREATE TABLE SaveSlot (
    id           INTEGER PRIMARY KEY,
    name         TEXT    NOT NULL,
    game_state   TEXT    NOT NULL,
    chapter      INTEGER NOT NULL,
    timestamp    INTEGER NOT NULL,
    preview_text TEXT
);

selectAll:
SELECT * FROM SaveSlot ORDER BY timestamp DESC;

upsert:
INSERT OR REPLACE INTO SaveSlot VALUES (?, ?, ?, ?, ?, ?);

deleteById:
DELETE FROM SaveSlot WHERE id = ?;
```

Единственная таблица `SaveSlot`. Колонка `game_state` хранит весь `GameState` в сериализованном JSON — это позволяет полностью восстановить игру из любого слота. `id = 0` зарезервирован для автосохранения; при каждом выборе автосейв перезаписывается (INSERT OR REPLACE). Ручные сохранения получают `id = currentTimeMillis()`, что гарантирует уникальность. Сортировка `ORDER BY timestamp DESC` — последние сохранения наверху.

### SaveRepositoryImpl

```kotlin
class SaveRepositoryImpl(
    private val database: AppDatabase,
    private val json: Json = Json { ignoreUnknownKeys = true },
) : SaveRepository {

    private val queries = database.saveSlotQueries

    override suspend fun getAll(): List<SaveSlot> {
        return queries.selectAll().executeAsList().map { entity ->
            SaveSlot(
                id = entity.id,
                name = entity.name,
                gameState = deserializeState(entity.game_state),
                chapter = entity.chapter.toInt(),
                timestamp = entity.timestamp,
                previewText = entity.preview_text,
            )
        }
    }

    override suspend fun upsert(slot: SaveSlot) {
        queries.upsert(
            id = slot.id,
            name = slot.name,
            game_state = serializeState(slot.gameState),
            chapter = slot.chapter.toLong(),
            timestamp = slot.timestamp,
            preview_text = slot.previewText,
        )
    }

    override fun serializeState(state: GameState): String = json.encodeToString(state)
    override fun deserializeState(json: String): GameState = this.json.decodeFromString(json)
}
```

Работает через сгенерированные SQLDelight queries (`database.saveSlotQueries`). `getAll()` десериализует JSON из столбца `game_state` обратно в `GameState`. `upsert()` сериализует `GameState` в JSON и вызывает INSERT OR REPLACE. Конвертация `chapter: Int ↔ Long` нужна из-за типа `INTEGER` в SQLite (всегда Long на уровне SQLDelight).

### DatabaseDriverFactory (expect/actual)

```kotlin
// shared/commonMain
expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

// shared/androidMain
actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver =
        AndroidSqliteDriver(AppDatabase.Schema, context, "game.db")
}

// shared/iosMain
actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver =
        NativeSqliteDriver(AppDatabase.Schema, "game.db")
}
```

Платформенная абстракция для создания SQL-драйвера. На Android используется `AndroidSqliteDriver` (нужен `Context`). На iOS — `NativeSqliteDriver` (без дополнительных зависимостей). Файл БД `game.db` создаётся автоматически в песочнице приложения.

---

## Data Layer — Settings (DataStore)

### SettingsRepositoryImpl

```kotlin
class SettingsRepositoryImpl(private val dataStore: DataStore<Preferences>) : SettingsRepository {

    companion object {
        private val MUSIC_VOLUME_KEY = floatPreferencesKey("music_volume")
        private val SFX_VOLUME_KEY = floatPreferencesKey("sfx_volume")
        private val LANGUAGE_KEY = stringPreferencesKey("language")

        private const val DEFAULT_MUSIC_VOLUME = 0.7f
        private const val DEFAULT_SFX_VOLUME = 0.8f
        private const val DEFAULT_LANGUAGE = "en"
    }

    override suspend fun getMusicVolume(): Float {
        return dataStore.data.first()[MUSIC_VOLUME_KEY] ?: DEFAULT_MUSIC_VOLUME
    }

    override suspend fun setMusicVolume(volume: Float) {
        dataStore.edit { preferences -> preferences[MUSIC_VOLUME_KEY] = volume }
    }

    // аналогично для sfxVolume и language
}
```

Реализация настроек через `DataStore<Preferences>`. Каждый параметр — типизированный ключ (`floatPreferencesKey`, `stringPreferencesKey`). Чтение — `dataStore.data.first()[KEY]` с дефолтным значением. Запись — `dataStore.edit {}`. DataStore автоматически персистит данные в файл.

### DataStoreFactory (expect/actual)

На Android создаётся через `PreferencesDataStoreFactory` с `context.filesDir`. На iOS — через `PreferenceDataStoreFactory.createWithPath` с путём в `NSDocumentDirectory` (используется `NSFileManager` через ExperimentalForeignApi).

---

## Presentation Layer

### Навигация: Screen.kt

```kotlin
@Serializable
sealed class Screen {
    @Serializable data object MainMenu : Screen()
    @Serializable data object OriginSelect : Screen()
    @Serializable data class Game(val origin: String? = null, val slotId: Long? = null) : Screen()
    @Serializable data object SaveLoad : Screen()
}
```

Четыре маршрута. `Game` принимает либо `origin` (новая игра — персонаж создаётся с нуля), либо `slotId` (загрузка — стейт восстанавливается из БД). Оба nullable, т.к. передаётся только один из двух. `@Serializable` на sealed class — type-safe навигация без ручного парсинга аргументов (Navigation Compose KMP).

### Навигация: NavGraph.kt

```kotlin
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.MainMenu) {

        composable<Screen.MainMenu> {
            MainMenuScreen(
                onNewGame = { navController.navigate(Screen.OriginSelect) },
                onLoadGame = { navController.navigate(Screen.SaveLoad) },
            )
        }

        composable<Screen.OriginSelect> {
            OriginSelectScreen(
                onOriginSelected = { origin ->
                    navController.navigate(Screen.Game(origin = origin)) {
                        popUpTo<Screen.MainMenu>()
                    }
                }
            )
        }

        composable<Screen.Game> { backStackEntry ->
            val route = backStackEntry.toRoute<Screen.Game>()
            val gameViewModel: GameViewModel = koinViewModel()
            GameScreen(
                viewModel = gameViewModel,
                origin = route.origin,
                slotId = route.slotId,
                onNavigateToLoad = { navController.navigate(Screen.SaveLoad) },
                onNavigateToMenu = {
                    navController.navigate(Screen.MainMenu) {
                        popUpTo<Screen.MainMenu> { inclusive = true }
                    }
                },
            )
        }

        composable<Screen.SaveLoad> {
            val saveLoadViewModel: SaveLoadViewModel = koinViewModel()
            SaveLoadScreen(
                viewModel = saveLoadViewModel,
                onSlotSelected = { slotId ->
                    navController.navigate(Screen.Game(slotId = slotId)) {
                        popUpTo<Screen.MainMenu>()
                    }
                },
                onBack = { navController.popBackStack() },
            )
        }
    }
}
```

Граф навигации. `startDestination = Screen.MainMenu` — приложение начинается с главного меню. Каждый `composable<>` блок извлекает route-параметры через `toRoute<>()`, получает ViewModel через `koinViewModel()` (Koin DI) и передаёт колбэки навигации.

Переход `OriginSelect → Game` и `SaveLoad → Game` использует `popUpTo<Screen.MainMenu>()` — очищает back stack до главного меню, чтобы кнопка «назад» не возвращала на экран выбора происхождения или загрузки. Переход «в главное меню» из игры использует `inclusive = true` — заменяет текущий `MainMenu` в стеке, чтобы не было дублей.

### App.kt

```kotlin
@Composable
fun App() {
    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.surface) {
            val navController = rememberNavController()
            NavGraph(navController = navController)
        }
    }
}
```

Корневой composable. Оборачивает всё в `MaterialTheme` и `Surface`, создаёт `NavController` и передаёт в `NavGraph`. Общий для Android и iOS.

### GameViewModel

```kotlin
class GameViewModel(
    private val loadScene: LoadSceneUseCase,
    private val makeChoice: MakeChoiceUseCase,
    private val saveGame: SaveGameUseCase,
    private val evaluateConditions: EvaluateConditionsUseCase,
    private val loadGame: LoadGameUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<GameUiState>(GameUiState.Loading)
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var gameState: GameState? = null
    private var currentScene: Scene? = null
    private var pendingOutcome: ChoiceOutcome? = null
    private var preChoiceFlags: Set<String>? = null
    // ...
}
```

Центральный ViewModel. Получает 5 use cases через конструктор (инъекция Koin). Хранит:
- `_uiState` — реактивный `StateFlow`, на который подписывается UI
- `gameState` — текущий domain-стейт игры
- `currentScene` — загруженная сцена (нужна для доступа к тексту при сохранении)
- `pendingOutcome` — результат последнего выбора (показывается popup)
- `preChoiceFlags` — флаги до выбора (для отложенной активации глоссария)

#### startNewGame

```kotlin
fun startNewGame(origin: String) {
    val initialCharacter =
        Character(id = "player", origin = origin, stats = statsForOrigin(origin))
    val initialState = GameState(
        character = initialCharacter,
        currentSceneId = "scene_01",
        chapter = 1,
        timestamp = currentTimeMillis(),
    )
    gameState = initialState
    loadCurrentScene()
}

private fun statsForOrigin(origin: String): Stats {
    return when (origin) {
        "noble" -> Stats(strength = 1, cunning = 2, wisdom = 2, charisma = 5)
        "merchant" -> Stats(strength = 1, cunning = 5, wisdom = 2, charisma = 2)
        "soldier" -> Stats(strength = 5, cunning = 2, wisdom = 1, charisma = 2)
        "scholar" -> Stats(strength = 1, cunning = 2, wisdom = 5, charisma = 2)
        else -> Stats(strength = 2, cunning = 2, wisdom = 2, charisma = 2)
    }
}
```

Создаёт нового персонажа с характеристиками, зависящими от происхождения. У каждого origin одна характеристика «профильная» (5) и остальные ниже. Сумма очков одинакова (10) для баланса. Начальная сцена — `scene_01`.

#### onChoiceSelected

```kotlin
fun onChoiceSelected(choiceId: String) {
    val state = gameState ?: return
    val scene = currentScene ?: return
    val choice = scene.choices.find { it.id == choiceId } ?: return

    viewModelScope.launch {
        try {
            val oldCharacter = state.character
            preChoiceFlags = oldCharacter.flags           // запоминаем флаги до выбора
            val newState = makeChoice(state, choice)      // применяем эффекты
            gameState = newState.copy(timestamp = currentTimeMillis())
            pendingOutcome = computeOutcome(oldCharacter, newState.character, choice)
            autoSave()
            loadCurrentScene()                            // загружаем следующую сцену
        } catch (e: IllegalStateException) {
            _uiState.value = GameUiState.Error(e.message ?: "Cannot make this choice")
        }
    }
}
```

Основной игровой цикл по нажатию на вариант выбора:
1. Запоминает `preChoiceFlags` — текущие флаги. Нужно для антиспойлерной системы глоссария: когда выбор даёт флаг `knows_harbor`, термин «Harbor» не должен подсвечиваться на следующей сцене до тех пор, пока игрок не закроет popup с результатом.
2. Вызывает `makeChoice` — проверка условий + применение эффектов.
3. Вычисляет `pendingOutcome` — что изменилось (какие статы, отношения, знания).
4. Автосохраняет в slot 0.
5. Загружает следующую сцену.

#### loadCurrentScene

```kotlin
private fun loadCurrentScene() {
    val state = gameState ?: return
    if (state.currentSceneId == "end") {
        _uiState.value = GameUiState.GameOver
        return
    }
    _uiState.value = GameUiState.Loading
    viewModelScope.launch {
        val scene = loadScene(state.currentSceneId)
        currentScene = scene

        val choiceModels = scene.choices
            .filter { evaluateConditions.isChoiceVisible(it, state.character) }
            .map { choice ->
                val available = evaluateConditions.isChoiceAvailable(choice, state.character)
                ChoiceUiModel(
                    id = choice.id, text = choice.text,
                    isAvailable = available,
                    requirementHint = if (!available) buildRequirementHint(choice) else null,
                )
            }

        val paragraphs = scene.text.split("\n\n").map { it.trim() }.filter { it.isNotEmpty() }

        // Антиспойлер: пока popup не закрыт, глоссарь считается по старым флагам
        val flagsForGlossary = preChoiceFlags ?: state.character.flags
        val activeTerms = Glossary.unlockedTerms(flagsForGlossary)

        val outcome = pendingOutcome?.takeIf { it.hasContent }
        pendingOutcome = null
        if (outcome == null) preChoiceFlags = null

        _uiState.value = GameUiState.SceneReady(
            sceneName = scene.title,
            paragraphs = paragraphs,
            visibleParagraphs = 1,
            backgroundAsset = scene.backgroundAsset,
            choices = choiceModels,
            character = mapCharacterToUi(state.character),
            allTextRevealed = paragraphs.size <= 1,
            activeGlossaryTerms = activeTerms,
            choiceOutcome = outcome,
        )
    }
}
```

Загрузка сцены и формирование UI-состояния:
1. Проверка `"end"` — специальный маркер окончания контента.
2. Загрузка JSON через `LoadSceneUseCase`.
3. Фильтрация выборов: `isChoiceVisible` скрывает неподходящие по origin/flags, `isChoiceAvailable` определяет, можно ли нажать (если нельзя — формируется `requirementHint`).
4. Разбивка текста на абзацы по `\n\n` — для пагинации (сначала виден только первый абзац).
5. Антиспойлерный глоссарий: если есть `preChoiceFlags` (popup ещё не закрыт) — активные термины считаются по старым флагам. Новые термины станут видны только после `dismissOutcome()`.
6. Если есть непустой `pendingOutcome` — передаётся в `SceneReady` для отображения popup.

#### computeOutcome

```kotlin
private fun computeOutcome(
    oldCharacter: Character,
    newCharacter: Character,
    choice: Choice,
): ChoiceOutcome {
    val statChanges = choice.effects.stats
        .filter { it.value != 0 }
        .map { (stat, delta) ->
            StatChange(stat = stat, delta = delta, reason = buildStatReason(stat, delta, choice))
        }

    val relationChanges = choice.effects.relations
        .filter { it.value != 0 }
        .map { (npcKey, delta) ->
            RelationChange(
                npcKey = npcKey,
                npcDisplayName = NpcRegistry.displayName(npcKey),
                delta = delta,
                reason = buildRelationReason(npcKey, delta, choice),
            )
        }

    val newFlags = newCharacter.flags - oldCharacter.flags
    val newKnowledge = newFlags.map { flagId ->
        val info = FlagRegistry.lookup(flagId)
        KnowledgeGain(
            flagId = flagId,
            title = info?.title ?: formatFlagFallback(flagId),
            description = info?.description ?: "You have acquired new knowledge.",
        )
    }

    return ChoiceOutcome(statChanges, relationChanges, newKnowledge)
}
```

Вычисляет, что изменилось после выбора, и формирует данные для popup:
- **Stat changes**: берёт дельты из `effects.stats`, формирует текстовое объяснение (цитата из текста выбора).
- **Relation changes**: берёт дельты из `effects.relations`, подставляет имя NPC из `NpcRegistry`.
- **New knowledge**: вычисляет разницу множеств флагов (новые − старые), подтягивает описания из `FlagRegistry`. Если флаг не зарегистрирован — используется fallback-форматирование (`knows_harbor` → `Harbor`).

### UI State модели

```kotlin
sealed interface GameUiState {
    data object Loading : GameUiState
    data class SceneReady(
        val sceneName: String,
        val paragraphs: List<String>,
        val visibleParagraphs: Int,
        val backgroundAsset: String,
        val choices: List<ChoiceUiModel>,
        val character: CharacterUiModel,
        val allTextRevealed: Boolean,
        val activeGlossaryTerms: List<String>,
        val choiceOutcome: ChoiceOutcome? = null,
        val saveConfirmation: String? = null,
    ) : GameUiState
    data class ChapterTransition(val chapter: Int, val summaryText: String) : GameUiState
    data object GameOver : GameUiState
    data class Error(val message: String) : GameUiState
}
```

Sealed interface с пятью состояниями экрана. `SceneReady` — основное: содержит все данные для рендеринга сцены. `visibleParagraphs` / `allTextRevealed` — для пагинации (текст раскрывается абзац за абзацем). `choiceOutcome` — popup результата (null = не показывать). `saveConfirmation` — временный баннер после ручного сохранения.

```kotlin
data class ChoiceOutcome(
    val statChanges: List<StatChange> = emptyList(),
    val relationChanges: List<RelationChange> = emptyList(),
    val newKnowledge: List<KnowledgeGain> = emptyList(),
) {
    val hasContent: Boolean
        get() = statChanges.isNotEmpty() || relationChanges.isNotEmpty() || newKnowledge.isNotEmpty()
}
```

Результат выбора для popup. `hasContent` — фильтр: popup не показывается, если выбор ничего не изменил (например, чисто нарративный переход без эффектов).

### RichTextParser

```kotlin
fun parseNarrativeText(text: String, activeTerms: List<String> = emptyList()): AnnotatedString {
    return buildAnnotatedString {
        val paragraphs = text.split("\n\n")
        paragraphs.forEachIndexed { index, raw ->
            val paragraph = raw.trim()
            if (paragraph.isEmpty()) return@forEachIndexed
            if (index > 0) append("\n\n")
            parseParagraph(paragraph, activeTerms)
        }
    }
}
```

Точка входа парсера нарративного текста. Разбивает текст на абзацы по `\n\n` и обрабатывает каждый. `activeTerms` — список разблокированных глоссарных терминов (подсвечиваются золотым).

```kotlin
private fun AnnotatedString.Builder.parseParagraph(paragraph: String, activeTerms: List<String>) {
    when {
        // *narrator text* → курсив, цвет рассказчика
        paragraph.startsWith("*") && paragraph.endsWith("*") && paragraph.length > 2 -> { ... }
        // Speaker: "dialogue" → жирное имя + курсивная реплика
        DIALOGUE_REGEX.matches(paragraph) -> { ... }
        // Обычный текст с инлайн-*italic*
        else -> { parseInlineFormatting(paragraph, activeTerms) }
    }
}
```

Три типа абзацев: (1) рассказчик (`*text*` → italic, #CDBFAA), (2) диалог (`Speaker: "text"` → bold speaker #E0C080 + italic dialogue #EDE4D4), (3) обычный текст (#F0EAE0) с поддержкой инлайн-курсива.

```kotlin
private fun AnnotatedString.Builder.appendWithGlossary(
    text: String, baseStyle: SpanStyle, activeTerms: List<String>,
) {
    // Ищет ближайшее вхождение любого термина из activeTerms
    // При совпадении позиций — выбирает самый длинный (greedy)
    // Найденный термин: gold bold + аннотация "glossary" для обработки кликов
    // Остальной текст — baseStyle
}
```

Глоссарная подсветка: сканирует текст на вхождения терминов из `activeTerms` (case-insensitive). При нахождении — применяет золотой (#E0C080) bold стиль и добавляет `pushStringAnnotation(tag = "glossary", annotation = term)`, что позволяет `GameScreen` обрабатывать нажатия на термины и открывать popup.

---

## DI (Koin)

```kotlin
val domainModule = module {
    factory { EvaluateConditionsUseCase() }
    factory { LoadSceneUseCase(get()) }
    factory { MakeChoiceUseCase(get()) }
    factory { SaveGameUseCase(get()) }
    factory { LoadGameUseCase(get()) }
}

val dataModule = module {
    single { SceneJsonParser() }
    single<NarrativeRepository> { NarrativeRepositoryImpl(get()) }
    single<SaveRepository> { SaveRepositoryImpl(get()) }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
}

val viewModelModule = module {
    factory { GameViewModel(get(), get(), get(), get(), get()) }
    factory { SaveLoadViewModel(get(), get()) }
}

val sharedModules = listOf(domainModule, dataModule, viewModelModule)
```

Три Koin-модуля: `domainModule` (use cases как `factory` — новый экземпляр при каждом запросе), `dataModule` (реализации репозиториев и парсер как `single` — один экземпляр на приложение), `viewModelModule` (ViewModels как `factory`). `sharedModules` — список для подключения в platform-specific инициализации. Платформенные модули (`platformModule` в androidMain/iosMain) добавляют `DatabaseDriverFactory`, `AppDatabase`, `DataStoreFactory`.

## currentTimeMillis (expect/actual)

```kotlin
// shared/commonMain
internal expect fun currentTimeMillis(): Long

// shared/androidMain
internal actual fun currentTimeMillis(): Long = System.currentTimeMillis()

// shared/iosMain
internal actual fun currentTimeMillis(): Long =
    (NSDate().timeIntervalSince1970 * 1000).toLong()
```

Платформенная абстракция для получения текущего времени в миллисекундах. Используется для `timestamp` в `GameState` и как уникальный `id` ручных сохранений. На Android — стандартный `System.currentTimeMillis()`, на iOS — `NSDate` из Foundation (конвертация из секунд в миллисекунды).

---

## Навигационная схема

```
MainMenuScreen
 ├── OriginSelectScreen → GameScreen  (новая игра, передаёт origin)
 └── SaveLoadScreen     → GameScreen  (загрузка, передаёт slotId)

GameScreen
 ├── CharacterSheet     (ModalBottomSheet — статы, отношения, хроника)
 ├── Menu dropdown:
 │   ├── Save           (ручное сохранение с именем сцены)
 │   ├── Load → SaveLoadScreen
 │   └── Main Menu → MainMenuScreen
 ├── ChoiceOutcomePopup (popup после выбора)
 └── GlossaryPopup      (popup при клике на золотой термин)
```

## Формат нарративного контента

JSON-файлы в `shared/src/commonMain/composeResources/files/narrative/chapter_XX/`. 48 сцен в главе 1 с 4-актной структурой.

```json
{
  "id": "scene_01",
  "title": "The Gates of Ashenmoor",
  "chapter": 1,
  "background": "city_gate",
  "text": "*The road to Ashenmoor stretches before you...*\n\nThe city gates loom ahead...",
  "choices": [
    {
      "id": "c1",
      "text": "Stride to the front and present your credentials",
      "requires": { "stat_min": { "charisma": 4 } },
      "effects": {
        "relations": { "guard_captain": 1 },
        "flags": ["noble_entry"]
      },
      "next_scene": "scene_02"
    }
  ]
}
```

Маппинг `sceneId → файл` хранится в `index.json`. Доступ через `Res.readBytes()` (Compose Resources KMP API), работает одинаково на Android и iOS.

## Справочные реестры

**Glossary.kt** — 20+ записей о мире. Записи без `requiredFlag` доступны всегда, с `requiredFlag` — разблокируются по мере получения флагов. Активация **отложена** до закрытия popup (антиспойлер).

**NpcRegistry.kt** — 10 NPC с 5 уровнями отношений (Hostile ≤ -5, Cold, Neutral, Warm, Trusted ≥ 10) и уникальными описаниями для каждого уровня.

**FlagRegistry.kt** — ~70 сюжетных флагов с метаданными (title, description, howObtained, hint). Отображаются в CharacterSheet как «Хроника» с кликабельными деталями.
