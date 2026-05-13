Overview

Serenity is an Android journaling application developed using Kotlin and Jetpack Compose. The application allows users to privately write journal entries, analyse emotional sentiment, and view mood trends over time while keeping all sensitive data stored locally on-device.

The project was designed around a privacy-first approach where journal entries and mood insights remain stored locally using Room Database rather than external cloud services.

Features
User registration and login
Journal entry creation
Local sentiment analysis
Mood insights and analytics
Mood trend graphs
Search and filter functionality
Delete journal entries
Offline-first design
Privacy-focused local storage

Technologies Used
Kotlin
Jetpack Compose
Room Database
SQLite
Android Studio
GitHub
MPAndroidChart

How It Works

Users create journal entries directly within the Serenity application. Each entry is analysed locally using a lightweight sentiment analysis system which classifies emotional tone as positive, negative, or neutral.

Sentiment scores and journal entries are stored locally within Room Database and used to generate mood insights and visual mood trend graphs.

Privacy

Serenity was designed with a strong focus on user privacy.

No cloud storage
No external AI APIs
No internet connection required
All data remains stored locally on-device

This allows users to reflect on sensitive emotions privately without uploading journal content online.

Installation
Clone the repository
Open the project in Android Studio
Sync Gradle files
Run the project using an Android Emulator or Android device
Future Improvements

Possible future improvements include:

Advanced AI sentiment models
Voice journaling
Encrypted cloud backup
Cross-platform support
Personalised wellbeing suggestions

Author

Alexandra Phelan
x20245823

Final Year Project – National College of Ireland
