# Implementation Plan - Rebase Pull Request #3

The goal is to rebase the `create-habit` branch (PR #3) onto `master`, resolving conflicts and ensuring the app follows the requirements (especially using Room for data persistence).

## User Review Required

> [!IMPORTANT]
> The rebase involves resolving conflicts in `HabitViewModel.kt` and `main_navigation.xml`. I will prioritize the Room database implementation over SharedPreferences, as per the project requirements.

## Proposed Changes

### [Component Name]

#### [MODIFY] [HabitViewModel.kt](file:///C:/File%20Kuliah/Semester%206/ANMP/project/Project%20UTS/app/src/main/java/com/aenempeh/habittracker/viewmodel/HabitViewModel.kt)
- Resolve conflicts by choosing the Room database implementation.
- Remove SharedPreferences-related code for habit persistence.
- Ensure CoroutineScope is properly implemented for database operations.

#### [MODIFY] [main_navigation.xml](file:///C:/File%20Kuliah/Semester%206/ANMP/project/Project%20UTS/app/src/main/res/navigation/main_navigation.xml)
- Resolve conflicts by merging the `habitEditFragment` definition and its arguments.
- Ensure all actions are correctly defined.

## Verification Plan

### Automated Tests
- I will attempt to build the project using `./gradlew assembleDebug` to ensure there are no compilation errors after the rebase.

### Manual Verification
- After rebasing, I will verify that the code logic correctly uses Room database for habit operations.
