# Zoo Management System

A management system of this type can be
used by businesses to keep track of the
amount of animals their zoos have, the
amount of enclosures, workers, and so on. It
also enables easy addition and removal of
data, like animals, easy listing of useful data,
like the animals and what enclosures they’re
in, and easy searching, to see details about a
particular animal. All of this can be served on
a privilege basis, i.e., not everyone who uses
the system may be allowed to do the same
operation.

# Features

- Adding and Deleting animals, zookeepers and enclosures
- Multithreaded searching by species
- Listing animals, zookeepers and enclosures
- Data persistence with a database
- Validating user input both in the frontend, and the backend
- Access levels: guests (can see animals and enclosures but not who works at the zoo), zookeepers (just like guests, but can see only their zookeeper information), and the admin (can do everything)
- Authentication & authorisation

# Stack

- JDBC for the database connection
- XAMPP to manage the MySQL server
- JavaFX for the GUI
