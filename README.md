# UClothes BE Application

This is the UClothes application. Below is an example of the `.env` file needed for the application to run correctly.

# Example .env file to connect to the database

```plaintext
# .env
DB_USERNAME=yourUsername
DB_PASSWORD=yourPassword
DB_URL=jdbc:mysql://localhost:3306/example
JWT_SECRET=exampleJWTSECRET
```
# To run application
```plaintext
1. Create database in MySQL
2. Launch server in MySQL
3. Create .env file with the following information
4. You can start application with SHIFT + F10 (if its not starting go to UClothesApplication file and hit CTRL + SHIFT + F10)
5. To run tests use Maven LifeCycle -> test
```
# Endpoints
```
Admin:
POST /admin/add
To add an offer example FormData:
[![img.png](screenshots%2Fimg.png)](https://github.com/Brylson123/Java-uClothes-backend/blob/main/screenshots/img.png)

DELETE /admin/delete/{id}
To delete an offer, replace `{id}` with the ID present in the database.

PUT /admin/update/{id}
You can update an offer with FormData

Offer:
GET /
Return all JSON with all offers in the database

GET /{id}
Return offer with the specified ID

GET /category/{category}
Return all offers in the specified category

POST /image/{id}
To upload an image

GET /image/{imageName}
Return image with the specified `imageName` in the database

User:

GET user/getUserByToken/{token}
Return user with the specified token

POST user/register
Register a user with the following JSON:
{
    "username": "example",
    "role": "admin",
    "password": "example"
}

POST user/login
Log in a user with the following JSON:
{
    "username": "example",
    "password": "example"
}

GET user/logout
Log out the currently logged-in user
