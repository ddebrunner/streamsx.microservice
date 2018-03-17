# Publish-subscribe overview
Publish-subscribe provides the foundation to create streaming microservices.
## Streaming microservices
Streaming microservices publish streams that are independently subscribed to by other streaming microservices.
A streaming microservice (henceforth referred to as simply a *microservice*) is a job running in a IBM Streams instance.
Publish-subscribe thus works between microservices (jobs) running within the same instance. The instance can be the one provided by
Streaming Analytics service on IBM Cloud or an on-prem deployment of IBM Streams.

See also: https://developer.ibm.com/streamsdev/2016/09/02/analytics-microservice-architecture-with-ibm-streams/

### Typical pattern
A typical pattern is to have three types of microservices:
 * **Ingest microservices** that publish streams of data from external sources. An ingest microservice may publish the raw data or
 provide value-add through data cleansing and/or enrichment. Providing source data as published stream provides isolation and
 allows developers of downstream applications to be unaware of external source details, such as connection credentials, URLs, hostnames etc.
 * **Analytical microservices** that subscribe to streams published by _ingest microservices_ and/or _analytical microservices_, perform streaming data analysis and then
 publish streams containing insights from the data. These published streams can contain final results for _egress microservices_
 or intermediate results for other _analytical microservices_. 
 * **Egress microservices** that subscribe to streams published by _analytics microservices_ and send the insights to external systems.
 Again this allows upstream developers to be unaware of details of how to write data to an external system.
 
 This pattern is just an example, a microservice can publish and/or subscribe to any number of streams.
 
 ## Implementation language independence
 Microservices can be implemented in any programming language supported by IBM Streams, so for example a Python
 _analytics microservice_ can subscribe to streams published by an SPL _ingest microservice_.
 
 ## Published stream
 
 A stream is published to a *topic* and has a *type*. Subscribers match published stream based upon the _topic_ and _type_.
 
 ## Published topic
 
 Publishing a stream publishes it to a single _topic. 
 
