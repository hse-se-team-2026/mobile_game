.PHONY: db-generate build clean test

db-generate:
	./gradlew :shared:generateSqlDelightInterface

shared-build:
    ./gradlew :shared:build

shared-tests:
    ./gradlew :shared:allTests
build:
	./gradlew build

clean:
	./gradlew clean

test:
	./gradlew :shared:allTests
