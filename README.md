# LangBuddy (formerly LangSupport): a companion app for languages

Android app to support the learning of new languages by storing a personal "word-log" of learnt lemmas, along with stats related to their usage and recall rate.

## Usage

The code in this repository works with minimum SDK version 24, and target SDK version 33.

**ATTENTION:** the project as-is won't start because of the [Firebase](https://firebase.google.com/?hl=en) dependency, which requires a specific file (named `google-services.json`) inside the root of the `app` module. Plese refer to [this guide](https://firebase.google.com/docs/android/setup?hl=en) on how to create a new Firebase project and link it to the app.
