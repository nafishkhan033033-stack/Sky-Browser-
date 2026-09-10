# SkyBrowser 3.0 — Complete WebView Browser

This project combines a real Android System WebView/Chromium rendering engine with a native browser shell.

Included:
- Real HTTPS website loading and address/search bar
- Google search fallback
- Back / forward / reload
- Multiple tabs
- Bookmarks and history persistence
- Clear browsing data
- Dark-mode support when the installed WebView supports it
- JavaScript and DOM storage
- Download handoff to Android
- File chooser support
- Safe Browsing enabled
- Mixed-content blocked
- Local app assets served through WebViewAssetLoader
- External URI schemes handed to Android
- WebView renderer lifecycle handled by Android

Important:
This uses the Chromium-based Android System WebView installed on the phone. It is a real browser rendering engine, but it is not a separately bundled Chromium fork. A separately bundled Chromium engine is a much larger native build and is not practical as a small Android Studio ZIP.

Build:
1. Open this folder in Android Studio.
2. Let Gradle sync/download dependencies.
3. Connect an Android phone with USB debugging, or start an emulator.
4. Run the app.
5. Or choose Build > Build APK(s).

If Android Studio asks to install Android SDK 35 or Gradle components, allow it.
