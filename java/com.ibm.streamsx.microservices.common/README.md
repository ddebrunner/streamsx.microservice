# Microservice Framework

This microservice frame enables clients to create Java or SPL services easily.  The goal is to have clients focus on creating the topology of the microservice.
The framework standardizes how a microservice is setup, customized, build and launch.

# AbstractService class

This is the base abstract class for all microservices.  Java microservices should subclass from this class.
This class is responsible for the following:

* Reading of service.properties file into a Properties object.  Function to allow a subclass to access service properties.
* Adding of all third party jar files, stored in opt/lib directory as jar dependency for the topology
* Providing a framework to help clients create a topology, and subsequently submitting the topology based on the context as specified in the service.properties.
* Enable debug and tracing based on the debug option as specified in the service.properties.

To create a Java microservice, client needs to:

* Subsclass from AbstractService
* Implement the `createToopology` function
* Implement a main method to instantiate and execute the service

For an example of a Java microservice, see samples/java/example.java/services.

# AbstractSPLService class

This is the base abstract class for all SPL service wrappers.  SPL service wrappers should subclass from this class.

This class is responsible for the following:

* Reading of service.properties file into a Properties object.  Function to allow a subclass to access service properties.
* Adding of all third party jar files, stored in opt/lib directory as jar dependency for the topology
* Adding the SPL toolkit, containing the SPL microservice, as toolkit dependency.
* Creation of a topology that calls the underlying SPL service and subsequently submitting the topology based on the context as specified in the service.properties.
* Enable debug and tracing based on the debug option as specified in the service.properties.

To create a SPL microservice, client needs to:

* Create a SPL main composite of the microservice
* Subsclass from AbstractSPLService
* Implement the `getMainCompositeFQN` function and provide the name of the main composite to execute
* Implement a main method to instantiate and execute the service