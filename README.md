# Flumblr-Backend

## Introduction 
Flumblr's backend is primarily built using the Spring Boot framework. The application is grounded on a foundation of Core Java and utilizes a PostgreSQL database to store user information, posts, preferences and interactions. The purpose of this application is to provide a platform for users to share short updates, post content like images, videos and longer text, interact with the larger community, and discover new content, all while ensuring a personalized and safe environment.

## User Stories

- As a user, I want to be able to create an account.
- As a user, I want to be able to verify my account so that others know it's authentic.
- As a user, I want to be able to post short text updates (like Twitter) so that I can share my thoughts and ideas.
- As a user, I want to be able to post images, videos, and longer text posts (like Tumblr) so that I can share a variety of content.
- As a user, I want to be able to follow other users so that I can keep up with their posts.
- As a user, I want to be able to like, comment on, and share posts so that I can interact with the community.
- As a user, I want to be able to customize my profile with a profile picture, bio, and theme so that I can express my personality.
- As a user, I want to be able to search for other users and hashtags so that I can discover new content.
- As a user, I want to be able to receive notifications when someone interacts with my posts or profile so that I can stay engaged with the community.
- As a user, I want to be able to report inappropriate content or behavior to ensure the platform remains a safe space.
- As a user, I want to be able to save or bookmark posts so that I can easily find them later.
- As a user, I want to be able to see trending topics or hashtags so that I can stay updated with what's popular.
- As a user, I want to be able to tag other users in my posts so that I can engage with them directly.
- As a user, I want to be able to delete or edit my posts so that I can manage my content.
- As a user, I want to be able to use a dark mode so that I can use the application comfortably in different lighting conditions.
- As a user, I want to be able to use a variety of emojis and GIFs in my posts and comments so that I can express myself more creatively.
- As a user, I want to be able to receive recommendations for users to follow based on my interests so that I can discover new content.

## MVP (Minimum Viable Product)
- Secure user registration, login and verification.
- Posting and managing short text updates, images, videos, and longer text posts.
- Customizable user profiles with profile pictures, bios, and themes.
- A system for following other users and interacting with their posts.
- A search function for users and hashtags.
- A notification system for user interactions.
- A system for reporting inappropriate content or behavior.
- A bookmarking function for posts.
- Displaying trending topics or hashtags.
- Tagging function in posts.
- Dark mode functionality.
- Usage of emojis and GIFs in posts and comments.
- Recommendations for users to follow.

## Stretch Goals

- Implementing an algorithm to sort posts based on relevancy to the user.
- Adding an admin role that can moderate content.
- Secure subscription payments which unlock more features/storage space.
- Add more options for  expression in posts.

## Tech Stack

- **Spring Boot**: The framework used for building the application's backend.
- **Java**: The programming language used for building the application's backend.
- **PostgreSQL**: Used as the database to store user, post, interaction, and preference data.
- **Maven**: Used for managing project dependencies.
- **Log4j**: A logging utility for debugging purposes.
- **Spring Data JPA**: An API for connecting to and executing queries on the database.
- **BCrypt**: A Java library for hashing and checking passwords for security.
- **JUnit**, **Mockito**, and **PowerMock**: Used for unit and integration testing.
- **Git** and **GitHub**: Used for version control and building a CI/CD pipeline.
- **AWS** **RDS**, **EBS**, and **S3**: Used for deploying the database, backend and frontend. 


## Requirements

- **Clean Codebase**: The code should be clean, well-documented, and free from unnecessary files or folders. Files and directories should be appropriately named and organized.
- **Database Design**: The database should be designed as per the principles of the 3rd Normal Form (3NF) to ensure data integrity and efficiency. An Entity Relationship Diagram (ERD) should be included in the documentation.
- **Secure**: Sensitive user data such as passwords must be securely hashed before storing it in the database. The application should avoid displaying any sensitive information in error messages.
- **Error Handling**: The application should handle errors gracefully and provide clear and helpful error messages to the users.
- **Testing**: The application should have extensive test coverage. Unit tests and integration tests should be implemented using JUnit, Mockito, and PowerMock.
- **Version Control**: The application should be developed using a version control system, preferably Git, with regular commits indicating progress.
- **Documentation**: The repository should include a README file with clear instructions on how to run the application. Code should be well-commented to allow for easy understanding and maintenance.
- **Scalable**: The design of the application should be scalable, allowing for easy addition of new features or modifications in the future.

## ERD

![Flumbr ERD](https://github.com/052223-java-angular/Flumblr-Backend/assets/55551370/95038ea5-24f4-41df-976f-f49de8655c12)

