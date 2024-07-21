# daraja_b2c_wrapper
A wrapper around the MPESA B2C API

## Running locally
Required tools: 
- Java Version - 17
- Docker
- A local/docker installation of MariaDB
- Daraja Account, with an app that has the Lipa Na MPESA type
- Daraja Sandbox Certificate
- 
Env Variables
- MARIADB_URL if the MariabDB is not running on port 3306
- MARIADB_PASSWORD
- QUEUE_TIMEOUT_URL, RESULT_URL - with base urls pointing to this application
- INITIATOR_NAME - from the B2C API on the Daraja portal
- SECURITY_CREDENTIALS - from the Certificate and the Initiator password
- B2C_MPESA_CONSUMER_SECRET, B2C_MPESA_CONSUMER_KEY - the credentials of the app on Daraja
Steps:
1. From the project root, cd into the docker folder 
    `cd docker`
2. Start the Kafka Broker and the Zookeeper containers: `docker-compose up -d`
3. Create the database `darajab2c` on your preferred MariaDB instance
4. Start the application

