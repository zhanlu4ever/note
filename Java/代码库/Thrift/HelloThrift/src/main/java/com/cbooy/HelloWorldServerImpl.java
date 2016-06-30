package com.cbooy;

import org.apache.thrift.TException;

public class HelloWorldServerImpl implements HelloWorldService.Iface {
	public String sayHello(String username) throws TException {
		return "hi nihao , " + username + " hello world";
	}
}
