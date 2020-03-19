package com.example.springdemo;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class HistoryClient extends WebServiceGatewaySupport {

	public Object soapFunction(String url,Object request)
	{
		return getWebServiceTemplate().marshalSendAndReceive(url,request);
	}
}
