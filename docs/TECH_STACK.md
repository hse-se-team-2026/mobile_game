# Технический стек

## Платформа

| Параметр | Значение |
|----------|----------|
| Подход | Kotlin Multiplatform (KMP) |
| UI | Compose Multiplatform (Jetbrains) |
| Targets | Android (primary), iOS |
| Язык | Kotlin 2.3.21 |
| Android minSdk | 30 (Android 11) |
| iOS deployment target | 18.2 |
| Build-система | Gradle 9.1.0 (Kotlin DSL) |

## Компоненты

### Общие (commonMain)

| Категория | Библиотека | Версия | Примечание |
|-----------|------------|--------|------------|
| UI | Compose Multiplatform | 1.11.0 | Jetbrains, не Jetpack |
| Навигация | Navigation Compose (JB) | 2.8.x | KMP-версия от Jetbrains |
| DI | Koin | 4.x | Hilt — только Android, Koin — KMP |
| Async | Kotlin Coroutines | 1.9.x | KMP-native |
| БД | SQLDelight | 2.x | Генерирует типизированный Kotlin из SQL |
| Настройки | DataStore KMP | 1.1.x | |
| Сериализация | kotlinx.serialization-json | 1.7.x | |
| Изображения | Coil 3 | 3.0.x | KMP-поддержка с v3 |

### Платформо-зависимые

| Платформа | Компонент | Реализация |
|-----------|-----------|------------|
| Android | SQLDelight driver | `AndroidSqliteDriver` |
| iOS | SQLDelight driver | `NativeSqliteDriver` |
| Android | Аудио | MediaPlayer + SoundPool |
| iOS | Аудио | AVAudioPlayer (Swift interop) |

## Тестирование

| Тип | Инструмент | Где запускается |
|-----|------------|-----------------|
| Unit (domain/data) | kotlin.test + MockK | commonTest (JVM) |
| UI | Compose UI Testing | androidTest / iosTest |
| БД | SQLDelight in-memory driver | commonTest |

## Инструменты

| Назначение | Инструмент |
|------------|------------|
| VCS | Git |
| CI | GitHub Actions |
| Статический анализ | Detekt |
| Форматирование | ktfmt |
