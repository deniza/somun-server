# SOMUN: Open Source Game Server

[![Licence](https://img.shields.io/github/license/Ileriayo/markdown-badges?style=for-the-badge)](./LICENSE)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)


<img src="https://he2apps.com/somun/somun-logo-icon-small.png" alt="Somun Logo" width="64" height="auto">

SOMUN is a modular, open-source server software system written in Java, designed for developing and deploying online games. It provides core functionalities like network communication, user management, matchmaking, data storage, and gameplay mechanics. SOMUN aims to offer developers a flexible and efficient platform to build their online games without reinventing the wheel.

> Somun means "nut of a bolt" in Turkish, and it is a metaphor for the core of a game server that holds everything together.

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
   Update the configuration file (e.g., server.conf) with your MongoDB connection details.
4. **Build the project:**
    ```bash
    mvn clean install
    ```
5. **Run the server:**
    ```bash
    java -jar target/Somun-1.0.jar
    ```

## Usage

A sample game client written in Java is included in the server distribution to showcase basic usage. You can find it in the `server/samplegame` directory. This game demonstrates turn-based multiplayer features and can be used as a starting point for building your own games.

## Contributing

SOMUN welcomes all forms of contributions from the community. This includes:

* **Bug fixes:** Report and fix bugs encountered while using SOMUN.
* **Code improvements:** Suggest and implement code enhancements to improve performance, maintainability, or functionality.
* **Refactorings:** Improve code structure and readability.
* **New modules:** Develop and integrate new modules to expand SOMUN's capabilities.
* **New sample games:** Create and contribute new sample games to showcase different functionalities and inspire developers.
* **Documentation improvements:** Enhance existing documentation or contribute new documentation for modules, features, and usage examples.

Please refer to the [CONTRIBUTING.md](docs/CONTRIBUTING.md) file for detailed guidelines and contribution processes.

## Research Background

This project is developed as part of a research initiative at Odtü Teknokent. We aim to explore and advance the development of open-source server software for online games, focusing on modularity, scalability, and community collaboration.

<img src="https://he2apps.com/somun/teknokent-logo.png" alt="Odtü Teknokent Logo">

## License

This project is licensed under the MIT license. See the LICENSE.txt file for details.

