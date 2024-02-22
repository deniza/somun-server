# SOMUN: Online Games Server

[![License: MIT](https://img.shields.io/badge/License-MIT-red.svg)](https://opensource.org/licenses/MIT)

<img src="https://he2apps.com/somun/somun-logo-icon-small.png" alt="image" width="64" height="auto" style="float: left; margin: 20px;">

SOMUN is a modular, open-source server software system written in Java, designed for developing and deploying online games. It provides core functionalities like network communication, user management, matchmaking, data storage, and gameplay mechanics. SOMUN aims to offer developers a flexible and efficient platform to build their online games without reinventing the wheel.

## Features

* **Modular design:** SOMUN consists of independent modules that can be easily integrated or extended to meet specific needs.
* **High performance:** Optimized network communication and data management for handling large numbers of concurrent users.
* **Scalability:** SOMUN can be deployed on various environments, from local development machines to cloud instances, scaling to accommodate growing player bases.
* **Open source:** Freely available for use, modification, and contribution under the MIT license.
* **Community support:** Encourages contributions and collaboration to enhance the platform and its capabilities.

## Getting Started

1. **Prerequisites:**
    * Java 8 or higher
    * MongoDB database instance ([https://www.mongodb.com/](https://www.mongodb.com/))
2. **Clone the repository:**
    ```bash
    git clone https://github.com/deniza/somun-server.git
    ```
3. **Configure MongoDB connection:**
   Update the configuration file (e.g., application.properties) with your MongoDB connection details.
4. **Build the project:**
    ```bash
    mvn clean install
    ```
5. **Run the server:**
    ```bash
    java -jar target/Somun-1.0.jar
    ```

## Usage

A sample game client written in Java is included in the server distribution to showcase basic usage. You can find it in the `samples/guess-the-number` directory. This game demonstrates turn-based multiplayer features and can be used as a starting point for building your own games.

## Contributing

SOMUN welcomes all forms of contributions from the community. This includes:

* **Bug fixes:** Report and fix bugs encountered while using SOMUN.
* **Code improvements:** Suggest and implement code enhancements to improve performance, maintainability, or functionality.
* **Refactorings:** Improve code structure and readability.
* **New modules:** Develop and integrate new modules to expand SOMUN's capabilities.
* **New sample games:** Create and contribute new sample games to showcase different functionalities and inspire developers.
* **Documentation improvements:** Enhance existing documentation or contribute new documentation for modules, features, and usage examples.

Please refer to the CONTRIBUTING.md file for detailed guidelines and contribution processes.

## License

This project is licensed under the MIT license. See the LICENSE.txt file for details.