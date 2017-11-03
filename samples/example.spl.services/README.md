# Example SPL Microservice

This project is an example implementation of a microservice written using the SPL.  The project contains two microservices:

* PublishReadingsService - This service generate random reading data.  A reading is simply a timestamp and a value.  The service generates readings and publishes the data for other services to consume.  
* SubscribeAndPrintReadingsService - This service subscribes data from the `PublishReadingsService` and print the data in the console output.

The project also contains two Java service wrappers to help compile and launch the SPL service.  The Java wrapper enables clients to have a consistent experience regardless of the langauge used to implement a microservice.  With this approach the end user will customize, build and execute the same way as a Java microservice.

## Directory Structure

The microservices project has this directory structure:

```
example.java.services
  |
  |---- info.xml - toolkit information of the service
  |---- build.gradle - build script for the Java service 
  |---- service.properties - contain properties to be passed to the service
  |---- impl/java/src - contains Java wrapper of the SPL microservices
  |---- example.spl.services - contains the SPL code of the microservices
  |---- opt/lib - contains third-party libraries required by the service
```

## Service

A SPL microservice application should adhere to the following guidelines:

* A SPL microservice application should reside in a namespace that ends with `.services`
* A SPL microservice application should be a main composite, named with a `Service` suffix.

A SPL microservice will be accompanied by a Java service wrapper.  The responsibility of the Java service wrapper is to:

* create the topology that uses the SPL microservice
* When the wrapper is run, the Java service wrapper compiles and launch the SPL microservice.

A Java service wrapper should adhere to the following guidelines:

* The Java service wrapper should reside in a package that ends with `.services`
* The Java service wrapper should subclass from `AbstractSPLService`
* The Java service wrapper should be named with a `Service` suffix. 

When clients use the microservice, they will be interacting with the Java service wrapper, rather than the undelying SPL service.

### Service Properties

Customization of a service are defined in a service.properties file.  The following are global properties, to be supported by all services.  These properties are handled by `AbstractService` class.  So any services that subclass from this abstract class will get this support by default:

* debug - to enable tracing and additional debugging information
* streamscontext - the context to submit the streams job to (embedded, standalone, distributed or streaming analytics)
* vmargs - additional vm arguments to pass to the PE JVM.

A service may define a set of custom properties.  Custom properties should be scoped and prefixed with the service's fully qualified name.  (e.g.  example.java.services.property1).   The scoping allows us to combine properties from multiple services into a single property file, without running into name collision.    

### Publishing Data

Services employ a publish-subscribe model to send or receive data to another service.  When publishing data, topic definition should follow these guidelines:

* The topic be defined using the MQTT convention as noted by the Publish/Subscribe operator in the streamsx.topology toolkit. (e.g. a/b/c/d)
* The topic should begin with the fully qualified name of the service, delimited by "/"
* Next the topic should describe the data being published  
* The last segment of the topic should be a version number
* For example: a/b/c/readings/v1
* All services publish data in JSON format, to maximize service interoperability with different languages.  

#### Evolving Data Schema

* If the data schema needs to change as the service evolves, follow these guidelines:
    1.  Adding a new attribute to the schema is not a breaking change.  Update the *minor* number of the topic version as follows to indicate a change in the schema:  e.g. a/b/c/readings/v1 -> **a/b/c/readings/v1/1**
    1.  Renaming or removing of an attribute breaks compatibility.  If this has to be done, update the *major* number of the topic version as follows to indicate a breaking change:  e.g. **a/b/c/readings/v2** 
    1.  Renaming or removing attributes is discouraged as it breaks application compatibility.  It is recommended that existing data stream is maintained.  New data stream can be added to a microservice to maintain compatibility.
 
### Subscribing Data

To subscribe data from an upstream application, a service should employ the following guidelines:

* If a service can only handle one version of the data being published, match the name of the topic exactly.  e.g. a/b/c/readings/v1
* If a service can handle non-breaking changes in the data schema from an upstream application.  A service should subscribe to data using the **#** wildcard.  e.g. a/b/c/readings/v1/# - In this case, as long as the upstream application does not bump the *major* version number, the service can continue to receive data.
        
## Building a Service

* A service is to be built using the build.gradle script.  The script is set up to build any Java code residing in the impl/java/src directory.
* If a service require a third-party library, client is responsible to define these libraries in the build script.  The dependencies will be downloaded by the build script and  stored in the opt/lib directory.  

## Executing a Service

* build.gradle is set up to execute the service, using the **execute_service_name** target.  
* Clients are expected to configure this target to identify the main class and jar file for running the service.
* Clients may also define additional execute targets if the project contains more than one service.


## Evolving a Service

A service's API is defined by the following:

* Version Number in info.xml
* Name of the Service
* Properties in the service.properties file
* Data publication topic
* Data schema of the data being published

To maintain backwards compatibility and avoid breaking downstream applications, developers should follow these guidelines when evolving a service:

|API     |Guideline |
|--------|----------|
|Version in info.xml | Follow toolkit versioning guideline |
|Name of Service | Cannot be changed after it is defined |
|Properties in service.properties | New properties can be added without breaking compatibility.  Removing or renaming a property result in a breaking change. |
|Data publication topic | New topic can be added without breaking compatibility.  Adding new segments to existing topic can also be done without breaking compatibility.  Removing or renaming a segment of an existing topic is a breaking change.|
|Data Schema | New attributes can be added to the schema without breaking compatbility.  Removing or renaming an attribute is a breaking change.|
