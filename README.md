# MyJourneyBlog Project 🚀

## 🌐 Live Demo
#### 👉 https://myjourneymemo.blog

## 💡 Overview
**My personal blog** for documenting my learning journey, 
featuring daily learning posts, project updates, 
and track project lifecycles (Planning → In Progress → Completed).

**Note:** This is a personal portfolio project 
demonstrating my ability to build secure, 
production-ready full-stack applications with Spring Boot.

## 🛠️ Tech Stack

### </> Backend
- **🍃 Spring Boot 3+:** Core framework for REST APIs and business logic.
- **🛡️ Spring Security 6:** Robust security using custom filters.
- **🔐JWT(JSON Web Token):** Token-based,Stateless authentication mechanism.
- **🛢️ Spring Data JPA:** ORM for efficient database interactions.
- **🐘 PostgreSQL:** Production-grade relational database.
- **🧪 H2 Database:** In-memory database for integration testing.
- **📄 SpringDoc OpenAPI:** Automated API documentation (Swagger UI).
- **⚡ Caffeine Cache:** High-performance local caching for frequently accessed data.
- **⏳ Spring Async:** Asynchronous processing for non-blocking email notifications.
- **(to be done) WebFlux** - GitHub API Integration

### 🎨 Frontend
- **🍃 Thymeleaf Engine:** Server-side Templating
- **💅CSS:** Modern, responsive styling with custom variables.
- **📝 SunEditor:** Lightweight WYSIWYG rich-text editor for content creation.

### DevOps & Deployment
- **☁️Cloud Provider:** AWS EC2 (Ubuntu 22.04 LTS).
- **🚦 Web Server:** Nginx (Reverse Proxy).
- **🐳 Containerization:** Docker & Docker Compose.
- **🔄 CI/CD:** GitHub Actions workflow for automated build and deployment.
- **🔒 SSL/TLS:** Secured via Let's Encrypt (Certbot).

## ✨ Key Features

### 🔐 Secure Authentication System
- Custom **JWT Authentication** implementation with JwtAuthenticationFilter.
- Role-based access control (Admin vs. User).
- Password encryption using BCrypt.

### ⚡ Performance Optimization
- Implemented **Caffeine Caching** (@Cacheable, @CacheEvict) for frequent read operations like fetching posts and user profiles.
- Solved **N+1 Query Problems** in JPA using JOIN FETCH for fetching Authors and Comments alongside Posts.

### 🐙 GitHub Integration(To be done)
- **Automated Sync:** Planned integration using Spring WebFlux to connect with the GitHub API.
- **Commit Tracking:** Automatically fetch commit history to generate "Project Updates" based on actual code activity.

### 📝 Content Management
- **Learning Posts:** Track daily learnings with categories and rich text.
- **Quick Access:** "Pin" favorite or important posts for global visibility.
- **File Storage:** Custom service to handle profile picture and post image uploads to the local file system.

## ☁️ Deployment Architecture
The application is deployed on an AWS EC2 Ubuntu instance using a production-ready containerized setup.

### **🏗️ Infrastructure Setup**
- **AWS EC2 Instance:** Ubuntu 22.04 Micro instance hosting the infrastructure.
- **Docker:** The Spring Boot application runs inside a Docker container on port 8080.
- **Nginx:** Acts as a reverse proxy, listening on ports 80 (HTTP) and 443 (HTTPS) and forwarding traffic to the Docker container.
- **SSL/TLS:** Automated certificate management via Certbot (Let's Encrypt) ensures HTTPS security.

## ⚙️ Prerequisites for Local Development
- **♨️ Java 17** or higher
- **📦 Maven 3.6**
- **🐘 PostgreSQL 12**
- **🐙 Git**

## 📬 Contact
**📧 Email:** yuttitham_ning@hotmail.com

© 2025 My Journey Blog. All Rights Reserved.
