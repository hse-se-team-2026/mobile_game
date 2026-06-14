.PHONY: db-generate build clean test shared-build shared-tests ci-build detekt ktfmt

db-generate:
	./gradlew :shared:generateSqlDelightInterface

shared-build:
	./gradlew :shared:build

shared-tests:
	./gradlew :shared:allTests

ci-build:
	./gradlew :shared:compileKotlinMetadata :shared:compileAndroidMain --stacktrace --no-daemon

detekt:
	./gradlew :shared:detekt --no-daemon

ktfmt:
	./gradlew :shared:ktfmtFormat --no-daemon

build:
	./gradlew build

clean:
	./gradlew clean

test:
	./gradlew :shared:allTests
