# AIS data gateway distributor

Spring Boot application that connects to WebSocket to read stream (IEC 61162 specification).

Publishes a several WebSockets for reading the received data stream in the various formats. The radio message part (ITU-R M.1371-5 recommendation) is for example converted to 8-bit text or GeoJSON. For the public WebSockets (without technical authentication mechanism in the application) the data is also filtered and the part of the data is masked. 

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

- Java is installed
- Maven is installed
- Git client is installed

There is a WebSocket service available (e.g. AIS data gateway connector) for reading the input stream.

### Compiling and running

1. clone the repository
2. go to root folder
3. build the project with Maven (e.g. "mvn clean install")
4. create additional properties file e.g. credentials.yml with syntax:
   ```
   ais:
     distributor:
       - username: ****
         password: ****
         allowed-endpoints:
           - url: "/raw-data"
           - url: "/parsed-data"
   ```
5. run application with command:
   ```
   java -jar target\ais-distributor-[version].jar --spring.profiles.active=local --user=****  --passwd=**** --address=[web_socket_address] --port=[port] --spring.config.additional-location=file:[credentials.yml]
   ```
   
WebSockets are published to port 8081 that is configured in the application.yml.