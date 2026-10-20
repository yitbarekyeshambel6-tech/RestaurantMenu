# Habesha Food — Android Restaurant Menu App

Starter Android app built with Kotlin and Jetpack Compose.

## Included
- Food menu with images, names, categories and ETB prices
- Category filters
- Add/remove quantity controls and shopping cart
- Total calculation
- Customer name, phone and optional delivery address
- GitHub Actions workflow to build a debug APK

## Build an APK from GitHub (no Android Studio required)
1. Create a GitHub repository named `RestaurantMenuApp`.
2. Upload all files and folders from this project to the repository's `main` branch.
3. Open the repository's **Actions** tab and choose **Build Android APK**.
4. Click **Run workflow** (or push a commit to `main`).
5. Wait for the workflow to finish successfully.
6. Open the completed workflow run and download the `Habesha-Food-debug-apk` artifact.
7. Extract the downloaded ZIP; it contains `app-debug.apk`. Transfer it to your Android phone and install it. Android may ask you to allow installation from that source.

## Project files
- `app/src/main/java/com/example/restaurantmenu/MainActivity.kt`: UI, sample menu, cart and demo checkout
- `app/build.gradle.kts`: Android dependencies and SDK settings
- `.github/workflows/build-apk.yml`: cloud APK build workflow

## Customize menu
Edit the `foods` list in `MainActivity.kt`. Each Food has `name`, `category`, `description`, `price`, and `image`. Prices are sample ETB amounts; replace them with your real menu and use image URLs you have permission to use.

## Important limitation
The Place Order button currently validates required fields and shows a demo message. It does **not** send or store orders. To accept real customer orders, connect a secure backend or ordering service. This workflow produces a debug APK for testing, not a Play Store release build.
