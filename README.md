# Perry's Summer Vacation Goods and Services 

## Description

We are making a private messaging service for our new company Perry’s Summer Vacation Goods and Services. 

## Goal
We need to design, and create, a scalable API to be able to handle the many messages this company is going to handle.

## Requirements 

### Functional requirements

* The application must be able to create users.
* The application must allow users to send a message to another user.
* The application must allow editing and deleting messages.
* The application must allow a user to "like" a message.
* The application must be able to get a list of other users that have sent or received messages to/from a specified user.
* The application must be able to get all the messages sent between two users.

### Technical requirements

* Use what language you are comfortable with, but Typescript, Javascript, Java preferred.
* The source code must be shared in a public repository (Github, Bitbucket, etc).
* The system should be able to scale to 200 requests per second efficiently.
* The application should be ready to run in the cloud or in a container (You can use any technology available in AWS).
* The application data must be persisted in a database of some type.

### Constraint(s)

* Although not explicitly a constraint, we have to stick to one of Java or JavaScript.
* We have to complete this assignment in no more than eight hours. 

## Q&A

Q. Will this be a one-to-one, group or both chat app? 
A. One-to-one, no group chat.

Q. Are we targeting mobile or web as a first class citizen for this application?
A. This is an API only, no UI. It should fit either use case.

Q. Does a user have to see all messages immediately, or can there be a delay? (thinking websockets vs. long polling)
A. No UI, you can just have an API to retrieve messages.

Q. Given this is a private messaging system, do we require roles and privileges for messages, groups, etc?
A. No, not necessary.

Q. Like Twitter, should we have a maximum message size, or can it be an unlimited message size? (thinking SQL vs. key-value stores)
A. I leave that up to you. If you have a good reason for a message size limit then yes that makes sense. Good kind of thing to think about.

Q. Has load, stress and/or performance testing been factored into the original 8 hour estimate?
A. Only what unit/integration testing you find necessary to build it. We will not judge for lack of test coverage.

Q. Should we supply load and/or performance tests to prove it scales "efficiently"?
A. Not necessary to do anything beyond the tests you use for development.


## Assumptions

* We'll assume the reference to "API" is with reference to a Restful API, i.e. create a scalable Restful API.
* We'll also consider this application will run as part of a cluster, so be wary of state within the application.

## Discovery

Given the questions and answers above, along with the assumptions, we can safely say we're looking at a Restful API.

The Restful API must be complete with a selection of CRUD operations for users and messages. These messages will be 
searchable, but given the time limit, we will not be implementing such functionality for user. 

Like most applications, we'll make use of pagination for a more seamless and performant API when it comes to searching
messages between users and the like.

There is some business logic required in the form of liking a message(s), which going by the above Q&A, will only be 
done on a message between two users. 

Although JavaScript is probably more suited to such a use case, I will opt to use Java as I'm far more comfortable with 
the ecosystem that surrounds.

Messages, as described in the Q&A above, can be of any varying length. Therefore, it would perhaps be more suited to use a 
NoSQL database to store this unstructured data, such as MongoDB. However, the User is structured in that we will require 
perhaps a forename, surname and email, suited more so to a SQL database, such as MySQL. Therefore, the solution here will 
be to make use of a NoSQL and SQL solution, such as those already mentioned.

## Scope

## The musts ... 

* The application must be able to create a user.
* The application must be able to create/update/delete a message.
* The application must be able to send messages between two users.
* The application must be able to like a message.
* The application must be able to get messages based on a query.
* The application must be able to handle at least 200 requests/second.
* The application must be able to run in the cloud or a container.
* The application must have unit tests to ensure code quality.
* The application must have integration tests to ensure code quality.

<b>Note</b>: The message query above should allow for querying between two users explicitly, as well as one user.

## The shoulds ... 

* The application should authenticate access to messages on a user basis.

## The will nots ... 

* The application will not facilitate group messages in any shape or form.
* The application will not facilitate searching on users in any shape or form.
* There will not be any load testing completed, as already mentioned above.
* There will not be any stress testing completed, as already mentioned above.
* There will not be any performance testing completed, as already mentioned above.

## Design

### Model(s)

Below illustrates our `User` model, simplified for our application.

```
User {
	id:string
	username:string
}
```

We have encapsulated the message below as an object.

```
Message {
	id:string
	sender:string
	recipient:string
	body:string
	likes:integer
}
```

We will have two types of `MessageQuery`, as described below.

```
SenderRecipientMessageQuery {
	sender:optional<string>
	recipient:optional<string>
}
```

The above will query based on both sender and recipient, and can be exclusive.

### System Interface(s)

Below are the basic endpoints which will look to implement.

`POST /v1/users` :- Enable the creation of a user.
`POST /v1/messages` :- Facilitate a means to create a message.
`PUT /v1/messages/{id}` :- To enable updates against a message.
`DELETE /v1/messages/{id}` :- For message deletion, if needed.
`GET /v1/messages` :- Allow execution of queries for messages.
`POST /v1/messages/{id}/likes` :- Provides a means to like a message. 

### High-Level

<b>TODO</b>

## Getting Started

<b>TODO</b>

