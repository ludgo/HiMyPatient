# MTAA backend
Sails.js backend for Mobile Technologies and Applications at FIIT STU BA

## Prerequisites
Docker + Docker compose

Works on Windows 10 Pro, OS X and many Linux distributions
https://docs.docker.com/install/

For other Windows versions check Docker Toolbox and pray :) 
https://docs.docker.com/toolbox/toolbox_install_windows/

## Docker

### Installation
Clone source codes from git and run

### Running
Open terminal and switch to directory, where you have `docker-compose.yml`

`docker-compose up`

### Stopping
`ctrl+c` or `docker-compose down`

## Using without Docker

### Installation
Install Node.js 8 LTS

Install MySQL-compatible database

### Database connection
Add new file `app/config/local.js` with following code:
```
module.exports = {
  models: {
    connection: 'localMysqlDb'
  }
};
```

### Running
```
cd app/
npm install
node start
```

## App

### URL
http://localhost:8080/

### Available connection methods
HTTP and WebSocket (Socket.io)

### API endpoints
#### `GET /:model`

Get all items, e.g. `/items`

#### `GET /:model/:id`

Get specific item, e.g. `/items/123`

#### `POST /:model`

Create new request, e.g. `/items`

Data must be in request body

#### `PUT /:model/:id`

Update a request, e.g. `/items/123`

Data must be in request body

#### `DELETE /:model/:id`

Delete a request, e.g. `/items/123`

#### Editing endpoints

Can be edited in file `app/config/routes.js`

https://sailsjs.com/documentation/concepts/routes

#### What are those GET/POST/PUT/DELETE methods?

http://www.restapitutorial.com/lessons/httpmethods.html

### Models
You can edit data models in `app/api/models` directory - add new ones or edit existing one

https://sailsjs.com/documentation/concepts/models-and-orm/models

### Cleaning database
Stop Docker/Node app and delete `db/data` directory

### Creating initial database state
Insert valid SQL file into `db/init` file with .sql suffix

### Creating database dump
```
docker-compose exec db bash
pg_dump -U postgres mtaadb > /dump/20180306-1315.sql
```
```
docker-compose exec db bash
/usr/bin/mysqldump -u root -p mtaadb > /dump/20180306-1330.sql
```
