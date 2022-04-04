# Turmas

Distributed Systems Project 2021/2022

## Authors

**Group T14**

### Code Identification

In all source files (namely in the *groupId*s of the POMs), replace __GXX__ with your group identifier. The group
identifier consists of a G and the group number - always two digits. This change is important for code dependency
management, to ensure your code runs using the correct components and not someone else's.

### Team Members


| Number | Name              | User                             | Email                               |
|--------|-------------------|----------------------------------|-------------------------------------|
| 85171  | Ruben Silva       | <https://github.com/rbssilva>    | <ruben.silva@tecnico.ulisboa.pt>    |
| 93587  | Joao Pereira      | <https://github.com/joaoecsde>   | <joao.h.pereira@tecnico.ulisboa.pt> |
| 93592  | Joao Pargana      | <https://github.com/JPPPPPPPPP>  | <joao.pargana@tecnico.ulisboa.pt>   |

## Getting Started

The overall system is made up of several modules. The main server is the _ClassServer_. The clients are the _Student_,
the _Professor_ and the _Admin_. The definition of messages and services is in the _Contract_. The future naming server
is the _NamingServer_.

See the [Project Statement](https://github.com/tecnico-distsys/Turmas) or a complete domain and system description.

### Prerequisites

The Project is configured with Java 17 (which is only compatible with Maven >= 3.8), but if you want to use Java 11 you
can too, just downgrade the version in the POMs.

To confirm that you have them installed and which versions they are, run in the terminal:

```s
javac -version
mvn -version
```

### Installation

To run this project, make sure you have at least java 11.

To compile and install all modules:

```s
mvn clean install
```
---
To run ClassServer server:

```s
mvn compile exec:java
```
---

To run Professor and Admin client:

```s
mvn compile exec:java
```
---

To run Student client:
```s
mvn compile exec:java -Dexec.args="<alunoXXXX> <name>"
```
XXXX is an Integer.

Name is a String between 3 and 30 characters.

---
To run debug mode instead of starting the server normally start like this:
```s
mvn compile exec:java -Dexec.args="-debug"
```

---
Commands for Admin:
```s
dump
```
**dump** command returns the state of the server.

---
Commands for Professor:
```s
openEnrollments <number>
closeEnrollments
list
cancelEnrollment <alunoXXXX>
```
**openEnrollments** command receives an Integer that will refer to the size of the class and makes enrollments possible.

**closeEnrollments** command closes the possibility of students enrolling in the class.

**list** command return the state of the class.

**cancelEnrollment** command receives an Integer that will refer to a studentId, and will move him from the list of enrolled students to the discarded list.

---
Commands for Student:
```s
enroll
list
```
**enroll** command tries to enroll the student.

**list** command return the state of the class.

---
## Built With

* [Maven](https://maven.apache.org/) - Build and dependency management tool;
* [gRPC](https://grpc.io/) - RPC framework.
