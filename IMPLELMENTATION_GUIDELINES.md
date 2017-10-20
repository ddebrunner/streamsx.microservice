# Microservice implementation guidelines

_This is an initial set of guidelines that may evolve over time_ 

A microservice is a Streams application, that publishes and/or subscribes to streams from other microservices.

A microservice is typically configurable through submission time parameters, typically with use of application configurations.

## Naming

The policy is to:
* Have the application use a namespace that ends in .services, e.g. com.ibm.streamsx.transportation.nextbus.services
* Have the application name end in Service, e.g. `AgencyLocationsService`.

## Publish/subscribe

A microservice publishes and/or subscribes to streams using the publish-subscribe paradigm defined by the topology toolkit.
* Topic names are like MQTT topics and a toolkit should use a scheme that makes the topics unique to it, e.g. the transportation toolkits uses `streamsx/transportation/subtopic(s)`.
* Topic names must describe the contents of the stream
  * Note topic names can be multiple levels, e.g. `streamsx/transportation/nextbus/buses/locations`.
  * The topic name should reflect the contents of the stream, not the purpose of the microservice.
    * E.g. a TwitterIngestService may publish a stream of tweets using a topic `streamsx/social/twitter/tweets` not a topic of `streamsx/social/twitter/ingest`.
  * Hint: Write a one sentence description of what is being published, the topic name should closely relate to the description.
  * Note that a microservice may be enhanced in the future to publish more streams, maybe derived results of the original streams, so any initial topic should consider the potential for additional related topics (either by the same service, or additional services).

Published stream types may be SPL/structured streams, JSON or string.
* SPL/structured streams have the benefit that a subscriber can filter on the published tuples
* JSON streams have the benefit of being flexible, in that the service can be enhanced to add additional values into the JSON object without breaking existing subscriber microservices.
* String streams (each tuple is a single UTF-8 encoded string) is a simple schema.

These schemas can be consumed by a microservice implemented in any langauge.

##  SPL

A microservice is an SPL main composite, thus
* No input/output streams.
* Any composite parameters are optional, typically the default being a submission time parameter.
  * the submission time parameter may have a default as well, e.g. a for a twitter ingest service the default application configuration name might be "Twitter".
  * Composite parameter names should reflect their purpose, not their implementation, e.g. 'twitterCredentials', not 'applicationConfigurationName'.

## Stream names
Use good names for streams/operator invocations in microservices, specifically don't just use the generated names provided by Streams studio.
* take time to look at your running microservice in the console, do the stream/operator invocation names provide clear understanding as to what the application is doing.
* https://www.ibm.com/support/knowledgecenter/en/SSCRJU_4.2.1/com.ibm.streams.dev.doc/doc/str_nameconv.html
