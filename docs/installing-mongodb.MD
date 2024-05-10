## Installing MongoDB for Somun Server on Windows, macOS, and Linux

This guide explains how to install MongoDB, a NoSQL database, on Windows, macOS, and Linux for use with your Somun server project.

**Prerequisites:**

* Administrator privileges on your computer

**Choosing the Right Version:**

* It's recommended to use a Long-Term Support (LTS) version of MongoDB for stability. Refer to the official MongoDB download page ([https://www.mongodb.com/try/download/community](https://www.mongodb.com/try/download/community)) to choose the appropriate version compatible with your Somun server requirements and your operating system.

**Installation Steps:**

**1. Windows:**

* Download the MongoDB Community Server installer for Windows from the official website.
* Double-click the downloaded `.msi` file and follow the on-screen installation wizard.
* During installation, you can choose the data directory location (default: `C:\ProgramData\MongoDB`) and optionally set up a service account for running MongoDB.
* After successful installation, launch the MongoDB service using the Services Management Console (search for "services.msc"). Locate "MongoDB Server" and start the service.

**2. macOS:**

* **Using Homebrew (Recommended):**
    * Install Homebrew, a package manager for macOS, by following the instructions on their website ([https://brew.sh/](https://brew.sh/).)
    * Open a terminal window and run the following command to install MongoDB:

    ```bash
    brew install mongodb-community
    ```

    * This will download and install MongoDB automatically.

* **Manual Installation:**
    * Download the MongoDB Community Server package for macOS from the official website.
    * Extract the downloaded archive file.
    * Open a terminal window and navigate to the extracted directory (e.g., `cd /path/to/mongodb-macos-x86_64-<version>`)
    * Run the following command to start the MongoDB service:

    ```bash
    ./bin/mongod --dbpath /data/db
    ```

    * This will start MongoDB using the default data directory (`/data/db`). You can modify the `--dbpath` option to specify a different location.

**3. Linux:**

* **Using Package Manager:**
    * The installation method might vary slightly depending on your Linux distribution. Here are general guidelines:
        * **Debian/Ubuntu:**
            * Update package lists: `sudo apt update`
            * Install MongoDB: `sudo apt install mongodb`
        * **CentOS/RHEL:**
            * Import the official MongoDB repository key: `curl -sSL https://www.mongodb.org/static/吭吭吭/吭吭吭/GPG-KEY-PUBLIC | sudo apt-key add -`
            * Create a repository file: `echo 'mongodb-org repo' | sudo tee /etc/yum.repos.d/mongodb-org.repo`
            * Update package lists: `sudo yum update`
            * Install MongoDB: `sudo yum install mongodb-org`
    * After installation, start the MongoDB service using the appropriate command for your distribution (e.g., `sudo systemctl start mongod` on systemd-based systems).

* **Manual Installation:**
    * Download the MongoDB Community Server package for your Linux distribution from the official website.
    * Extract the downloaded archive file.
    * Open a terminal window and navigate to the extracted directory.
    * Run the following command to start the MongoDB service:

    ```bash
    ./bin/mongod --dbpath /data/db
    ```

    * This will start MongoDB using the default data directory (`/data/db`). You can modify the `--dbpath` option to specify a different location.

**Post-Installation:**

* **Windows:**
    * You can connect to the MongoDB shell using the following command in a terminal window: `mongo`
* **macOS/Linux:**
    * By default, MongoDB listens on port 27017. You can connect to the MongoDB shell using the following command in a terminal window:

    ```bash
    mongosh
    ```

* **Securing MongoDB (Optional):**
    * It's highly recommended to secure your MongoDB installation by setting up authentication and access controls. Refer to the official MongoDB documentation ([https://www.mongodb.com/docs/](https://www.mongodb.com/docs/)) for detailed instructions on securing your database.

**Using MongoDB with Somun Server:**

* Refer to your Somun server documentation or code for specific instructions on configuring the connection to your MongoDB database. You'll likely need to provide the following information:

  * **MongoDB connection URI (e.g., `mongodb://localhost:27017/somunDB_test`):**
      * This URI specifies the connection details for your MongoDB instance.
      * It typically follows the format `mongodb://host:port/database_name`.
      * Replace `localhost` with the hostname or IP address of your MongoDB server if it's running on a different machine.
      * Replace `27017` with the port on which your MongoDB instance is listening (default: 27017).
      * Replace `somunDB_test` with the actual name of the database you want your Somun server to use.
      * You should update the connection URI and database name in your Somun server configuration file: **server.conf**

  * **Authentication (if applicable):**
      * If you set up authentication during MongoDB installation, you'll need to provide the username and password in the connection URI or through the Somun server configuration.

**Testing the Connection:**

* Once you've installed and configured MongoDB, you can test the connection from your Somun server code.
* Refer to your Somun server documentation for specific instructions on how to establish and verify the connection to the MongoDB database.

**Additional Notes:**

* Consider using a dedicated user account with restricted permissions for your Somun server to connect to MongoDB.
* Keep MongoDB updated with the latest security patches to maintain a secure database environment.

By following these steps, you can install MongoDB on your Windows, macOS, or Linux system and configure it for use with your Somun server project. Remember to consult the official MongoDB documentation for more advanced configuration options and security best practices.