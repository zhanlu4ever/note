package com.cbooy;

import java.util.concurrent.ExecutionException;

import com.facebook.nifty.client.FramedClientConnector;
import com.facebook.swift.service.ThriftClientManager;
import com.google.common.net.HostAndPort;

public class Client {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ThriftClientManager clientManager = new ThriftClientManager();
		
		// 异步的,createClient返回Future接口
		HelloService helloService = clientManager.createClient(
				new FramedClientConnector(HostAndPort.fromParts("localhost", 9999)), 
				HelloService.class).get();
		
		String res = helloService.hello();
		
		System.out.println(res);

		clientManager.close();
	}
}
