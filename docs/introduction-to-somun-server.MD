# Introduction to Somun Server

## Overview

The Somun Server project aims to create an open-source, modular, and user-friendly server software system for online multiplayer games. This project includes a server-side software module (somun-server) and client-side libraries and development interfaces (client SDKs) that can utilize this system. The modular architecture of the developed system makes it suitable for the basic needs of turn-based online game projects and easily adaptable to new requirements.

## Project Rationale

The Somun Server project was developed to reduce the software burden on developers in fundamental software modules such as stable data transmission, security, user registration and session processes, database storage/record keeping, and user matching. This approach aims to shorten the implementation process of projects and reduce server development and operation costs.

## Innovative Aspects

The Somun Server project has been developed following open-source application development principles. All software modules created have been hosted in publicly accessible code repositories and published under a free software license (MIT License) for everyone to use upon project completion. The open-source code repositories can be accessed via the following links:

- [Somun Server GitHub](https://github.com/deniza/somun-server)
- [Somun Flutter Client SDK GitHub](https://github.com/deniza/somun-flutter)
- [Somun Solar2D Client SDK GitHub](https://github.com/deniza/somun-solar2d)

## Key Software Technologies

The Somun project utilizes a broad range of technologies to achieve an open-source, modular, and easy-to-use server software system. The main software technologies used in the project include:

### Programming Languages
- **Java**: The main programming language used for server-side development, offering high performance and extensive library support.
- **Lua**: A scripting language used for client-side development with the Solar2D framework, enabling flexible and rapid prototyping.
- **Dart**: The programming language used for client-side development with the Flutter framework, allowing platform-independent application development.

### Frameworks and Libraries
- **Apache MINA**: A high-performance, event-driven network application framework used in the server network communication layer. Its multithreaded structure supports serving a high number of users.
- **Flutter**: A UI toolkit developed by Google for building natively compiled applications for mobile, web, and desktop from a single codebase.
- **Solar2D**: An open-source, fast, and easy-to-use game development framework.

### Database Technologies
- **MongoDB**: A NoSQL database system chosen for its schema-less operation and high performance, used for data storage and management.

## Developed Modules and Achievements

Several core modules planned for the Somun Server project have been developed, and the system is currently used in several published online game projects by He2 Apps. Additionally, the system has been utilized as a rapid prototyping tool for various online game prototypes within our company He2 Apps.

### 1. Network Communication Module
The networking layer, based on Apache MINA, supports multithreaded, binary RPC (Remote Procedure Call) communication. This layer replaced our previously used JSON-based communication layer, resulting in a 60% improvement in network data load and a 20% increase in the number of packets sent per unit time.

### 2. Social Interaction Module
The social messaging module allows players to communicate with each other, create friends lists, and send/receive text messages both in and out of the game (in the lobby).

### 3. Notification Module
Supporting Apple APNS and Google FCM, this module allows sending individual or group notifications to players' mobile devices when important events occur in the game.

### 4. Matchmaking Module
Players can match with each other to start new games through various matchmaking methods, including random matching, weighted average based on player statistics, invite-based matching, and matching via Facebook friends.

### 5. Session Management Module
This module, acting as a shell, encompasses the social interaction and account creation modules, ensuring the functionality of a lobby system in online games.

### 6. Account Management Module
The authentication layer allows players to create accounts and log in securely. It supports guest accounts and account creation via email verification, as well as quick account creation and login through Facebook.

### 7. Data Storage Module
A data storage layer based on MongoDB for storing game data with object serialization/deserialization, meeting the data storage needs efficiently.

### 8. Gameplay Module
This module includes the basic implementations of common game mechanics such as scoring, statistics management, game start/end, move logic, surrender, and turn-passing. These implementations are designed to be customizable through software interfaces and event mechanisms.

### 9. Client SDK Implementations

#### 9.1. Flutter Client SDK
A basic client SDK module for the Flutter framework was developed, enabling essential functionality to communicate with the server's network layer and handle remote procedure calls and events.

- [Somun Flutter SDK GitHub](https://github.com/deniza/somun-flutter)

#### 9.2. Solar2D Client SDK
A basic client SDK implementation for the Solar2D framework was developed, facilitating remote procedure calls and real-time event handling with the server.

- [Somun Solar2D SDK GitHub](https://github.com/deniza/somun-solar2d)

## Conclusion

The Somun Server system, created within the scope of the project, has been tested and developed under real-world conditions in our "Kelimelik" and "Arkadaş Okey" game projects and has become an integral part of these projects' server-side software architecture. The system provides significant advantages in new online game development projects and prototyping processes, such as:

- **Rapid Prototyping**: The modular structure and ready-to-use modules shorten the development time of new game mechanics and features from months to weeks.
- **Cost Savings**: Optimized network communication and data usage reduce data center costs and development/operation costs by utilizing ready-made infrastructure.
- **Developer Efficiency**: Client SDKs and comprehensive documentation facilitate easy server integration, improving developer productivity and ensuring smooth project development.

The Somun Server project is published under an open-source license on GitHub, and we believe that it will be further enhanced with support from the open-source community. This will enable the projects to reach a wider audience and be continuously improved with contributions from developers.
