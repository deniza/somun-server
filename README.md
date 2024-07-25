# SOMUN: Open Source Game Server

[![Licence](https://img.shields.io/github/license/Ileriayo/markdown-badges?style=for-the-badge)](./LICENSE)
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)


<img src="https://he2apps.com/somun/somun-logo-icon-small.png" alt="Somun Logo" width="64" height="auto">

SOMUN is a modular, open-source server software system written in Java, designed for developing and deploying online games. It provides core functionalities like network communication, user management, matchmaking, data storage, and gameplay mechanics. SOMUN aims to offer developers a flexible and efficient platform to build their online games without reinventing the wheel.

> Somun means "nut of a bolt" in Turkish, and it is a metaphor for the core of a game server that holds everything together.

## Features

<img src="https://he2apps.com/somun/somun-admin.png" alt="Somun Admin Page" width="auto" height="auto">

* **Modular design:** SOMUN consists of independent modules that can be easily integrated or extended to meet specific needs.
* **High performance:** Optimized network communication and data management for handling large numbers of concurrent users.
* **Scalability:** SOMUN can be deployed on various environments, from local development machines to cloud instances, scaling to accommodate growing player bases.
* **Open source:** Freely available for use, modification, and contribution under the MIT license.
* **Community support:** Encourages contributions and collaboration to enhance the platform and its capabilities.

## Getting Started

1. **Prerequisites:**
    * Java 8 or higher
    * Maven 3.6 or higher ([https://maven.apache.org/](https://maven.apache.org/))
    * MongoDB database instance ([https://www.mongodb.com/](https://www.mongodb.com/))
2. **Clone the repository:**
    ```bash
    git clone https://github.com/deniza/somun-server.git
    ```
3. **Build the project:**
    ```bash
    ./build.sh
    ```
4. **Configure MongoDB connection:**
   Update the configuration file server.conf with your MongoDB connection details.

5. **Initialize the database:**
    ```
    ./somun.sh setup
    ```
   
6. **Start the server:**
    ```bash
    ./somun.sh run
    ```

## Usage

A sample game client written in Java is included in the server distribution to showcase basic usage. You can find it in the `server/samplegame` directory. This game demonstrates turn-based multiplayer features and can be used as a starting point for building your own games.

## Client Libraries

SOMUN provides following client libraries for different platforms:
* somun-solar2d for Solar2d game engine: [somun-solar2d](https://github.com/deniza/somun-solar2d)
* somun-flutter for Flutter: [somun-flutter](https://github.com/deniza/somun-flutter)

## Documentation

Detailed documentation for Somun Server is available in the `docs` directory of this repository. You can start with the [Documentation Index](docs/index.MD) for an overview and links to specific topics.

## Contributing

SOMUN welcomes all forms of contributions from the community. This includes:

* **Bug fixes:** Report and fix bugs encountered while using SOMUN.
* **Code improvements:** Suggest and implement code enhancements to improve performance, maintainability, or functionality.
* **Refactorings:** Improve code structure and readability.
* **New modules:** Develop and integrate new modules to expand SOMUN's capabilities.
* **New sample games:** Create and contribute new sample games to showcase different functionalities and inspire developers.
* **Documentation improvements:** Enhance existing documentation or contribute new documentation for modules, features, and usage examples.

Please refer to the [CONTRIBUTING.md](docs/CONTRIBUTING.md) file for detailed guidelines and contribution processes.

## Main Contributors

* Deniz Aydınoğlu (Project Admin): https://github.com/deniza
* Neslihan Aydınoğlu: https://github.com/neslihe2

## Research Background

This project is developed as part of a research initiative at Odtü Teknokent. We aim to explore and advance the development of open-source server software for online games, focusing on modularity, scalability, and community collaboration.

[<img src="https://he2apps.com/somun/teknokent-logo.png" alt="Odtü Teknokent Logo">](https://odtuteknokent.com.tr)

## License

This project is licensed under the MIT license. See the [LICENSE.txt](LICENSE.txt) file for details.
