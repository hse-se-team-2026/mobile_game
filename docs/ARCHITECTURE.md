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

## Слои внутри shared/commonMain

```
Presentation (screen/, navigation/)
   ↓ зависит от
Domain (entity, repository interfaces, usecases)
   ↑ зависит от
Data (repository impl, SQLDelight DAO, JSON parser)
```

Domain не содержит зависимостей от платформы и фреймворков.

## Структура пакетов

```
// shared/src/commonMain/kotlin/ru/hse/mobile_game/
ru.hse.mobile_game/
├── domain/
│   ├── entity/
│   │   ├── GameState.kt          # Состояние игры (character, sceneId, chapter, history)
│   │   ├── Character.kt          # Персонаж (origin, stats, flags, relations, taint)
│   │   └── Scene.kt              # Сцена + Choice, Requirements, Effects
│   ├── repository/
│   │   ├── NarrativeRepository.kt
│   │   ├── SaveRepository.kt
│   │   └── SettingsRepository.kt
│   └── usecase/
│       ├── LoadSceneUseCase.kt
│       ├── MakeChoiceUseCase.kt
│       ├── EvaluateConditionsUseCase.kt  # Проверка stat/flag/origin requirements
│       ├── SaveGameUseCase.kt
│       └── LoadGameUseCase.kt
├── data/
│   ├── narrative/
│   │   ├── NarrativeRepositoryImpl.kt
│   │   ├── NarrativeModels.kt       # SceneJson, ChoiceJson, RequirementsJson, EffectsJson
│   │   └── SceneJsonParser.kt
│   ├── database/
│   │   ├── SaveRepositoryImpl.kt
│   │   └── DatabaseDriverFactory.kt  # expect/actual
│   └── settings/
│       ├── DataStoreFactory.kt       # expect/actual
│       └── SettingsRepositoryImpl.kt
├── di/
│   └── AppModule.kt                  # Koin: domainModule, dataModule, viewModelModule
├── navigation/
│   ├── NavGraph.kt                   # Compose Navigation graph
│   └── Screen.kt                     # @Serializable sealed class routes
└── screen/
    ├── mainmenu/
    │   └── MainMenuScreen.kt         # Главное меню (New Game / Continue / Load)
    ├── origin/
    │   └── OriginSelectScreen.kt     # Выбор происхождения (Noble/Merchant/Soldier/Scholar)
    ├── game/
    │   ├── GameScreen.kt             # Игровой экран (сцена, выборы, глоссарий, меню)
    │   ├── GameViewModel.kt          # MVVM ViewModel — логика игры
    │   ├── Glossary.kt               # 20+ записей с progressive discovery
    │   ├── FlagRegistry.kt           # ~70 флагов с описаниями
    │   ├── NpcRegistry.kt            # 10 NPC с relation tiers и flavor text
    │   ├── RichTextParser.kt         # Парсинг *italic* + золотые глоссарные термины
    │   └── CurrentTimeMillis.kt      # expect/actual (System.currentTimeMillis / NSDate)
    ├── character/
    │   └── CharacterSheet.kt         # Bottom sheet: статы, отношения, хроника
    ├── save/
    │   ├── SaveLoadScreen.kt         # Список слотов сохранений
    │   └── SaveLoadViewModel.kt      # MVVM для экрана сохранений
    └── model/
        ├── GameUiState.kt            # UI-состояние игрового экрана
        └── SaveLoadUiState.kt        # UI-состояние экрана сохранений
```

## expect/actual — платформенные реализации

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

```kotlin
// shared/commonMain (screen/game/)
internal expect fun currentTimeMillis(): Long

// shared/androidMain
internal actual fun currentTimeMillis(): Long = System.currentTimeMillis()

// shared/iosMain
internal actual fun currentTimeMillis(): Long =
    (NSDate().timeIntervalSince1970 * 1000).toLong()
```

## Основные сущности (Domain)

```kotlin
data class GameState(
    val character: Character,
    val currentSceneId: String,
    val chapter: Int,
    val choiceHistory: List<String>,
    val timestamp: Long
)

data class Character(
    val id: String,
    val origin: String,
    val stats: Stats,
    val relations: Map<String, Int>,        // npcKey → value
    val factionStandings: Map<String, Int>,
    val flags: Set<String>,
    val taint: Int
)

data class Scene(
    val id: String,
    val title: String,              // Narrative scene name (e.g. "The Gates of Ashenmoor")
    val chapter: Int,
    val backgroundAsset: String,
    val text: String,
    val choices: List<Choice>
)

data class Choice(
    val id: String,
    val text: String,
    val requires: Requirements?,    // stat_min, flags_required, flags_forbidden, origin_required
    val effects: Effects,           // stats, relations, factionStandings, flags
    val nextSceneId: String
)
```

## Формат нарративного контента

JSON-файлы в `shared/src/commonMain/composeResources/files/narrative/chapter_XX/`.
Маппинг `sceneId → путь` — `index.json`. Доступ через `Res.readBytes()` (Compose Resources KMP API).

48 сцен в главе 1 с 4-актной структурой.

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
    },
    {
      "id": "c2",
      "text": "Wait patiently and listen",
      "effects": {
        "stats": { "wisdom": 1 },
        "flags": ["patient_entry"]
      },
      "next_scene": "scene_05"
    }
  ]
}
```

## Схема БД (SQLDelight)

```sql
-- shared/src/commonMain/sqldelight/com/example/ashesofgods/SaveSlot.sq

CREATE TABLE SaveSlot (
    id           INTEGER PRIMARY KEY,
    name         TEXT    NOT NULL,
    game_state   TEXT    NOT NULL,   -- JSON (GameState)
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

Автосохранение использует `id = 0`, ручные сохранения используют `currentTimeMillis()` как уникальный ID.

## UI State

```kotlin
sealed interface GameUiState {
    data object Loading : GameUiState

    data class SceneReady(
        val sceneName: String,                      // Scene.title
        val paragraphs: List<String>,               // Текст сцены, разбитый по \n\n
        val visibleParagraphs: Int,                 // Пагинация текста
        val backgroundAsset: String,
        val choices: List<ChoiceUiModel>,
        val character: CharacterUiModel,
        val allTextRevealed: Boolean,
        val activeGlossaryTerms: List<String>,      // Разблокированные термины глоссария
        val choiceOutcome: ChoiceOutcome? = null,   // Popup с результатом выбора
        val saveConfirmation: String? = null,       // Баннер подтверждения сохранения
    ) : GameUiState

    data class ChapterTransition(val chapter: Int, val summaryText: String) : GameUiState
    data object GameOver : GameUiState
    data class Error(val message: String) : GameUiState
}

data class ChoiceOutcome(
    val statChanges: List<StatChange>,       // Изменения характеристик
    val relationChanges: List<RelationChange>, // Изменения отношений с NPC
    val newKnowledge: List<KnowledgeGain>,   // Новые знания (флаги)
)
```

## Поток данных (игровой экран)

```
Пользователь выбирает вариант
        ↓
GameViewModel.onChoiceSelected(choiceId)
        ↓
Сохраняем preChoiceFlags (для отложенной активации глоссария)
        ↓
MakeChoiceUseCase → проверка Requirements → применение Effects → новый Character
        ↓
computeOutcome() → ChoiceOutcome (stat changes, relation changes, new knowledge)
        ↓
SaveGameUseCase (автосохранение, slot 0)
        ↓
LoadSceneUseCase(nextSceneId)
        ↓
Glossary.unlockedTerms(preChoiceFlags) → термины БЕЗ новых знаний (антиспойлер)
        ↓
GameUiState.SceneReady с choiceOutcome popup
        ↓
Пользователь закрывает popup → dismissOutcome()
        ↓
activeGlossaryTerms обновляются полными флагами → новые термины становятся видимыми
```

## DI (Koin)

```kotlin
// shared/commonMain — AppModule.kt
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

// androidMain / iosMain — platformModule: DatabaseDriverFactory, AppDatabase, DataStoreFactory
```

## Навигация

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

## Система глоссария

Glossary.kt содержит 20+ записей с progressive discovery:
- Записи без `requiredFlag` — доступны всегда (Ashenmoor, Duke Aldren, Kerhold)
- Записи с `requiredFlag` — разблокируются при получении соответствующего флага
- Термины подсвечиваются золотым цветом в тексте сцены через RichTextParser
- Активация новых терминов **отложена** до закрытия popup с результатом выбора (антиспойлер)

## Система NPC

NpcRegistry.kt содержит 10 NPC с relation tiers:
- Hostile (≤ -5), Cold (-4..0), Neutral (0..4), Warm (5..9), Trusted (≥ 10)
- Flavor text для каждого NPC по уровню отношений
- Имена NPC в CharacterSheet кликабельны → открывают глоссарный popup

## Система флагов

FlagRegistry.kt содержит ~70 флагов с метаданными:
- title, description, howObtained, hint
- Используются для: разблокировки записей глоссария, видимости выборов, веток сюжета
- Отображаются в CharacterSheet как "Chronicle" с кликабельными деталями
