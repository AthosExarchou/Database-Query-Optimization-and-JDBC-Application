# Database Query Optimization & JDBC Application

Developed as a project for the **Database Design** course at [Harokopio University of Athens – Dept. of Informatics and Telematics](https://www.dit.hua.gr), this project focuses on **query optimization** and the use of **indexes** to improve database performance.
Additionally, included is a Java application built with **JDBC (Java Database Connectivity)** to connect to a database and execute queries.

---

## Key Objectives
- **Understanding Query Optimization:** Analyze the execution plan of a query to understand how the database processes data.
- **Impact of Indexes:** Evaluate the benefits of creating indexes on specific fields by comparing execution plans before and after their creation.
- **Practical JDBC Experience:** Build a Java program that connects to an Oracle database, runs SQL queries, and displays the results.

---

## Technologies Used
- **Programming Language:** Java
- **Database:** Oracle (using a database schema for a company with `employee` and `department` tables)
- **Connectivity:** JDBC (using the `OracleDriver`)

---

## Project Structure
This repository contains all the necessary files for the project, including the code, documentation, and configuration files.
- `README.md`: This file provides an overview of the project, its objectives, and instructions on how to run the application.
- `DBApplication.java`: Contains all the application logic, including methods to connect to the database, execute a query, create and delete an index, and print the execution plan.
- `report_el.pdf`: A PDF document (in greek) containing a detailed analysis of the theoretical questions from the assignment, including B+ tree calculations and console outputs from the Java application.
- `LICENSE`: The MIT License file, defining the permissions and limitations for using the code.
- `.gitignore`:  file that specifies which files and directories should be ignored by Git, ensuring that sensitive information (like credentials) is not committed to the repository.

---

## How to Run

1. **Set up the Database:** Ensure you have a database schema with the `employee` and `department` tables.
2. **Configure Connection:** Create a file named `db.properties` in the same directory as `DBApplication.java`. Add your database connection details to this file:

```properties
db.url=<YOUR_DB_URL>
db.user=<YOUR_USERNAME>
db.password=<YOUR_PASSWORD>
```

3. **JDBC Driver:** Make sure you have the appropriate Oracle JDBC driver **.jar** file in your project's classpath.
4. **Execute:** Run the `DBApplication.java` file. The application will perform the following steps:

- Connect to the database.
- Delete the index (if it exists) to start from a clean state.
- Print the list of employees.
- Print the initial execution plan.
- Create the `index_employee_dno`.
- Print the new execution plan to show the performance improvement.

---

## Analysis of the Code
The provided Java code, `DBApplication.java`, effectively demonstrates the concepts outlined in the project. Here is a breakdown of its key functions and their relevance:
- `getConnection()`: This method is a robust way to handle database connectivity.
It uses a `db.properties` file to securely store and retrieve database credentials, preventing them from being hard-coded directly into the source file.
It also loads the Oracle JDBC driver dynamically and establishes a connection using `DriverManager.getConnection()`.
- `fetchAllEmployeeDepartmentPrepared()`: This method uses a `PreparedStatement` to execute the main query that joins the `employee` and `department` tables.
Using `PreparedStatement` is a best practice as it is more efficient for repeated executions and helps prevent SQL injection attacks.
The code then iterates through the `ResultSet` to print the required data.
- `createExplainPlan()`: This function is central to the project's goal of understanding query optimization.
It executes the `EXPLAIN PLAN FOR` command for the main query and then retrieves and prints the plan using `DBMS_XPLAN.DISPLAY`.
This allows you to visually see and analyze the steps the database's optimizer takes to execute the query, providing insight into its efficiency.
- `createIndex()` **and** `deleteIndex()`: These methods demonstrate how to programmatically manage database objects like indexes using JDBC.
By creating an index on the `dno` column of the `EMPLOYEE` table and then re-running the `createExplainPlan()` method, the code proves that the query optimizer will choose a more efficient execution path (e.g., an index scan) due to the new index.
The `deleteIndex()` method is a useful utility to reset the database state before each run.

---

## B+ Tree Calculations
- **Blocking Factor & Block Usage:** The blocking factor is calculated as the block size divided by the average record size, resulting in **2 records per block**.
This leads to **224 bytes of wasted space** per block due to unspanned record allocation. To store all **50,000 records, 25,000 blocks** are required.
- **Unindexed Search Cost:** For an unindexed file, a simple equality search has a worst-case cost of reading all **25,000 blocks**, which is **1250 ms**.
A range search on an unsorted file also requires a full scan in the worst case.
- **B+ Tree Structure:** The calculations for the B+ tree structure are based on the block size of 1024 bytes and the given key/pointer sizes.
  - **Leaves:** Each leaf node can hold **32 entries**, leading to a fanout of 33. This means **1563** blocks are needed for the leaf level.
  - **Internal Nodes:** Internal nodes are assumed to be 2/3 full.
  The calculations show that the tree has a total of **4 levels**: 1 root node, 5 nodes in the third level, 87 nodes in the second level, and 1563 nodes in the leaf level.
- **Index Size:** The total size of the index is calculated by summing the blocks at each level, resulting in a total of **1652 blocks**.
- **Indexed Search Cost:** For a range search on the B+ tree, the cost involves traversing the tree to find the starting key and then reading the relevant leaf blocks.
The calculation shows a traversal cost of 4 blocks plus 2 leaf blocks, for a total of **0.3 ms**, which is a significant improvement over the unindexed search.

---

## Areas For Improvement
- Add more queries to test different scenarios.
- Extend the application to support other database systems (e.g., MySQL, PostgreSQL).
- Implement a separate class or method for handling database connection and closing resources to follow the **Single Responsibility Principle**.

---

## Author

- **Name**: Exarchou Athos
- **Student ID**: it2022134
- **Email**: it2022134@hua.gr, athosexarhou@gmail.com

## License
This project is licensed under the MIT License.
