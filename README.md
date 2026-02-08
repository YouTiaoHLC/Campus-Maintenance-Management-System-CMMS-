# Campus Maintenance and Management System (CMMS)
📊 Project Overview
A comprehensive Java Swing-based desktop application for managing campus maintenance operations, including facility management, workforce scheduling, emergency planning, and academic course coordination.

# 🚀 System Features

Core Modules
Database Modification - CRUD operations for all entities

SQL Queries - Direct database query interface

Activity Information - Track maintenance activities and schedules

Other Searches - Advanced search functionalities  (Conducted by me)

Report Generation - Generate system reports and analytics

Advanced Search Capabilities
External Company Requests - Manage contractor communications

Management Hierarchy - View organizational structure

Weather-based Scheduling - Adapt plans based on weather conditions

Worker Activities - Track staff tasks and assignments

Course Schedule - Coordinate academic activities with maintenance

# 🏗️ Architecture

Frontend: Java Swing GUI with modern card-based navigation

Backend: MySQL database with JDBC connectivity


# 🛠️ Technical Implementation

Database Schema
The system manages multiple interconnected entities:

Executive Officers (EXECUTE_OFFICIER)

Middle Managers (MID_MANAGER)

Base Workers (BASE_WORKER)

External Companies (EXTERNAL_COMPANY)

Emergency Plans (EMERGENCY_PLAN)

Courses (COURSE)

Activities (ACTIVITY)

Key Technologies
Java 8+ - Core application language

Java Swing - GUI framework

MySQL - Relational database

JDBC - Database connectivity

CardLayout - Dynamic UI navigation

# 🎯 Key Features

1. User-Friendly Interface
Clean, modern GUI with consistent color scheme

Card-based navigation for different modules

Responsive buttons with hover effects

Intuitive search and filter options

2. Database Operations
Connection pooling for efficiency

Prepared statements to prevent SQL injection

Asynchronous queries using SwingWorker

Comprehensive error handling

3. Search Functionality
Dynamic query building based on user input

Multiple criteria filtering

Real-time results display in JTable

Export capabilities for search results


# 🚀 Getting Started

Prerequisites
Java JDK 8 or higher

MySQL Server 5.7+

MySQL Connector/J

Installation
Clone the repository

Import the database schema from database/schema.sql

Update database credentials in Constants.java

Compile and run Main.java

Database Configuration
java
// Constants.java
public static final String DB_URL = "jdbc:mysql://localhost:3306/cmms_db";
public static final String DB_USER = "root";
public static final String DB_PASSWORD = "password";
