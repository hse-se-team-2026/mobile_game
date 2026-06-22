# Ashes of Gods

Сюжетная мобильная RPG-квест, вдохновлённая *The Life and Suffering of Sir Brante*. Игрок проходит через нарративные сцены, принимает решения, развивает персонажа и влияет на отношения с NPC, флаги сюжета и дальнейшие ветки повествования.

Проект построен на **Kotlin Multiplatform** (Android + iOS) с общим UI на **Compose Multiplatform**.

## Содержание

- [Стек](#стек)
- [Структура проекта](#структура-проекта)
- [Требования](#требования)
- [Сборка и запуск](#сборка-и-запуск)
- [Тесты](#тесты)
- [Линтеры и CI](#линтеры-и-ci)
- [Игровые возможности](#игровые-возможности)
- [Документация](#документация)
- [Команда](#команда)
- [Лицензия](#лицензия)

## Стек

| Компонент | Технология |
|-----------|------------|
| Платформа | Kotlin Multiplatform (KMP) |
| UI | Compose Multiplatform 1.11.0 (JetBrains) |
| Навигация | Navigation Compose (JB KMP) |
| DI | Koin 4.0.4 |
| БД | SQLDelight 2.0.2 |
| Настройки | DataStore KMP 1.1.7 |
| Сериализация | kotlinx.serialization 1.7.3 |
| Статический анализ | Detekt 1.23.8 |
| Форматирование | ktfmt (kotlinLangStyle) |

Подробнее — [TECH_STACK.md](./docs/TECH_STACK.md).

## Структура проекта

```
mobile_game/
├── shared/                        # KMP-модуль: вся логика, данные и UI
│   └── src/
│       ├── commonMain/            # Domain, Data, Presentation
│       │   ├── kotlin/.../domain/ # Сущности, репозитории, use cases
│       │   ├── kotlin/.../data/   # JSON-парсер, SQLDelight DAO, DataStore
│       │   ├── kotlin/.../di/     # Koin-модули
│       │   ├── kotlin/.../navigation/  # NavGraph, Screen routes
│       │   ├── kotlin/.../screen/ # Compose-экраны и ViewModels
│       │   ├── composeResources/  # Фоны (webp), JSON-сцены (48 шт.)
│       │   └── sqldelight/        # SQL-схема сохранений
│       ├── androidMain/           # expect/actual для Android
│       ├── iosMain/               # expect/actual для iOS
│       ├── commonTest/            # Общие unit-тесты
│       ├── androidHostTest/       # Android host тесты (JVM)
│       └── iosTest/               # iOS тесты
├── androidApp/                    # Android entry point (MainActivity)
├── iosApp/                        # Xcode-проект для iOS
├── docs/                          # Архитектура, стек, процесс разработки
└── Makefile                       # Команды для сборки, тестов, CI
```

Подробнее — [ARCHITECTURE.md](./docs/ARCHITECTURE.md).

## Требования

### Общие

- **JDK 21+** (рекомендуется JBR или Azul Zulu)
- **Gradle 9.1** (wrapper включён в репозиторий, ставить не нужно)

### Android

- **Android Studio** Meerkat (2024.3+) или новее
- **Android SDK** с `compileSdk 36` и `minSdk 30`
- Эмулятор или устройство с Android 11+

### iOS

- **macOS** с **Xcode 16+**
- iOS Simulator или устройство с iOS 18.2+

## Сборка и запуск

### Android

Через терминал:

```bash
# Собрать debug APK
./gradlew :androidApp:assembleDebug

# APK появится здесь:
# androidApp/build/outputs/apk/debug/androidApp-debug.apk

# Установить на подключённое устройство / эмулятор
adb install androidApp/build/outputs/apk/debug/androidApp-debug.apk
```

Через Android Studio — открыть корень проекта и нажать **Run** (▶).

### iOS

1. Открыть `iosApp/iosApp.xcodeproj` в Xcode
2. Выбрать target **iosApp** и симулятор / устройство
3. ⌘R для запуска

KMP framework подтягивается из `shared` автоматически при сборке.

### На физическом устройстве (Android)

1. Включить **Developer options** → **USB debugging** на телефоне
2. Подключить USB-кабелем, подтвердить доверие на устройстве
3. `./gradlew :androidApp:assembleDebug` и передать APK через `adb install` или просто файлом

## Тесты

```bash
# Все тесты shared-модуля
./gradlew :shared:allTests

# Только Android host тесты (JVM, быстрые)
./gradlew :shared:testAndroidHostTest

# Только iOS тесты (нужен macOS)
./gradlew :shared:iosSimulatorArm64Test
```

Или через Makefile:

```bash
make shared-tests
```

В проекте 29+ unit-тестов:
- `SceneJsonParserTest` — парсинг JSON-сцен, edge cases
- `MakeChoiceUseCaseTest` — применение эффектов выбора, обновление стейта
- `EvaluateConditionsUseCaseTest` — проверка stat/flag/origin requirements

## Линтеры и CI

Проект использует GitHub Actions. Локально можно прогнать те же проверки:

```bash
# Форматирование (ktfmt, kotlinLangStyle)
./gradlew :shared:ktfmtFormat --no-daemon
# или
make ktfmt

# Статический анализ (Detekt)
./gradlew :shared:detekt --no-daemon
# или
make detekt
```

Перед пушем рекомендуется запускать оба — CI проверяет и то, и другое.

## Игровые возможности

- **4 происхождения**: Noble, Merchant, Soldier, Scholar — каждое даёт уникальные стартовые характеристики и открывает эксклюзивные варианты выбора
- **48 сцен** с 4-актной нарративной структурой, 12 фоновых изображений
- **Система характеристик**: charisma, intellect, combat, wisdom, piety, taint — изменяются через выборы
- **Отношения с NPC**: 10 персонажей, 5 уровней отношений (Hostile → Trusted), уникальные реплики
- **Флаги и хроника**: ~70 сюжетных флагов с описаниями, отображаются в карточке персонажа
- **Глоссарий**: 20+ записей о мире, разблокируются по мере прохождения, подсвечиваются золотым в тексте
- **Отложенная активация знаний**: новые термины глоссария появляются только после закрытия popup результата выбора (без спойлеров)
- **Система сохранений**: автосохранение + ручные слоты с названиями сцен
- **Пагинация текста**: сцены раскрываются постепенно, абзац за абзацем
- **Popup результатов**: после каждого выбора показывает изменения характеристик, отношений и полученные знания

## Документация

Подробная документация в `docs/`:

- [Архитектура](./docs/ARCHITECTURE.md) — модули, слои, пакеты, сущности, потоки данных, DI, навигация
- [Технический стек](./docs/TECH_STACK.md) — все библиотеки с версиями, платформенные реализации
- [Процесс разработки](./docs/DevelopmentProcess.md) — распределение задач, диаграмма Ганта, риски

## Команда

- Абишев Виктор
- Русанов Дмитрий
- Усатов Павел
- Устинов Тимофей

## Лицензия

См. [LICENSE](./LICENSE).
