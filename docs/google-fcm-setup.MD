## Setting Up Firebase for FCM Notifications in Somun Server Project

This guide explains how to configure a Firebase project to use as an FCM (Firebase Cloud Messaging) backend for your Somun server project, enabling push notifications for Android devices.

**Prerequisites:**

* A Google account

**Steps:**

1. **Create a Firebase Project:**

    * Visit the Firebase console ([https://console.firebase.google.com/](https://console.firebase.google.com/)) and sign in with your Google account.
    * Click on "Add project" and provide a unique name for your Somun project (e.g., SomunServerNotifications).
    * Select your preferred region (where your project's data will be stored) and click "Create project".

2. **Enable FCM:**

    * In the Firebase console project overview, navigate to the "Project settings" section (gear icon).
    * Go to the "Your apps" tab and click "Add app".
    * Select "Android" for the platform.
    * Follow the on-screen instructions to register your app.

3. **Create a Service Account**

    * In the Firebase console project settings, navigate to "Service accounts" under the "Project settings" tab.
    * Click on "Generate new private key" and give it a descriptive name (e.g., somun-server-fcm).
    * Download the JSON file containing the private key (serviceAccountKey.json). **Store this file securely** as it will be used to authenticate your Somun server with FCM.

**4. Integrate with Somun Server:**

    * Provide the downloaded private key JSON file (serviceAccountKey.json) to your Somun server. Update server.conf with the path to this file.

**Additional Notes:**

* This guide provides a general overview. Refer to the official Firebase documentation ([https://firebase.google.com/docs/cloud-messaging](https://firebase.google.com/docs/cloud-messaging)) for detailed steps and explanations.
* Securely store your configuration files and service account credentials. Avoid committing them to public repositories.

By following these steps, you can set up a Firebase project and configure FCM to send push notifications to Android devices in your Somun server project. Remember to consult the official Firebase documentation for more advanced configurations and functionalities. 