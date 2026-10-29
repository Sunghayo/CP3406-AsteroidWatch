# Asteroid Watch

Asteroid Watch is a small Android utility app that shows near-Earth asteroid approaches for the current date. It uses the NASA NeoWs feed to show a quick daily summary, followed by a list sorted by closest distance to Earth.

This project started from the CP3406 MobileTech Utility App template. It keeps the two-tab layout and replaces the counter with live data.

## Features

- Today's close approach count and potentially hazardous asteroid (PHA) count
- Approximate size, closest distance and relative speed for each object
- Kilometers/miles and meters/feet display settings
- Optional PHA-only list filter
- Loading, empty-result and network-error states, plus a refresh button

A PHA classification **does not** mean a collision is predicted. The UI intentionally avoids describing the classification as an impact warning.

## Run in Android Studio

1. Open this folder as an Android Studio project and wait for Gradle Sync.
2. The default API key is NASA's `DEMO_KEY`, so the app can be tested without configuring a key. DEMO_KEY has low rate limits.
3. For a personal NASA key, add the following line to your **user** `~/.gradle/gradle.properties` (not the project's tracked file):

   `NASA_API_KEY=your_key_here`

4. Build and run on an Android emulator or device with Internet access (min SDK 26).
5. Run `app/src/test` from Android Studio to execute the unit tests.

The app requests the current device-local date. This is a daily *close approaches* tracker, not a prediction of asteroid impacts. No personal data or location permission is needed.

## Implementation

- **UI:** Compose + Material 3. The starter template's bottom navigation is preserved.
- **State:** `AsteroidViewModel` holds the fetched data and display settings using `StateFlow`.
- **Network:** `AsteroidRepository` calls NASA NeoWs using Retrofit + Gson.
- **Data:** DTOs are mapped to `Asteroid` model objects for the UI.
- **Settings:** Saved in memory during the app session only, as permitted by the task.
- **Tests:** `FormattingTest` verifies unit conversions. `AsteroidRepositoryTest` uses a fake API to check sorting, missing fields, and empty responses without making live network requests.

## Limitations

- Requires Internet access; there is no offline data cache.
- Refreshing also checks the current device date, so the feed updates correctly after midnight.
- Empty/error handling is provided, but this project has not been validated on every device size.

## Data sources

- NASA Open APIs – NeoWs: https://api.nasa.gov/
- NASA/JPL CNEOS FAQ – What is a PHA?: https://cneos.jpl.nasa.gov/faq/new.html

## Academic integrity and acknowledgements

Starter code: CP3406 MobileTech Utility App teaching template. NASA NeoWs supplies asteroid data. Record any other AI assistance and external sources in the **required university declaration** according to the assessment instructions. The author should add their own accurate development notes and reflection before submission.

## Assessment 1 rubric checklist

| Criterion | Implementation evidence |
| --- | --- |
| Code quality (20%) | Separate model, data, view-model and UI files; descriptive names; focused functions |
| Utility purpose (20%) | At-a-glance daily totals, most relevant information first, sorted closest approaches |
| Android API (20%) | Internet permission, Retrofit-powered NASA NeoWs web API, error handling |
| Jetpack Compose (20%) | Reusable cards, Material 3, Settings screen that updates the Today screen instantly |
| GitHub (10%) | **Student action:** push this project and make honest incremental commits as changes are made; add screenshots and maintain README |
| Self-reflection (10%) | **Student action:** write about actual implementation decisions, bugs, and lessons learned |

The code is a starting working version to validate in Android Studio; do not claim a successful device build until you have run it.
