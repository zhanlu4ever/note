package com.cbooy.async1;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;

/**
 * Created by chenhao on 2016/6/30.
 */
public class PingImpl implements Ping.Iface {

    public String ping() throws TException {
        return "pong";
    }
}
