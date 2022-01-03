Prerequisites:
Maven is installed: sudo apt install maven
NodeJS and npm is installed (required for Maildev): 
	sudo git clone https://github.com/maildev/maildev.git
	sudo apt update
	sudo apt install nodejs npm
	
Maildev is installed and running:
	sudo npm install -g maildev
	maildev
	
Select OS in programme
	go to the Globals Class and set your OS in the String variable

Run *having issues documenting to run with maven. will be updated soon
	as a workaround the IDE can be used

	Install lombok or replace


Javadoc - classes, packages and public functions. Author, date of inception, copyright

Domain package: User, BoUser and admin are all a type of users. Use inheritage or interface to use this advantage in for instance the AuditorAwareImpl
do not use e.printStackTrace(); is logged directly std.err and not controlled by logger

why did you include the mvnw cmds?

unit tests are empty?
	
-----------------------------------------------------------------------

To help grasp the application visit these classes in order:
1. DatabaseFiller
2. pom.xml
3. AuthController
4. UserService
5. UserRepository

Then the html files (which use thymeleaf notation)
1. /resources/templates/login.html
2. /resources/templates/dashboard.html

