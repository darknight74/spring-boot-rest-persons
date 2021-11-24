# Demo project: Persons API

### Description

The project aim to build a selfcontained docker application exposing a REST CRUD API to manage/retrieve "persons".
Below the requirements

The app should support:
* In-memory user-based auth(create 2 test users/roles “admin” and “guest”).
* JPA/Hibernate with in-memory H2
* Standard REST CRUD for the entity (with pagination, only admin can access).
* Extra endpoint that returns a list of users that can be filtered by name and age (admin and guest can access). Username and password should not be returned on this endpoint.
* Customised API error response.

Bonus: choose 1 of the following tasks:
1. Integrate a simple hibernate search on the entity and expose it with a search endpoint
2. Create an optimised docker image
3. Expose the standard actuators and create a custom one showing the number of persons in the table.

#### Implementation details

Users "admin" (password `secret2`) and "guest" (password `secret`) are created in the SecurityConfiguration that enables Basic Authentication to access the main API.

The api for the admin user is exposed under `/api/admin/persons` path, while standard user can only access `/api/persons`.

The application interacts with the database using Spring repositories.

A ControllerAdvice is used to control the output format if any unexpected exception occurs.

Spring actuators are enabled to expose the standard `health` endpoint and the custom `persons` - that returns a json with the count of persons in the DB.

A docker image is created as a default task - using a docker gradle plugin) of the gradle build (simply invoke `./gradlew`) and runs the packaged application.