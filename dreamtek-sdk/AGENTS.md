# AGENTS.md

This file provides guidance to the AI agent when working with code in this repository.

## Project Structure & Module Organization

This is a Java 8 Android project built with Gradle 7.5 and Android Gradle Plugin 7.2.2. The primary device-service test client is `app/`. Its production code is under `app/src/main/java/`, grouped into activities (`view/`), service wrappers (`Utils/`), test support (`testtools/`), and hardware cases (`moudles/` and `moudles/newModules/`). Android layouts, strings, images, and other resources live in `app/src/main/res/`; vendor JARs and AARs live in `app/libs/`.

`DV01Printer/` is a separate printer/SAM demo application with its own source, assets, AAR dependency, and tests. `pycharm/` contains legacy Python experiments and is not part of the Android build. Module membership is controlled by `settings.gradle` (note: `printer_sam_demo` is listed but does not exist on disk). `docs/` contains API documentation and test plans. `tools/` contains Python test automation and an EMV simulator.

## Architecture

The app binds to a remote Android service (`com.dreamtek.smartpos.deviceservice`) via AIDL IPC. `ServiceManager` (`Utils/ServiceManager.java`) is the central singleton managing this connection with thread-safe lifecycle, observer pattern, and automatic reconnect.

Two module systems coexist: the legacy monolithic `ServiceMoudle` (in `moudles/`) and the newer `ServiceModule`/`SystemServiceModule` (in `moudles/newModules/`). `MyApplication` bridges both. Hardware test cases are discovered via reflection (`Class.forName`, `getDeclaredMethods`) — method names like `B01001`, `D03028` encode the module letter and case number. Do not rename these methods arbitrarily.

## Build, Test, and Development Commands

Run commands from the repository root on Windows:

- `.\gradlew.bat clean` removes generated build output.
- `.\gradlew.bat :app:assembleDTK_Debug` builds the main test-client APK (flavor `DTK_` sets `INTENT_PKGNAME`/`INTENT_ACTION` BuildConfig fields).
- `.\gradlew.bat :DV01Printer:assembleDebug` builds the printer demo.
- `.\gradlew.bat test` runs all available host-side unit tests.
- `.\gradlew.bat lint` runs Android static analysis; review warnings even though lint is configured not to abort the build.

APK output is named `testclient_{versionName}_{buildType}_{timestamp}.apk`. Set the Android SDK path in untracked `local.properties`. Use a compatible POS device and matching device/system service packages for functional validation.

## Coding Style & Naming Conventions

Use four-space indentation, same-line opening braces, Java 8 syntax, and Android Studio’s standard formatter. Name classes in `PascalCase`, methods and fields in `lowerCamelCase`, constants in `UPPER_SNAKE_CASE`, and resources in `lower_snake_case`. Hardware case entry points conventionally use the `T_` prefix.

**Critical**: Preserve existing package spellings such as `moudles` (not `modules`); broad renames break reflection-based case discovery (`Class.forName("moudles.BeerMoudle")`). Test case method names like `B01001`, `A05003` encode module letter + case number — do not rename without updating all discovery logic.

## Testing Guidelines

Place JVM tests in `<module>/src/test/java/` and device tests in `<module>/src/androidTest/java/`; name classes `*Test.java`. `DV01Printer` uses JUnit 4 and AndroidX/Espresso. Run `:DV01Printer:testDebugUnitTest` for its local tests and `:DV01Printer:connectedDebugAndroidTest` with a connected device. For hardware changes, report the device model, service/JAR version, case IDs, and observed result; emulator success is not evidence for card readers, PIN pads, printers, or JNI paths.

## Commit & Pull Request Guidelines

History favors short, imperative, scope-specific messages such as `新增sde测试案例` or `更新systemService jar包`. Include case IDs when relevant. Pull requests should summarize affected modules and hardware, list validation commands and device results, link the issue, and attach screenshots for UI changes. Do not commit `local.properties`, signing keys, credentials, generated APKs, or unexplained vendor-binary updates.
