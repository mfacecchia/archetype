# Archetype

Simple yet useful Spring Boot Maven archetype

## Table of Contents

- [1.0 Brief Description](#10-brief-description)
  - [1.1 Details](#11-details)
- [2.0 Technologies](#20-technologies)
- [3.0 Features](#30-features)
- [4.0 How to use it](#40-how-to-use-it)
  - [4.1 Update settings.xml](#41-update-settingsxml)
  - [4.2 Create your project](#42-create-your-project)

## 1.0 Brief Description

One of the most tedious parts of starting a project from scratch is the initial
configuration; building the initial structure, creating the configuration
classes, connecting it to a database, potentially setting up security,
connection to an external IDP, and so on…
For this reason, I wanted to build a project which would give a
small productivity boost for such cases, and, since my main
backend stack is Java + Maven + Spring Boot for almost any project,
the feature Maven offered to create reusable archetypes seemed like
a great way of learning. This project contains all the most basic
features a backend developer would be worried about writing on its own,
from abstract service layer classes with all the most basic CRUD operations,
validation, abstract classes for an easier entities creation by leveraging
Spring Data JPA features, basic security configuration (with option
for connecting it with an IDP such as Keycloak), database connection
properties, logging, and more! By just writing a simple command on the CLI
of your preference, you’ll be able to create a project full of
all these features without any kind of stress!
If you have time, you might find interest in reading the full document,
otherwise, you can directly skip to [4.0 How to use it](#40-how-to-use-it)
to find all instructions related to building your new, shiny project.

### 1.1 Details

This project was built following my personal [Style guides](https://github.com/mfacecchia/code-styleguide)
you may want to follow to keep the whole codebase styling consistent.

## 2.0 Technologies

![Java 21](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Maven 3.9.9](https://img.shields.io/badge/apache_maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![Spring Boot 3.5.7](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)

## 3.0 Features

This project contains most of the features a developer would want to have in a
mid-level/advanced Spring Boot project. The pre-configured features
featured in the project are:

- Logging
- Service layer abstraction for painless CRUD operations configuration
- Base classes for auditing, and Spring Data Entities
- Base DTO classes for either request and response
- Basic Spring Security configuration
- Abstract mapping classes for all entities
- Global Exceptions handler
- Custom App exceptions for the most basic operations
- Base abstract specification builder for most complex queries
- Pre-configured user MVC implementation (which you can take
  inspiration from to build your other entities)

## 4.0 How to use it

Building your new project is as easy as drinking a cup of coffee!

### 4.1 Update settings.xml

First, you will need to configure your Maven installation to actually
find the GitHub Package repository. To achieve this, head over to
your `settings.xml` configuration, or create it if it doesn't exist (this
is usually located under `~/.m2/settings.xml` on Linux/MacOS
and under `C:\Users\{yourName}\.m2\settings.xml` on Windows), and
add the following properties

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <localRepository>${user.home}/.m2/repository</localRepository>

  <profiles>
    <profile>
      <id>github</id>
      <repositories>
        <repository>
          <id>archetype</id>
          <name>Feis Maven Spring Archetype</name>
          <url>https://maven.pkg.github.com/mfacecchia/archetype</url>
          <releases>
            <enabled>true</enabled>
            <checksumPolicy>fail</checksumPolicy>
          </releases>
        </repository>
      </repositories>
    </profile>
  </profiles>

  <servers>
    <server>
      <id>github</id>
      <!-- Write here your GitHub username -->
      <username>yourGHUsername</username>
      <!--  Your Personal Access Token goes here -->
      <password>authToken</password>
    </server>
  </servers>

</settings>
```

**NOTE:** You  will need to get a Personal Access Token
with the `read:packages` scope from GitHub to be able to retrieve
the package, otherwise, GitHub will return a `401 Unauthorized`
while fetching the package.
You can generate a Personal Access Token at [this link](https://github.com/settings/tokens/new)
and paste it in the `<password></password>` property.

### 4.2 Create your project

Now, you will only need to generate your project by
running the following script and you'll be good to go!

```bash
mvn archetype:generate \
  -DarchetypeGroupId=com.feis \
  -DarchetypeArtifactId=archetype \
  -DarchetypeVersion=1.0.1 \
  -Pgithub
```

**NOTE:** You may be prompted to input some information about your
new project, such as the GroupId and the ArtifactId.
