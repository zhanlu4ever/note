package com.cbooy;

import com.cbooy.beans.Caller;
import com.cbooy.beans.Order;
import com.cbooy.beans.UserInfo;
import com.cbooy.service.CashierService;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

/**
 * Created by root on 16-6-28.
 */
public class Client {
    public static void main(String[] args) {
        TTransport transport = new TSocket("10.97.212.16",9999,5000);
        try {
            transport.open();

            TProtocol protocol = new TCompactProtocol(transport);

            CashierService.Client client = new CashierService.Client(protocol);

            Order order = new Order();
            order.setOrderId("xxxx:1111");

            UserInfo userInfo = new UserInfo();
            userInfo.setUserID("user:122222");
            userInfo.setAccountType(1);

            order.setUserInfo(userInfo);
            Caller caller = new Caller();
            caller.setCaller("haoc");

            client.createOrder(caller,order);
            String status = client.status();
            System.out.println(status);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
