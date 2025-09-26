# Makefile for Compose Multiplatform Project

.PHONY: help
help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

.PHONY: install-kdoctor
install-kdoctor: ## Install kdoctor for Kotlin Multiplatform development
	@echo "Installing kdoctor..."
	@command -v brew >/dev/null 2>&1 || (echo "Error: Homebrew is not installed. Please install it first from https://brew.sh" && exit 1)
	@brew install kdoctor

.PHONY: doctor
doctor: ## Run kdoctor to check development environment
	@command -v kdoctor >/dev/null 2>&1 || $(MAKE) install-kdoctor
	@kdoctor

.PHONY: doctor-verbose
doctor-verbose: ## Run kdoctor with verbose output
	@command -v kdoctor >/dev/null 2>&1 || $(MAKE) install-kdoctor
	@kdoctor -v

.PHONY: setup
setup: install-kdoctor ## Setup development environment
	@echo "Setting up development environment..."
	@$(MAKE) doctor

.PHONY: build-android
build-android: ## Build Android app
	./gradlew :composeApp:assembleDebug

.PHONY: run-android
run-android: ## Run Android app
	./gradlew :composeApp:installDebug
	@echo "Launching app..."
	@adb shell am start -n org.example.project/org.example.project.MainActivity

.PHONY: build-ios
build-ios: ## Build iOS framework
	./gradlew :composeApp:linkDebugFrameworkIosSimulatorArm64

.PHONY: run-ios
run-ios: ## Run iOS app in simulator
	@echo "Opening iOS project in Xcode..."
	@open iosApp/iosApp.xcodeproj

.PHONY: run-ios-cli
run-ios-cli: ## Build and run iOS app via command line
	xcodebuild -project iosApp/iosApp.xcodeproj \
		-scheme iosApp \
		-destination 'platform=iOS Simulator,name=iPhone 17' \
		-derivedDataPath build \
		build
	@echo "Launching app in simulator..."
	@xcrun simctl boot "iPhone 17" 2>/dev/null || true
	@open -a Simulator
	@xcrun simctl install booted build/Build/Products/Debug-iphonesimulator/AndroidBootcampIwatePrefComposeMultiplatform.app
	@xcrun simctl launch booted org.example.project.AndroidBootcampIwatePrefComposeMultiplatform

.PHONY: run-desktop
run-desktop: ## Run desktop app
	./gradlew :composeApp:run

.PHONY: run-web
run-web: ## Run web app
	./gradlew :composeApp:wasmJsBrowserDevelopmentRun

.PHONY: clean
clean: ## Clean build files
	./gradlew clean

.PHONY: test
test: ## Run all tests
	./gradlew test

.PHONY: lint
lint: ## Run lint checks
	./gradlew ktlintCheck

.PHONY: format
format: ## Format code with ktlint
	./gradlew ktlintFormat