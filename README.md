# ASE Project

We're gonna use only one class for database connections and all the queries. Not two classes like right now. 

I am moving all database related code to DBQuery file. It should not have main function. 

Only the main class should have the main function. We will create an object of DBQuery in main class and call functions on it to create, update, read, delete from database table.