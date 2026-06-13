.PHONY: db-generate build clean test

db-generate:
	./gradlew :shared:generateCommonMainSqlDelightDatabaseInterface

build:
	./gradlew build

clean:
	./gradlew clean

test:
	./gradlew :shared:allTests
