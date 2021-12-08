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
	
-----------------------------------------------------------------------

To help grasp the application visit these classes in order:
1. DatabaseFiller
2. pom.xml
3. AuthController
4. UserService
5. UserRepository

Then the html files (which use thymeleaf expressions)
1. /resources/templates/login.html
2. /resources/templates/dashboard.html

