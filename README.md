##Vote Counter Exercise built with Node.js with tests written in Java

**Spec:**

Below 1 second performance.

Tested solution.

Solution should run on a single machine.

No more than 3 votes per user.

Same user can vote multiple times for the same candidate or for different ones.

**Design**

This system is a RESTful API for dealing with user votes. It is meant to be lightweight, fast and scalable.

It includes the following endpoints:

GET /index Renders the homepage of the application

POST /vote/:voterId/:candidate Facilitates voting for a candidate. Increments the vote count for the given candidate and increments the number of votes cast by the given user.

GET /vote/count/:candidate Returns the number of votes for the given candidate.

The following endpoints were used to seed the DB with user and candidate info and for debugging purposes:

GET /votes/cast/:voterId Return the number of votes cast for the given user.

POST /new/candidate/:candidateId Creates a new candidate in the DB.

POST /new/voter/:voterId Creates a new user in the DB.

**Dependencies**

This application uses the Node.js framework, the Express framework and MongoDB as the Database.

The brains of this application, where the requests are handled and the database queries are carried out, are the two following files:

**app.js** - Includes the dependencies, routes and sort function for the notifications.

&

**/models/vote.js** - Includes the DB schema and DB functions for the candidates.

&

**/models/voters.js** - Includes the DB schema and DB functions for the users (referred to as voters).

**IMPORTANT**

Before the RESTful API can be used it is important to seed the DB with candidate and user information to emulate a real-world situation.

To do this cd into the directory where you have cloned the repo. Make sure you are in the folder that includes the app.js file. 
Now run:
	- mongod

		and in another terminal tab/window run

	- nodemon

Now you can seed the DB by running the Java file InitializeDB.java

By running CountMeUpTester.java the server is hit with 300 concurrent http requests distributed across 100 threads.


**Application Performance**

The file PerformanceTest.java tests the speed of the system to see what speed the server can return the candidate vote counts.

When run this file consistently outputs something similar to the following screenshot with a time of 205502494 nanoseconds or 0.2 seconds, well under the required time of 1 second.
This can be attributed to the lightweight and async nature of Javascript and Node.js.


**Additional Notes**

CountMeUp10MTest.java attempts more threads, users and votes but due to time constraints some bugs remain.