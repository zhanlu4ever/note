package com.cbooy;

import com.facebook.nifty.core.NettyServerConfig;
import com.facebook.nifty.core.ThriftServerDef;
import com.facebook.swift.codec.ThriftCodecManager;
import com.facebook.swift.service.ThriftEventHandler;
import com.facebook.swift.service.ThriftServer;
import com.facebook.swift.service.ThriftServiceProcessor;
import com.google.common.collect.ImmutableList;

public class Server {

	public static void main(String[] args) {
		ThriftServiceProcessor processor = new ThriftServiceProcessor(
				new ThriftCodecManager(),
				ImmutableList.<ThriftEventHandler> of(), 
				new HelloServiceImpl()
			);

		ThriftServerDef serverDef = ThriftServerDef.newBuilder().listen(9999).withProcessor(processor).build();

		NettyServerConfig serverConfig = NettyServerConfig.newBuilder().build();
		
		ThriftServer server = new ThriftServer(serverConfig, serverDef);
		server.start();
		
		System.out.println("服务已启动!");
	}
}
