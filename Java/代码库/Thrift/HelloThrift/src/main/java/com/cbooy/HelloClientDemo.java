package com.cbooy;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class HelloClientDemo {
	public static final String SERVER_IP = "127.0.0.1";
	public static final int SERVER_PORT = 8080;
	public static final int TIMEOUT = 50000;

	public static void main(String[] args) {
		HelloClientDemo client = new HelloClientDemo();
		client.startClient("jack");
	}

	public void startClient(String userName) {
		TTransport transport = null;
		try {
			transport = new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT);
			transport.open();
			
			TProtocol protocol = new TCompactProtocol(transport);
			
			HelloWorldService.Client client = new HelloWorldService.Client(protocol);
			String result = client.sayHello(userName);
			
			System.out.println("client called...." + result);
		} catch (TTransportException e) {
			e.printStackTrace();
		} catch (TException e) {
			e.printStackTrace();
		} finally {
			if (null != transport) {
				transport.close();
			}
		}
	}
}
