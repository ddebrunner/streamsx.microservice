# Publish-subscribe overview
Publish-subscribe provides the foundation to create streaming microservices.
## Streaming microservices
Streaming microservices publish streams that are independently subscribed to by other streaming microservices.
A streaming microservice (henceforth referred to as simply a *microservice*) is a job running in a IBM Streams instance.
Publish-subscribe thus works between microservices (jobs) running within the same instance. The instance can be the one provided by
Streaming Analytics service on IBM Cloud or an on-prem deployment of IBM Streams.

See also: https://developer.ibm.com/streamsdev/2016/09/02/analytics-microservice-architecture-with-ibm-streams/

### Dyanamic and independent
The publish-subscribe model allows the publisher and subscriber to be independent. Any number of publishers and subscribers can exist and be unware of any implementation details of any microservice. No pre-registration is needed, if a subscriber's stream subscription matches a published stream then the connection is made and subscriber microservice starts consuming data from the publishing microservice.

Publish-subscribe is also dynamic, when a subscribing microservice is submitted it will connect to any matching published streams that exist at the time. Subsequently if new publishing microservices are submitted they will connect to any existing subscribing microservice that have a matching subscription.

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
Publishing a stream publishes it to a single _topic_. Topic syntax is the same as MQTT topic syntax. A topic consists of one or more topic levels. Each topic level is separated by a forward slash (topic level separator), for example `twitter/tweets/raw`. There can be an arbitrary number of levels, typically made up of three parts:
  * A topic domain space, typically one or two levels - allows microservices to be developed independently without fear of clashes leading to incorrect subscriptions. For example:
   * `twitter/tweets/raw` - the domain is `twitter` indicating any topic starting with `twitter` is from Twitter.
   * `streamsx/transportation/nextbus/actransit/locations` - the domain is `streamsx/transportation` and is the pattern used by the toolkits from IBMStreams organization at github.com using `streamsx` as the first element and then the project name as the second. This ensures that re-useable microservices implemented by different projects will not end up accidentially using the same topic.
  * The category of the data within the domain, for example within the `twitter` domain these could exist:
    * `twitter/tweets/raw` - `tweets` is the category and means that any published stream with a topic starting with `twitter/tweets` is related to tweets, in this case the raw JSON of the tweet.
    * `twitter/users/new` - `users` is the category and means that any published stream with a topic starting with `twitter/users` is related to users, such as for this topic a stream containing users that have just registered.
    * `streamsx/transportation/nextbus/actransit/locations` - `nextbus` is the category for data from NextBus.
  * Levels following the domain and category topic levels that indicate the data being published. For example:
    * `twitter/tweets/raw` - A published stream containing the full raw JSON of each tweet.
    * `twitter/tweets/text` - A published stream containing just the text of each tweet.
    * `streamsx/transportation/nextbus/actransit/locations` - Nextbus locations for AC Transit agency.
    * `streamsx/transportation/nextbus/sf-muni/locations` -  Nextbus locations for San Francisco Muni agency.
    
 Subscribers can use topic filters to subscribe to multiple topics, for example `streamsx/transportation/nextbus/+/locations` subscribes
 to published streams for NextBus locations from any agency being published.
 
Before designing a system comprising of multiple microservices the topic scheme used must be designed with publishers and subscriber use cases in mind.

## Published type
A stream is published with its stream type, this is one of:
  * The stream's schema - its SPL or structured schema. Supported by all programming languages.
    * For example a published stream of sensor data might have the schema `tuple<timestamp ts, rstring id, float64 value>`
    * Each tuple is handled as streams tuple with named attributes, tuple in SPL, `com.ibm.streams.operator.Tuple` in Java, `dict`, `tuple` or named tuple in Python.
  * An interchange type - A type that treats each tuple as a single value, a JSON object, a string, binary data or an XML document.
    * Each tuple is handled as a single object, for example with string `java.lang.String` in Java, `str` in Python.
  * A Java class - Each tuple on the stream is an instance of the declared class or interface. Only supported by Java applications.
  * A Python object - Each tuple on the stream is a Python object of any type. Only supported by Python applications.
 
