## Somun Server Configuration File (`server.conf`)

This document explains the configuration options found in the `server.conf` file for the Somun Game Server project.

### 1. Server Configuration

* **`port` (Integer):** This option specifies the port on which the Somun server listens for incoming connections. The default value is `16666`.

### 2. Database Configuration

* **`mongo_uri` (String):** This option defines the connection URI for the MongoDB database that your Somun server will use. The URI format typically follows `mongodb://host:port/`
* **`mongo_database` (String):** This option specifies the name of the MongoDB database to use. The default value is `somunDB_test`.

### 3. Admin Interface Configuration (Optional)

* **`admin_interface_enabled` (Integer):** This option enables or disables the admin/stats HTTP endpoint. Set it to `1` to enable the admin interface, and `0` to disable it (default).
* **`admin_interface_port` (Integer):** This option specifies the port on which the admin interface listens. The default value is `8080`.

### 4. Thread Configuration

* **`network_threads` (Integer):** This option specifies the number of threads dedicated to handling incoming connections. The default value is `4`.
* **`worker_threads` (Integer):** This option specifies the number of threads dedicated to handling all gameplay workload. The default value is `32`.

### 5. Email Configuration (Optional)

* **`mail_enabled` (Integer):** This option enables or disables email functionality for the server. Set it to `1` to enable email sending, and `0` to disable it (default).
* **`mail_host` (String):** This option specifies the hostname or IP address of the mail server (if email is enabled). The default value is `localhost`.
* **`mail_from_address` (String):** This option specifies the email address used for sending messages (if email is enabled).
* **`mail_threads` (Integer):** This option specifies the number of threads used for handling email sending (if email is enabled). The default value is `4`.

### 6. Notification Configuration (Optional)

* **`notification_enabled` (Integer):** This option enables or disables notification functionality for the server. Set it to `1` to enable notifications, and `0` to disable them (default).

### 7. Firebase Notification Configuration (Optional - Requires `notification_enabled` to be 1)

These options are used for sending notifications through Firebase:

* **`notification_firebase_credentials` (String):** Path to the Firebase service account key JSON file, required for authentication.
* **`notification_firebase_database_url` (String):** URL of the Firebase Realtime Database instance you want to use.
* **`notification_firebase_enable_dryrun` (Integer):** Controls dry run mode for Firebase notifications:
    * `0`: Send notifications normally.
    * `1`: Enable dry run mode to send test notifications without actual delivery.
* **`notification_firebase_channel` (String):** The Firebase notification channel to use for sending notifications.
* **`notification_firebase_click_action` (String):** The action to perform on a device when a notification is clicked.
* **`notification_firebase_sound` (String):** The sound to play on the device when a notification is received (optional).

### 8. APNS Notification Configuration (Optional - Requires `notification_enabled` to be 1)

These options are used for sending notifications through Apple Push Notification Service (APNS):

* **`notification_apns_credentials` (String):** Path to the APNS credentials P12 file containing your certificates.
* **`notification_apns_credentials_password` (String):** Password for the APNS credentials P12 file (if applicable).
* **`notification_apns_enable_sandbox` (Integer):** Controls the APNS environment:
    * `0`: Use production APNS servers for sending notifications.
    * `1`: Use APNS sandbox for development testing.
* **`notification_apns_topic` (String):** The APNS topic to which notifications should be sent.
* **`notification_apns_sound` (String):** The sound to play on the device when an APNS notification is received (optional).

### 9. Facebook Configuration

* **`facebook_thread_count` (Integer):** This option specifies the number of threads used for Facebook Connect interactions. The default value is `4`.

### 10. Server Limits Configuration

These options configure various server limits to ensure proper operation and security:

* **`limit_connections_per_second` (Integer):** This option specifies the maximum number of connections allowed per second. The default value is `10`.
* **`limit_create_account_per_ip_per_hour` (Integer):** This option specifies the maximum number of account creations allowed per IP address per hour. The default value is `10`.
* **`limit_rpc_calls_per_session_per_second` (Integer):** This option specifies the maximum number of RPC calls allowed per session per second. The default value is `5`.
* **`limit_max_allowed_new_guest_accounts_per_day` (Integer):** This option specifies the maximum number of new guest accounts allowed per day. The default value is `30000`.
* **`limit_max_failed_login_attempts_per_ip_per_minute` (Integer):** This option specifies the maximum number of failed login attempts allowed per IP address per minute. The default value is `10`.
* **`limit_max_failed_login_attempts_per_player_per_minute` (Integer):** This option specifies the maximum number of failed login attempts allowed per player per minute. The default value is `3`.
* **`limit_max_allowed_sessions` (Integer):** This option specifies the maximum number of allowed sessions. The default value is `100000`.
* **`limit_max_allowed_active_games` (Integer):** This option specifies the maximum number of allowed active games. The default value is `1000000`.

### Important Notes:

* Replace placeholder values (e.g., paths, URLs) with your actual configuration details.
* Make sure to restart the Somun server after making changes to the `server.conf` file for the new settings to take effect.
* Always keep a backup of the original `server.conf` file before making any modifications.
* Refer to the official Somun documentation for more detailed information on server configuration options and settings.
