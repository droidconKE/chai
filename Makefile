BUILD_TYPE ?= Debug
GRADLE_ARGS ?= --build-cache

.PHONY: all android-test assemble build bundle clean lint test

all: clean lint test build assemble bundle

android-test:
	./gradlew connectedCheck -PminifyDebugBuild=false ${GRADLE_ARGS}

assemble:
	./gradlew assemble${BUILD_TYPE} ${GRADLE_ARGS}

build:
	./gradlew build${BUILD_TYPE} ${GRADLE_ARGS}

bundle:
	./gradlew bundle${BUILD_TYPE} ${GRADLE_ARGS}

clean:
	./gradlew clean ${GRADLE_ARGS}

lint:
	./gradlew lint${BUILD_TYPE} ${GRADLE_ARGS}

test:
	./gradlew test${BUILD_TYPE}UnitTest ${GRADLE_ARGS}

spotless:
	./gradlew --init-script gradle/init.gradle.kts -q spotless
