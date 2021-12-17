## Backend component of user personality test app

This is a simple Spring Boot application with a H2 in-memory database and a dataset consisting of a 3-questions
introvert/extrovert personality test

## TODOs
- Use a dedicated model for rest requests/responses instead of exposing JPA entities directly
- Custom exceptions in service layers - deal with http error codes in exception mappers only
- Better test coverage for calculation of personality scores