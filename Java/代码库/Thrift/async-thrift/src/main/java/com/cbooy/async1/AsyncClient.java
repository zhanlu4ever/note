package com.cbooy.async1;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.*;

import java.io.IOException;

/**
 * Created by chenhao on 2016/6/30.
 */
public class AsyncClient {
    public static void main(String[] args) throws Exception {
//        async();
        sync();
    }

    public static void sync() throws TException {
        TSocket socket = new TSocket("127.0.0.1", 9999, 5000);
        TTransport transport = new TFramedTransport(socket);
        TProtocol protocol = new TCompactProtocol(transport);
        Ping.Client client = new Ping.Client(protocol);

        transport.open();

        String result = client.ping();

        System.out.println(result);

        transport.close();
    }

    public static void async() throws TException, IOException, InterruptedException {
        TAsyncClientManager clientManager = new TAsyncClientManager();

        TNonblockingTransport transport = new TNonblockingSocket("127.0.0.1", 9999, 5000);

        TProtocolFactory protocol = new TCompactProtocol.Factory();

        Ping.AsyncClient client = new Ping.AsyncClient(protocol, clientManager, transport);

        client.ping(new AsyncMethodCallback<Ping.AsyncClient.ping_call>() {
            public void onComplete(Ping.AsyncClient.ping_call ping_call) {
                System.out.println("onComplete");
                try {
                    System.out.println(ping_call.getResult().toString());
                } catch (TException e) {
                    e.printStackTrace();
                }
            }

            public void onError(Exception e) {
                System.out.println("onError");
            }
        });

        Thread.sleep(5000);
    }
}
