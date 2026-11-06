# Vacanza - Hotel Room Reservation Management System

![Java](https://img.shields.io/badge/Java-25-orange)
![Storage](https://img.shields.io/badge/Storage-Text_Files-blue)
![NetBeans](https://img.shields.io/badge/NetBeans-IDE-green)

A **desktop-based hotel management system** built with **Java Swing** that allows hotel staff and administrators to manage rooms, guests, reservations, and billing from one place. All data is saved to plain text files, so no database setup is needed.

---

## Features

- Role-based access control (Admin and Staff)
- Room management (Standard, Deluxe, Suite)
- Guest records with New / Returning tagging
- Reservation booking and status tracking
- Automatic 10% returning guest discount
- Billing, payments, and receipts
- User account management with lockout after 3 failed logins

---

## Technologies Used

- Java Swing
- Plain text (`.txt`) file storage
- Apache NetBeans
- Apache Ant

---

## Installation

1. Clone the repository
```bash
git clone https://github.com/yourusername/Vacanza.git
```

2. Make sure the data files are in the project root folder
   - `rooms.txt`, `guests.txt`, `reservations.txt`, `payments.txt`, `users.txt`

3. Open the project in NetBeans
   - Go to **File > Open Project** and select the project folder
   - The project uses **JDK 25**. If you use another version, change `javac.source` and `javac.target` in `nbproject/project.properties`

4. Build and run the project
   - The main class is `vacanza.Main`
   - Run it from the project root so the app can find the `.txt` files

5. Log in with a default account

| Role  | Username | Password   |
|-------|----------|------------|
| Admin | `admin`  | `admin123` |
| Staff | `staff`  | `staff123` |

   - Change these passwords before using the system for real
   - An Admin can unblock a locked account in the Users screen
