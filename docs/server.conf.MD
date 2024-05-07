## Somun Server Configuration File (`server.conf`)

This document explains the configuration options found in the `server.conf` file for Somun Game Server project.

**1. Server Configuration**

* **`port` (Integer):** This option specifies the port on which the Somun server listens for incoming connections. The default value is `16666`.

**2. Database Configuration**

* **`mongo_uri` (String):** This option defines the connection URI for the MongoDB database that your Somun server will use. The URI format typically follows `mongodb://host:port/`

**3. Email Configuration (Optional)**

* **`mail_enabled` (Integer):** This option enables or disables email functionality for the server. Set it to `1` to enable email sending, and `0` to disable it (default).

**4. Notification Configuration (Optional)**

* **`notification_enabled` (Integer):** This option enables or disables notification functionality for the server. Set it to `1` to enable notifications, and `0` to disable them (default).

**5. Firebase Notification Configuration (Optional - Requires `notification_enabled` to be 1)**

These options are used for sending notifications through Firebase:

* **`notification_firebase_credentials` (String):** Path to the Firebase service account key JSON file, required for authentication.
* **`notification_firebase_database_url` (String):** URL of the Firebase Realtime Database instance you want to use.
* **`notification_firebase_enable_dryrun` (Integer):** Controls dry run mode for Firebase notifications:
    * `0`: Send notifications normally.
    * `1`: Enable dry run mode to send test notifications without actual delivery.
* **`notification_firebase_channel` (String):** The Firebase notification channel to use for sending notifications.
* **`notification_firebase_click_action` (String):** The action to perform on a device when a notification is clicked.
* **`notification_firebase_sound` (String):** The sound to play on the device when a notification is received (optional).

**6. APNS Notification Configuration (Optional - Requires `notification_enabled` to be 1)**

These options are used for sending notifications through Apple Push Notification Service (APNS):

* **`notification_apns_credentials` (String):** Path to the APNS credentials P12 file containing your certificates.
* **`notification_apns_credentials_password` (String):** Password for the APNS credentials P12 file (if applicable).
* **`notification_apns_enable_sandbox` (Integer):** Controls the APNS environment:
    * `0`: Use production APNS servers for sending notifications.
    * `1`: Use APNS sandbox for development testing.
* **`notification_apns_topic` (String):** The APNS topic to which notifications should be sent.
* **`notification_apns_sound` (String):** The sound to play on the device when an APNS notification is received (optional).

**7. Facebook Configuration**

* **`facebook_thread_count` (Integer):** This option specifies the number of threads used for Facebook Connect interactions.

**Important Notes:**

* Replace placeholder values (e.g., paths, URLs) with your actual configuration details.
* Make sure to restart the Somun server after making changes to the `server.conf` file for the new settings to take effect.
* Always keep a backup of the original `server.conf` file before making any modifications.
* Refer to the official Somun documentation for more detailed information on server configuration options and settings.