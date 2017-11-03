//*******************************************************************************
//* Copyright (C) 2017 International Business Machines Corporation
//* All Rights Reserved
//*******************************************************************************

package example.java.services;

public interface IServiceConstants {
	
	String PREFIX = "example.java.services.PublishReadingsService";
	
	
	// Topic should follow MQTT-Style.  It is recommended each data stream end with
	// a version number.  This helps to maintain backwards compatibility.  
	// Clients subscribe by using the following topic filter:  /com/ibm/streamsx/health/example/java/observation/v1/#
	//
	// If changes in data schema are backwards compatible (e.g. additional of new attributes), the service
	// will increment the version number to a/b/c/v1/1, a/b/c/c/v1/2, etc.  
	// 
	// If changes in data schema are not backwards compatible,
	// (e.g. changing of data types or removal of attributes), then the service may update the data
	// stream version number by incrementing the major version as followings:  a/b/c/v1 becomes a/b/c/v2.
	//
	// Clients may choose to subscribe to a newer version of data stream when they choose to
	// when it becomes available.
	String READING_TOPIC = "example/java/services/PublishReadingsService/reading/v1";

}
