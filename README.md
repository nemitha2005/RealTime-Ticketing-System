# RealTime-Ticketing-System producer-consumer pattern simulator CLI + FullStack Application


# CLI Application Using Core JAVA
## Overview

This project is a real-time event ticket management system. It uses multithreading to simulate vendors adding tickets and customers buying tickets in real time, ensuring synchronization and effectively handling race conditions.

[![Program Running](https://i.postimg.cc/sfJLbRPV/Screenshot-2024-12-12-003018.png)](https://postimg.cc/jCLvJmk9)

---

## Prerequisites

Ensure you have the following installed:
- **Java Development Kit (JDK)** 8 or higher
- A compatible **IDE** or terminal for running Java programs

---

## How to Run

1. Clone this repository:
   ```bash
   git clone https://github.com/nemitha2005/RealTime-Ticketing-System.git

2. Navigate to the **Ticketing System CLI** folder:
   - Open the project in **IntelliJ IDEA** or your preferred IDE.

3. Run the program:
   - Locate the `Main.java` file in the project structure.
   - Right-click on `Main.java` and select **Run Main** to start the program.

4. Follow the on-screen menu:
   - **Option 1**: Enter configuration parameters (e.g., total tickets, rates).
   - **Option 2**: Start the ticket system to see vendors and customers in action.
   - **Option 3**: Exit the program.

  
# Full-Stack Ticket Simulation System (React + Springboot)
## Overview

This is a **full-stack ticket simulation system** developed using **React** for the frontend and **Spring Boot** for the backend. The system demonstrates real-time ticket management simulation with multithreading and synchronization.

[![System UI](https://i.postimg.cc/2jwzGMVW/Screenshot-2024-12-12-010649.png)](https://postimg.cc/bdZcN60Y)

---

## Prerequisites

Before running the application, ensure you have:
- **Java Development Kit (JDK)** 8 or higher
- **Node.js** (v14 or higher)
- **Maven** (for building the Spring Boot backend)

---

## How to Run

### Step 1: Clone the Repository
```bash
git clone https://github.com/nemitha2005/RealTime-Ticketing-System.git
```
### Step 2: Backend Setup (Spring Boot)
1. Navigate to the **system-backend** folder:
   ```bash
   cd system-backend
   ```
2. Open the folder in your preferred **IDE** (e.g., IntelliJ IDEA).

3. Run the Spring Boot application:
   - Locate the `SystemBackendApplication` file in the `com.nemitha.systembackend.service` package.
   - Right-click and select **Run SystemBackendApplication**.

4. The backend server will start running at `http://localhost:8080`.

### Step 3: Frontend Setup (React)
1. Navigate to the **system-frontend** folder:
   ```bash
   cd system-frontend

2. Install dependencies:
   ```bash
   npm install

3. Run the React application:
   ```bash
   npm start

4. The frontend server will start running at `http://localhost:3000` and connect to the backend server.

## Program Workflow

### Backend (Spring Boot):
- The Spring Boot backend manages the ticket pool, vendors, and customers.
- It provides REST APIs for communication with the frontend.

### Frontend (React):
- The React UI allows users to configure the ticket simulation system.
- Key functionalities include:
  - Setting the number of tickets, release rates, retrieval rates, and pool capacity.
  - Starting and stopping the simulation.
  - Viewing real-time logs for system operations.

---

## System UI

The system UI provides the following features:
- A control panel for configuring and starting the simulation.
- Logs displaying real-time ticket operations (e.g., tickets being added or retrieved).
- A responsive design for seamless user interaction.

### You can test the live hosted version of the application [here](https://test-frontend-rose.vercel.app/) without running it locally.
