package com.cbooy;

import com.cbooy.service.impl.CashierServiceImpl;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import com.cbooy.service.CashierService;

public class Server {
	public static void main(String[] args) throws TTransportException {
		System.out.println("HelloWorld TSimpleServer start ....");

		TServerSocket serverTransport = new TServerSocket(9999);
		
		CashierService.Processor<CashierService.Iface> tprocessor = new CashierService.Processor<CashierService.Iface>(new CashierServiceImpl());

		TServer.Args tArgs = new TServer.Args(serverTransport);
		tArgs.processor(tprocessor);
		tArgs.protocolFactory(new TCompactProtocol.Factory());
		
		TServer server = new TSimpleServer(tArgs);
		server.serve();
	}
}
