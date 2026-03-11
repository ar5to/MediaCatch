# 🔐 Signing Setup Guide

This document explains how to configure APK signing for the MediaCatch release workflow.

---

## 1. Generate a Keystore (one-time setup)

If you don't already have a keystore:

```bash
keytool -genkeypair \
  -v \
  -keystore mediacatch.jks \
  -alias mediacatch \
  -keyalg RSA \
  -keysize 2048 \
  -validity 10000
```

You will be prompted for:
- **Keystore password** → remember this (`KEY_STORE_PASSWORD`)
- **Key alias** → use `mediacatch` (`KEY_ALIAS`)
- **Key password** → remember this (`KEY_PASSWORD`)
- Name, org, country info

> ⚠️ **Never commit `mediacatch.jks` to the repo.** It is in `.gitignore`.

---

## 2. Encode the Keystore to Base64

```bash
base64 -w 0 mediacatch.jks > mediacatch.b64
cat mediacatch.b64
```

Copy the full output — you'll use it as the `KEYSTORE_BASE64` secret.

---

## 3. Add GitHub Repository Secrets

Go to: **Settings → Secrets and variables → Actions → New repository secret**

| Secret Name | Value |
|-------------|-------|
| `KEYSTORE_BASE64` | Base64-encoded keystore (from step 2) |
| `KEY_STORE_PASSWORD` | Your keystore password |
| `KEY_ALIAS` | `mediacatch` (or your alias) |
| `KEY_PASSWORD` | Your key password |

---

## 4. Trigger a Release

### Option A — Push a tag (recommended)

```bash
git tag v1.0.0
git push origin v1.0.0
```

The `release.yml` workflow will automatically:
1. Build and sign the release APK + AAB
2. Generate a changelog from commit history
3. Create a GitHub Release with both artifacts attached

### Option B — Manual trigger

Go to **Actions → Release → Run workflow** and enter the version tag.

---

## 5. Workflow Overview

| Workflow | Trigger | Purpose |
|----------|---------|---------|
| `ci.yml` | Push to `main`/`develop`/`feature/**` | Lint + tests + debug build |
| `pr-checks.yml` | Pull requests to `main`/`develop` | Fast PR feedback + APK size comment |
| `release.yml` | Push `v*.*.*` tag | Signed APK + AAB + GitHub Release |

---

## 6. Versioning Convention

MediaCatch follows [Semantic Versioning](https://semver.org/):

```
v{MAJOR}.{MINOR}.{PATCH}

Examples:
  v1.0.0   → Initial release
  v1.1.0   → New features
  v1.1.1   → Bug fix
  v2.0.0   → Breaking change / major redesign
  v1.0.0-beta.1  → Pre-release (marked as pre-release on GitHub)
```
