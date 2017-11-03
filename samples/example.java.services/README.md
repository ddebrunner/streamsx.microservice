# Example Java Microservice

This project is an example implementation of a microservice written using the Java Topology API.  The project contains two microservices:

* PublishReadingsService - This service generate random reading data.  A reading is simply a timestamp and a value.  The service generates readings and publishes the data for other services to consume.
    * This service publishes data with this topic: example/java/services/PublishReadingsService/reading/v1    
* SubscribeAndPrintReadingsService - This service subscribes data from the `PublishReadingsService` and print the data in the console output.
    * This services subcribes data with this topic:  example/java/services/PublishReadingsService/reading/v1
    * This definition can be found in the service.properties.

## Directory Structure

The microservices project has this directory structure:

```
example.java.services
  |
  |---- info.xml - toolkit information of the service
  |---- build.gradle - build script for the Java service 
  |---- service.properties - contain properties to be passed to the service
  |---- impl/java/src - contains Java code of the service 
  |---- opt/lib - contains third-party libraries required by the service
```

Although this is just a plain Java project, we treat the project as a toolkit (with toolkit name and versions).  As the project evolves, the toolkit version should be updated based on toolkit versioning guidelines.  If the services require other toolkits, those dependencies can be specified in the info.xml as documentation.

## Service

A Java service class should adhere to the following guidelines:

* The Java service class should reside in a package that ends with .services
* The Java service class should subclass from AbstractService
* The Java service class should be named with a `Service` suffix. 

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

## Excuting a Pipeline of Services

* build.gradle is set up to show how one can execute a pipeline of services.  The **execute** target is set up for this purpose.
* The **execute** target executes the microservices that are part of the pipeline in sequence.

* In this example, the pipeline only contains two services.  In a more complex application, the **execute** target can be set up to run more microservices.  
* The connections between services (i.e. topic subscriptions) are can be set up the service.properties file.
* Because service properties scoped by prefixing the property name with the service's fully qualified name, a single properties file can be used to describe properties all the services in the pipeline.
* Running the **execute** target will run all services that are part of the pipeline.


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
