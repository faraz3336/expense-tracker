# 💰 My Wallet - Expense Tracking App

<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=json-web-tokens&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)

**A full-stack expense tracking web application for efficient day-to-day financial management**

[Features](#-features) • [Quick Start](#-quick-start) • [Technologies](#-technologies-used) • [Screenshots](#-screenshots) • [Contributing](#-contributing)

</div>

---

## 📝 Description

**My Wallet** is a comprehensive expense tracking application that enables users to efficiently manage their daily finances. Built with modern technologies, it provides secure multi-role authentication and robust financial management features for both regular users and administrators.

## ✨ Features

### 👤 User Features

- 🔐 **Secure Authentication** - Sign-up, sign-in, password reset with email verification
- 📊 **Interactive Dashboard** - Overview of financial status with monthly summaries
- 💳 **Transaction Management** - Add, edit, delete, and categorize expenses/income
- 🎯 **Budget Tracking** - Set and monitor budgets for different categories
- 🔄 **Recurring Transactions** - Track upcoming and recurring payments
- 📈 **Financial Statistics** - Visual insights into spending patterns

### 👑 Admin Features

- 👥 **User Management** - View and manage all registered users
- 🏷️ **Category Management** - Add and edit transaction categories
- 👁️ **Transaction Oversight** - Monitor all user transactions
- 🔍 **Advanced Controls** - Search, filter, and pagination for efficient management

## 🚀 Quick Start

### Prerequisites

Before you begin, ensure you have the following installed:

- Java 17 or higher
- Node.js 16 or higher
- MySQL 8 or higher
- Maven
- Email account (for verification features)

### Installation

#### 1️⃣ Clone the Repository

```bash
git clone https://github.com/faraz3336/Fullstack-Expense-Tracker.git
cd Fullstack-Expense-Tracker
```

#### 2️⃣ Configure Database

Navigate to `backend/src/main/resources/application.properties` and update with your credentials:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL@gmail.com
spring.mail.password=YOUR_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

> **Note:** For Gmail, you'll need to generate an [App Password](https://support.google.com/accounts/answer/185833)

#### 3️⃣ Initialize Backend

```bash
cd backend
mvn spring-boot:run
```

The application will automatically create the necessary database tables.

#### 4️⃣ Seed Initial Data

- Add transaction categories (both expense and income types) to the database
- Create an admin user by inserting into the `users` table with role `ADMIN`

#### 5️⃣ Start Frontend

```bash
cd frontend
npm install
npm start
```

#### 6️⃣ Access the Application

- **Frontend:** http://localhost:3000
- **Backend API:** http://localhost:8080

Create a new account using your email to get started!

## 📁 Project Structure

```
Fullstack-Expense-Tracker/
│
├── backend/                 # Spring Boot Application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/expensetracker/
│   │   │   │   ├── config/        # Security, CORS, and other configurations
│   │   │   │   ├── controller/    # REST API endpoints
│   │   │   │   ├── dto/           # Data Transfer Objects (Request/Response)
│   │   │   │   ├── model/         # Entity classes (User, Transaction, Category, etc.)
│   │   │   │   ├── repository/    # Data access layer (JPA repositories)
│   │   │   │   ├── security/      # JWT authentication and authorization logic
│   │   │   │   ├── service/       # Business logic layer
│   │   │   │   └── util/          # Helper classes and utilities
│   │   │   │
│   │   │   └── resources/
│   │   │       ├── application.properties    # Database, email, JWT configurations
│   │   │       └── static/                   # (Optional) Static resources
│   │   │
│   │   └── test/                   # Unit and integration tests
│   │
│   ├── pom.xml                    # Maven dependencies and build configuration
│   └── target/                    # Compiled application (generated)
│
├── frontend/                # React.js Application
│   ├── public/
│   │   ├── index.html
│   │   └── ...              # Other static assets
│   │
│   ├── src/
│   │   ├── api/                    # Axios setup and API call functions
│   │   ├── assets/                 # Images, icons, fonts
│   │   ├── components/             # Reusable UI components (Buttons, Modals, Cards)
│   │   ├── constants/              # Application-wide constants
│   │   ├── contexts/               # React Context for state management (e.g., Auth)
│   │   ├── pages/                  # Main page components
│   │   │   ├── auth/               # Login, Register, ForgotPassword pages
│   │   │   ├── user/               # User Dashboard, Transactions, Budget, Stats
│   │   │   └── admin/              # Admin panels for User, Category, Transaction management
│   │   ├── styles/                 # CSS or SCSS files
│   │   ├── utils/                  # Helper functions (formatters, validators)
│   │   ├── App.js                  # Main app component with routing
│   │   └── index.js                # Application entry point
│   │
│   ├── package.json                # NPM dependencies and scripts
│   └── README.md                   # Frontend-specific setup instructions
│
├── .gitignore                     # Git ignore rules for both frontend and backend
├── LICENSE                        # MIT License
└── README.md                      # Project overview and main documentation
```

## 📸 Screenshots

### 🎨 Welcome & Authentication

#### Welcome Page
![Welcome Page](IMAGE_URL_1)
*Landing page with financial growth visualization and call-to-action buttons*

#### Login Page
![Login](IMAGE_URL_2)
*Secure login form with email and password fields*

#### Register Page
![Register](IMAGE_URL_3)
*User registration with form validation and terms agreement*

#### Forgot Password
![Forgot Password](IMAGE_URL_4)
*Password recovery through email verification*

---

### 👤 User Dashboard & Features

#### Dashboard Overview
![Dashboard](IMAGE_URL_5)
*Comprehensive dashboard showing income (Rs. 15000), expenses (Rs. 8540), cash in hand (Rs. 6460), and budget tracking with interactive charts*

#### Transaction History
![Transaction History](IMAGE_URL_6)
*Complete list of all transactions with date grouping, search, and filter options*

#### New Transaction
![New Transaction](IMAGE_URL_7)
*Add new expense or income with category selection (Other, Food, Leisure, Household, Clothing, Education, Healthcare)*

#### Edit Transaction
![Edit Transaction](IMAGE_URL_8)
*Edit existing transaction details with delete option*

#### Saved Transactions
![Saved Transactions](IMAGE_URL_9)
*Manage recurring transactions with reminders (Labour fee - 3 days overdue, Electricity - Due on Today, School fee - Monthly)*

#### Statistics & Analytics
![Statistics](IMAGE_URL_10)
*Visual spending insights with line charts showing expense and income trends over months*

#### User Settings
![User Settings](IMAGE_URL_11)
*Profile management with avatar upload and password change functionality*

---

### 👑 Admin Panel

#### Admin Transaction Management
![Admin Transactions](IMAGE_URL_12)
*View all user transactions across the platform with search and pagination (1 to 10 of 47 records)*

#### User Management
![User Management](IMAGE_URL_13)
*Manage all registered users with enable/disable controls, showing total expenses, income, and transaction count*

#### Category Management
![Categories](IMAGE_URL_14)
*Manage expense and income categories (Salary, Food, Leisure, Household, Clothing, Education, Healthcare, Sales, Awards, Interest)*

#### New Category
![New Category](IMAGE_URL_15)
*Add new transaction category with type selection (Expense/Income)*

#### Edit Category
![Edit Category](IMAGE_URL_16)
*Edit existing category details with save and cancel options*

#### Admin Settings
![Admin Settings](IMAGE_URL_17)
*Admin profile settings with avatar management and password security*

---

## 🛠️ Technologies Used

| Technology | Purpose |
|------------|---------|
| **Spring Boot** | Backend REST API framework |
| **Spring Security** | Authentication & Authorization |
| **JWT** | Secure token-based authentication |
| **React.js** | Frontend user interface |
| **MySQL** | Database management |
| **CSS3** | Styling and responsive layout |
| **Axios** | HTTP client for API calls |
| **Spring Mail** | Email verification and notifications |

## 🔒 Security Features

- ✅ **JWT Authentication** - Secure token-based authentication with refresh tokens
- ✅ **Password Encryption** - BCrypt password hashing
- ✅ **Email Verification** - Required for new account activation
- ✅ **Password Reset** - Secure password recovery via email
- ✅ **Role-Based Access** - USER and ADMIN role authorization
- ✅ **CORS Configuration** - Secure cross-origin resource sharing
- ✅ **Input Validation** - Server-side validation for all user inputs

## 📈 Future Enhancements

- [ ] Mobile application using React Native
- [ ] Data export (PDF, Excel, CSV)
- [ ] Advanced analytics and AI-powered insights
- [ ] Integration with banking APIs
- [ ] Multi-language support
- [ ] Dark mode theme
- [ ] Push notifications
- [ ] Financial goal tracking and recommendations

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. **Fork the repository**

2. **Create a feature branch**
   ```bash
   git checkout -b feature/AmazingFeature
   ```

3. **Commit your changes**
   ```bash
   git commit -m 'Add some AmazingFeature'
   ```

4. **Push to the branch**
   ```bash
   git push origin feature/AmazingFeature
   ```

5. **Open a Pull Request**

## 👤 Author

**Faraz**

- GitHub: [@faraz3336](https://github.com/faraz3336)
- Project Link: [Fullstack-Expense-Tracker](https://github.com/faraz3336/Fullstack-Expense-Tracker)

## 🙏 Acknowledgments

- Spring Boot community and documentation
- React.js team and comprehensive resources
- All contributors and testers
- Open source community for inspiration and support

---

<div align="center">

**If you found this project helpful, please consider giving it a ⭐️**

Made with ❤️ by [Faraz](https://github.com/faraz3336)

</div>
