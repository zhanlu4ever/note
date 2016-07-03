package com.cbooy.async1;

import org.apache.thrift.TProcessorFactory;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;

/**
 * Created by chenhao on 2016/6/30.
 */
public class AsyncServer {
    public static void main(String[] args) throws TTransportException {
        TNonblockingServerSocket socket = new TNonblockingServerSocket(9999);

        Ping.Processor processor = new Ping.Processor(new PingImpl());

        THsHaServer.Args serverArgs = new THsHaServer.Args(socket);

        serverArgs.protocolFactory(new TCompactProtocol.Factory());
        serverArgs.processorFactory(new TProcessorFactory(processor));
        serverArgs.transportFactory(new TFramedTransport.Factory());

        TServer server = new THsHaServer(serverArgs);
        server.serve();
    }
}
