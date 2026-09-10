# SkyBrowser — Mobile-only online APK build

This version is prepared for GitHub Actions, so the APK can be built from a phone browser without Android Studio.

## Phone steps

1. Create/sign in to a GitHub account.
2. Create a NEW repository, for example `SkyBrowser`.
3. On the repository page, choose **Add file → Upload files**.
4. Extract this ZIP first. Upload the PROJECT CONTENTS, not the outer ZIP:
   - `.github/`
   - `app/`
   - `build.gradle`
   - `settings.gradle`
   - `gradle.properties`
   - `README.md`
   - `MOBILE_BUILD.md`
5. Commit the files to the `main` branch.
6. Open the repository's **Actions** tab.
7. Select **Build SkyBrowser APK**.
8. Tap **Run workflow**.
9. Wait for the workflow to finish.
10. Open the completed workflow run and download the **SkyBrowser-debug-apk** artifact.
11. Extract the downloaded artifact and install `app-debug.apk` on your Android phone.

## Why this works

GitHub Actions supplies a Linux build machine. The workflow installs Java 17 and Gradle 8.7, then runs `gradle :app:assembleDebug`. Gradle's official documentation supports using `setup-gradle` in GitHub Actions.

## Notes

- The APK is a DEBUG build for testing.
- Android may ask you to allow installation from the browser/file manager.
- For Play Store release, create a signed release build and protect the signing key; do not put keystore passwords in the repository.
