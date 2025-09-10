# GarageTestBot
Test telegram bot on java for collecting, analyzing, and processing reviews
# Documentation
**Admin panel url: `http://localhost:8084/admin/feedbacks`**

## Configuration

### Google documents
First, you need to configure everything and add secret tokens to the application.properties file located in the /resources folder

Tutorial how to create google service account and download credential.json file: https://www.youtube.com/watch?v=_FmsEkF72M0. When creating a Google service account, don't forget to assign it the **Editor** role.

**ATTENTION** You need to obtain the email address of your Google service account, and in the Google document, you need to grant editing permissions for this email address.

### Trello cards
Guide to get API Keys and Tokens for Trello: https://www.youtube.com/watch?v=ndLSAD3StH8

Guide to get trello id list: https://stackoverflow.com/questions/26552278/trello-api-getting-boards-lists-cards-information

Example:
```properties
bot.token=123456:abcdef
openai.token=sk-xxxxxx
google.api.credentials_file_path=C:/GarageTestBot/src/main/resources/credential.json
google.api.document_id=ab124sc-i
trello.token=xAAAxxxxx
trello.key=123-xxxxx
trello.idList=4124kixxxx
```

### Database Configuration 
The project uses PostgreSQL

If you have Docker installed on your computer, you can look at the docker-compose file in the /resources folder, get the password and username from it, and substitute these values into the application.properties file

**Navigate into the /resources directory**

And run this command

**On macOS/Linux:** 
```bash
sudo docker compose up -d
```

**On Windows:**
```bash
docker compose up -d
```


## How to Get Started

1.  **Clone the repository and navigate into the project directory.**

2.  **Build and Run the application:**
    The project includes a Maven wrapper, which is the recommended way to build and run it.

    **On macOS/Linux:**
    ```bash
    ./mvnw spring-boot:run
    ```
    **On Windows:**
    ```bash
    mvnw.cmd spring-boot:run
    ```
    The application will start and be accessible at `http://localhost:8084` you can change port in application.properties file.
