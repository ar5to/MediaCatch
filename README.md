<div align="center">

# 📥 MediaCatch

**Download any media, instantly.**

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android)](https://android.com)
[![API Level](https://img.shields.io/badge/Min%20SDK-26%20(Android%208.0)-blue?style=flat-square)](https://developer.android.com)
[![Material 3](https://img.shields.io/badge/Design-Material%203-1E88E5?style=flat-square)](https://m3.material.io)
[![License](https://img.shields.io/badge/License-MIT-green?style=flat-square)](LICENSE)
[![CI](https://github.com/ar5to/MediaCatch/actions/workflows/ci.yml/badge.svg)](https://github.com/ar5to/MediaCatch/actions/workflows/ci.yml)
[![Release](https://github.com/ar5to/MediaCatch/actions/workflows/release.yml/badge.svg)](https://github.com/ar5to/MediaCatch/actions/workflows/release.yml)
[![Latest Release](https://img.shields.io/github/v/release/ar5to/MediaCatch?style=flat-square&color=1E88E5)](https://github.com/ar5to/MediaCatch/releases/latest)

*by [ar5to](https://github.com/ar5to)*

</div>

---

## Overview

**MediaCatch** is a clean, modern Android app that lets you download videos and audio from 100+ social media platforms with a single tap. Built with Material Design 3, it offers a fast, beautiful, and intuitive experience.

Just copy a URL → open MediaCatch → tap the download button. That's it.

---

## Supported Platforms

MediaCatch supports downloading from:

| Social | Video | Audio |
|--------|-------|-------|
| YouTube | TikTok | SoundCloud |
| Instagram | Vimeo | Spotify |
| Facebook | Twitch | Mixcloud |
| X (Twitter) | Reddit | Bandcamp |
| Pinterest | Snapchat | and more… |
| LinkedIn | Rumble | |
| Threads | BitChute | |

> Powered by the **Social Download All-in-One** API via RapidAPI.

---

## Screenshots

| Home Screen | Download Progress | API Setup |
|:-----------:|:-----------------:|:---------:|
| ![Home](assets/screenshot_home.png) | ![Progress](assets/screenshot_progress.png) | ![API](assets/screenshot_api.png) |

> *Note: Screenshots are placeholders — build and run the app to see the real UI.*

---

## Features

- ✅ One-tap download from clipboard URL
- ✅ Share-to-download from any browser or app
- ✅ Supports both video (MP4) and audio (MP3)
- ✅ Files saved to `Movies/MediaCatch` or `Music/MediaCatch`
- ✅ Download progress with live notifications
- ✅ Material Design 3 UI (light theme, clean & modern)
- ✅ API key management screen with video tutorial
- ✅ Permission handling for Android 8–14+

---

## Getting Started

### Prerequisites

- Android Studio Hedgehog (2023.1.1) or newer
- Android SDK 34
- Java 8+
- A free [RapidAPI](https://rapidapi.com) account

### Build Instructions

```bash
# 1. Clone the repository
git clone https://github.com/ar5to/MediaCatch.git
cd MediaCatch

# 2. Open in Android Studio
# File → Open → select the MediaCatch folder

# 3. Sync Gradle
# Android Studio will prompt to sync — click "Sync Now"

# 4. Build the APK
./gradlew assembleDebug

# The APK will be at:
# app/build/outputs/apk/debug/app-debug.apk
```

### Install on Device

```bash
# Via ADB
adb install app/build/outputs/apk/debug/app-debug.apk

# Or use Android Studio's Run button (▶)
```

---

## API Setup

MediaCatch uses the [Social Download All-in-One](https://rapidapi.com/social-download-all-in-one/) API from RapidAPI.

**To get your free API key:**

1. Sign up at [rapidapi.com](https://rapidapi.com)
2. Search for **"Social Download All In One"**
3. Subscribe to the **Free plan**
4. Copy your API key from the **Code Snippets** section
5. Open MediaCatch → tap the ⚙️ Settings icon → paste your key

---

## Project Structure

```
MediaCatch/
├── app/
│   └── src/main/
│       ├── java/com/arsto/mediacatch/
│       │   ├── callback/        # Callback interfaces
│       │   ├── detector/        # Platform URL detection
│       │   ├── fetcher/         # API data fetchers
│       │   ├── helper/          # Application class
│       │   ├── model/           # Data models
│       │   ├── ui/              # Activities (Main, APIKey)
│       │   └── utils/           # Download, Dialog, UI utilities
│       ├── res/
│       │   ├── drawable/        # Vector icons & assets
│       │   ├── layout/          # XML layouts
│       │   └── values/          # Colors, strings, themes
│       └── AndroidManifest.xml
├── build.gradle
├── settings.gradle
└── README.md
```

---

## Dependencies

| Library | Purpose |
|---------|---------|
| `material:1.12.0` | Material Design 3 components |
| `appcompat:1.7.0` | Backwards-compatible Activity & UI |
| `constraintlayout:2.2.0` | Flexible layout system |
| `okhttp3:4.12.0` | HTTP networking for API calls |

---
## CI / CD

MediaCatch uses GitHub Actions for automated builds and releases.

| Workflow | Trigger | What it does |
|----------|---------|-------------|
| **CI** | Push to `main`, `develop`, `feature/**` | Lint → Unit tests → Debug APK |
| **PR Checks** | Any pull request | Lint + tests + APK size comment on PR |
| **Release** | Push tag `v*.*.*` | Signed APK + AAB → GitHub Release |

### Creating a Release

```bash
git tag v1.1.0
git push origin v1.1.0
```

See [SETUP_SIGNING.md](SETUP_SIGNING.md) for keystore configuration.

---


## Contributing

Contributions are welcome! Feel free to:

- Open an [Issue](https://github.com/ar5to/MediaCatch/issues) for bugs or feature requests
- Submit a [Pull Request](https://github.com/ar5to/MediaCatch/pulls) for improvements

---

## License

```
MIT License

Copyright (c) 2024 ar5to

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
```

---

<div align="center">

Made with ❤️ by **[ar5to](https://github.com/ar5to)**

</div>
