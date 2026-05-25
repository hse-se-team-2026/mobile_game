# Архитектура

## Паттерн

**Clean Architecture + MVVM.** Три слоя: Presentation, Domain, Data. Проект разбит на два Gradle-модуля: `shared` (commonMain) и `composeApp` (UI).

## Структура модулей

```
project/
├── shared/                     # KMP модуль — бизнес-логика и данные
│   └── src/
│       ├── commonMain/         # Domain + Data (без платформенных зависимостей)
│       ├── androidMain/        # Android-специфичные реализации (SQLite driver)
│       └── iosMain/            # iOS-специфичные реализации (SQLite driver)
├── composeApp/                 # Compose Multiplatform UI
│   └── src/
│       ├── commonMain/         # Общий UI (Screens, ViewModels, Navigation)
│       ├── androidMain/        # Android entry point (MainActivity)
│       └── iosMain/            # iOS entry point (MainViewController)
└── iosApp/                     # Xcode-проект (обёртка для iOS)
```

## Слои внутри shared/commonMain

```
Domain (entity, repository interfaces, usecases)
   ↑ зависит от
Data (repository impl, SQLDelight DAO, JSON parser)
```

Domain не содержит зависимостей от платформы и фреймворков.

## Структура пакетов

```
// shared/src/commonMain/
com.example.ashesofgods/
├── domain/
│   ├── entity/
│   │   ├── GameState.kt
│   │   ├── Character.kt
│   │   ├── Scene.kt
│   │   └── Choice.kt
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
└── data/
    ├── narrative/
    │   ├── NarrativeRepositoryImpl.kt
    │   └── SceneJsonParser.kt
    ├── database/
    │   ├── SaveRepositoryImpl.kt
    │   └── DatabaseDriverFactory.kt  ← expect/actual
    └── settings/
        └── SettingsRepositoryImpl.kt

// composeApp/src/commonMain/
com.example.ashesofgods/
├── di/                         # Koin модули
├── navigation/
│   ├── NavGraph.kt
│   └── Screen.kt
├── screen/
│   ├── mainmenu/
│   ├── origin/
│   ├── game/
│   ├── character/
│   └── save/
└── component/
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
    val origin: Origin,
    val stats: Stats,
    val relations: Map<String, Int>,        // npcId → [-100, 100]
    val factionStandings: Map<String, Int>,
    val flags: Set<String>,
    val taint: Int
)

data class Scene(
    val id: String,
    val chapter: Int,
    val backgroundAsset: String,
    val text: String,
    val choices: List<Choice>
)

data class Choice(
    val id: String,
    val text: String,
    val requires: Requirements?,
    val effects: Effects,
    val nextSceneId: String
)
```

## Формат нарративного контента

JSON-файлы в `composeApp/src/commonMain/composeResources/files/narrative/chapter_XX/`.  
Маппинг `sceneId → путь` — `index.json`. Доступ через `Res.readBytes()` (Compose Resources KMP API).

```json
{
  "id": "scene_01",
  "chapter": 1,
  "background": "market_dusk",
  "text": "...",
  "choices": [
    {
      "id": "c1",
      "text": "...",
      "requires": { "stat_min": { "cunning": 2 } },
      "effects": { "stats": { "cunning": 1 }, "relations": { "npc_guard": -5 } },
      "next_scene": "scene_02"
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

## UI State

```kotlin
sealed interface GameUiState {
    object Loading : GameUiState
    data class SceneReady(
        val sceneText: String,
        val backgroundAsset: String,
        val choices: List<ChoiceUiModel>,
        val character: CharacterUiModel,
        val isTyping: Boolean
    ) : GameUiState
    data class ChapterTransition(val chapter: Int, val summaryText: String) : GameUiState
    object GameOver : GameUiState
    data class Error(val message: String) : GameUiState
}
```

## Поток данных (игровой экран)

```
Пользователь выбирает вариант
        ↓
GameViewModel.onChoiceSelected(choiceId)
        ↓
MakeChoiceUseCase → проверка Requirements → применение Effects → новый Character
        ↓
SaveGameUseCase (автосохранение)
        ↓
LoadSceneUseCase(nextSceneId)
        ↓
GameViewModel обновляет GameUiState → перекомпозиция GameScreen
```

## DI (Koin)

```kotlin
// shared/commonMain
val domainModule = module {
    factory { LoadSceneUseCase(get()) }
    factory { MakeChoiceUseCase(get()) }
    factory { SaveGameUseCase(get()) }
}

val dataModule = module {
    single<NarrativeRepository> { NarrativeRepositoryImpl(get()) }
    single<SaveRepository> { SaveRepositoryImpl(get()) }
    single { AppDatabase(get<DatabaseDriverFactory>().createDriver()) }
}

// androidMain / iosMain — добавляют platformModule с DatabaseDriverFactory
```

## Навигация

```
MainMenuScreen
 ├── OriginSelectScreen → GameScreen  (новая игра)
 └── SaveLoadScreen     → GameScreen  (загрузка)

GameScreen
 ├── CharacterScreen  (bottom sheet)
 ├── SaveLoadScreen   (пауза)
 └── MainMenuScreen   (выход)
```
