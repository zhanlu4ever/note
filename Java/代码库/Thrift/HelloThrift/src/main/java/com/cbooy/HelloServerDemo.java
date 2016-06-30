package com.cbooy;

import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;

public class HelloServerDemo {
	static final int SERVER_PORT = 8080;

	public static void main(String[] args) {
		HelloServerDemo server = new HelloServerDemo();
		server.startServer();
	}

	public void startServer() {
		try {
			System.out.println("HelloWorld TSimpleServer start ....");

			TServerSocket serverTransport = new TServerSocket(SERVER_PORT);
			
			HelloWorldService.Processor<HelloWorldService.Iface> tprocessor = new HelloWorldService.Processor<HelloWorldService.Iface>(new HelloWorldServerImpl());

			TServer.Args tArgs = new TServer.Args(serverTransport);
			tArgs.processor(tprocessor);
			tArgs.protocolFactory(new TCompactProtocol.Factory());
			
			TServer server = new TSimpleServer(tArgs);
			server.serve();

		} catch (Exception e) {
			System.out.println("Server start error!!!");
			e.printStackTrace();
		}
	}
}
