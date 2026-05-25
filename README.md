# Mobile Game

`Mobile Game` is a story-driven mobile quest RPG inspired by games like *The Life and Suffering of Sir Brante*. The player moves through narrative scenes, makes choices, develops a character, and affects relationships, flags, and future story branches.

The project is built as a Kotlin Multiplatform application targeting Android and iOS.

## Documentation

Architecture documents are available in `internnalDocs/Architecture/`:

- [Architecture](./docs/ARCHITECTURE.md)
- [Tech Stack](./docs/TECH_STACK.md)
- [Development Process](./docs/DevelopmentProcess.md)

## Team

- Abishev Viktor
- Rusanov Dmitriy
- Usatov Pavel
- Ustinov Timofey

## Running the Project

- Android build: `./gradlew :androidApp:assembleDebug`
- Android tests: `./gradlew :shared:testAndroidHostTest`
- iOS tests: `./gradlew :shared:iosSimulatorArm64Test`
- iOS app: open `iosApp` in Xcode and run it there