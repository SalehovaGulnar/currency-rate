**currency-rate**
This service is designed to obtain daily exchange rates from the Central Bank

 **Authors**
- ghsalehova (https://github.com/SalehovaGulnar/) 

**Requirements**
The fully fledged server uses the following:
• Spring Framework
• SpringBoot

**Building the project**
You will need:
• Java JDK 
• Gradle
• Git

Use Gradle to build the server ./gradlew assemble

**Running the application locally**
There are several ways to run a Spring Boot application on your local machine. 
One way is to execute the CurrencyRateApplication method in the com.umbrella.currencyRate.controller class from your IDE
Another way is to open cmd and enter the project folder with the cd command. Then java -jar currency-rate-0.0.1.jar
Alternatively you can use the Spring Boot Gradle task like so: ./gradlew bootRun

**Browser URL**
Open your browser at the following URL for Swagger UI (giving REST interface details):
http://localhost:8080/swagger-ui.html

