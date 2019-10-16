# Take Home Engineering Challenge

## Technical Details

### Language & Build
This project is a Scala based project using the Play web framework.
I chose it because it's the language I'm most comfortable with.  Given more
time I might have chosen something else but I know this'll can be a 
stateless scalable application.

Since it's Scala Play, SBT is the build and dependency management tool.

### Other Utils
Two complicated problems which I didn't want to rewrite were
CSV parsing and geospatial searching.

I've used two off the shelve offerings to solve both problems.

For CSV parsing, I chose an Apache Commons util.

For geospatial searching, I chose an in memory lucene index.

### Project Layout
This follows the Play project layout.

* `app/` Scala source organized by package
* `test/` Scala tests organized by package
* `project/` Configuration for the SBT plugin itself
* `build.sbt` Configuration for the projects build and dependencies


## Code Architecture

I've used some basic DDD patterns to separate out concerns
of different components.  

### Domain
Collection of simple objects which represent entities and value objects
represented in the CSV.  Currently they only have the minimum
which is needed as far as fields for this.

### Infrastructure
This layer would encapsulate any datasource.  For the time limit
here I chose to focus on the CSV file provided.
Interactions with the CSV file and transformation into the 
domain entity can be found in the `CSVRepository` class.

### Service
There are two services in this project: `FoodTruckService` and `SearchService`

The `SearchService` is an application service that encapsulates 
the lucene and spatial searching
functionality.  Given a list of FoodTrucks, it creates the in memory index
which can be used for searching by other clients.

The `FoodTruckService` is a domain service which contains
business functionality like max results and also composes
our infrastructure layer and search together.

## Running this project

### Using SBT
To run this project, you'll need to have SBT installed.

With sbt you can issue commands in the console or 
run them directly in the command line.

Example console usage for testing:

```bash
$ sbt
[info] Loading global plugins from /Users/jose/.sbt/1.0/plugins
...
[take-home-engineering-challenge] $ test
...
[success] Total time: 12 s, completed Oct 16, 2019 10:59:23 AM
[take-home-engineering-challenge] $ 
```

Example arguement usage for running:

```bash
$sbt run
[info] Loading global plugins from /Users/jose/.sbt/1.0/plugins
...
[info] p.c.s.AkkaHttpServer - Listening for HTTP on /0:0:0:0:0:0:0:0:9000

(Server started, use Enter to stop and go back to the console...)
```

### Making Requests

Currently only a JSON API exists for the application.
Requests can be made using `curl`.

Example request for all trucks using JQ to format:

```bash
$ curl 'http://localhost:9000/food-trucks' | jq
```

Example request for searching 5 closest trucks to latitude and longitude

```bash
$ curl 'http://localhost:9000/food-trucks?lat=37.7101930199757&long=-122.455221906126' | jq
```

## Improvements I'd make

### Replacing the infrastructure layer
Instead of a CSV repository we should be feeding directly 
from the API.

### Replacing the search service
An in memory index is enough for this exercise and finite dataset
but for the problem to be scalable it should be pulled out
into a suitable datastore.

### A UI!
I would have liked to build a nice react based UI

### More Testing

I would like to add more unit testing to the controller
Would have been nice to at least cover the JSON serialization.

I'm also a fan of component level testing.  Specifically testing the built artifact
to make sure everything is composed together correctly.

