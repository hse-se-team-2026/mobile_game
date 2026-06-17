# Технический стек

## Платформа

| Параметр | Значение |
|----------|----------|
| Подход | Kotlin Multiplatform (KMP) |
| UI | Compose Multiplatform (JetBrains) |
| Targets | Android (primary), iOS |
| Язык | Kotlin 2.3.21 |
| Android minSdk | 30 (Android 11) |
| Android targetSdk | 36 |
| iOS deployment target | 18.2 |
| Build-система | Gradle 9.1.0 (Kotlin DSL) |
| AGP | 9.0.1 |

## Компоненты

### Общие (commonMain)

| Категория | Библиотека | Версия | Примечание |
|-----------|------------|--------|------------|
| UI | Compose Multiplatform | 1.11.0 | JetBrains, не Jetpack |
| Material | Material 3 | 1.11.0-alpha07 | JetBrains KMP |
| Навигация | Navigation Compose (JB) | 2.9.0-alpha14 | KMP-версия от JetBrains |
| ViewModel | Lifecycle ViewModel Compose | 2.11.0-beta01 | JetBrains KMP |
| DI | Koin | 4.0.4 | Hilt — только Android, Koin — KMP |
| Async | Kotlin Coroutines | встроенные | KMP-native |
| БД | SQLDelight | 2.0.2 | Генерирует типизированный Kotlin из SQL |
| Настройки | DataStore KMP | 1.1.7 | Preferences DataStore |
| Сериализация | kotlinx.serialization-json | 1.7.3 | Для JSON-парсинга и GameState |

### Платформо-зависимые

| Платформа | Компонент | Реализация |
|-----------|-----------|------------|
| Android | SQLDelight driver | `AndroidSqliteDriver` |
| iOS | SQLDelight driver | `NativeSqliteDriver` |
| Android | DataStore | `PreferencesDataStoreFactory` (context-based) |
| iOS | DataStore | `PreferenceDataStoreFactory.createWithPath` (NSDocumentDirectory) |
| Android | currentTimeMillis | `System.currentTimeMillis()` |
| iOS | currentTimeMillis | `NSDate().timeIntervalSince1970 * 1000` |

## Тестирование

| Тип | Инструмент | Где запускается |
|-----|------------|-----------------|
| Unit (domain/data) | kotlin.test | commonTest → androidHostTest (JVM) |
| Количество тестов | 29+ | SceneJsonParser, MakeChoice, EvaluateConditions, SharedCommon |

## Инструменты

| Назначение | Инструмент | Версия |
|------------|------------|--------|
| VCS | Git | — |
| CI | GitHub Actions | — |
| Статический анализ | Detekt | 1.23.8 |
| Форматирование | ktfmt (kotlinLangStyle) | 0.22.0 |
