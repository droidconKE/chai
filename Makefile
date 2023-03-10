BUILD_TYPE ?= Debug
GRADLE_ARGS ?= --build-cache

.PHONY: all assemble build bundle clean lint test

all: clean lint test build assemble bundle

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