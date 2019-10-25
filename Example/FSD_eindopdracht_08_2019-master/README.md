# DonDiablo

**_Please note_**  
Database, pom.xml and db.properties files have been removed from this public repository due to security reasons.

**Contents of repository**  
This repository contains the webapplication made during the first [NOVI Bootcamp](https://www.novi.nl/full-stack-developer/) for Full Stack Developers, which started in May 2019.  
It is a demo drop application for DJ Don Diablo. Features are listed in the project description.

**License**  
Everything in this repository can be used for education and testing purposes only. No code can be used in production, commercial or even graded assignments without contacting the author.

## Project description

**_Please note_**  
Unless explicitly mentioned all database connections are made with MariaDB or MySQL. Check *connection URL* and/or *pom.xml files* for details on connection and loaded dependencies.  
All database connections are made non persistent, meaning they are closed after each SQL query.

### database
MariaDB SQL and data export for the Don Diablo Project.  
The data export contains some test data, including hashed passwords. If you want to use these accounts you will have to generate a new Argon2 hash.  
Includes a Visual Paradigm class diagram for this database.
