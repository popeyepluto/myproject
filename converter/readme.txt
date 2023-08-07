The project is running on Java 17, you can update the pom configuration to change the Java version to fit your local dev environment.
How to test it?
If the postman is installed on your local, you can import the postman script file in the project folder.
If you do not have postman, you can aslo use other restapi testing tool, like chrome extensions "Advanced REST client".
You can simplfy the testing as well,just need to update the controller annotation to @GetMapping, you would not need to input any paramters.
The api url is localhost:8081/api/convert.
Thanks.


