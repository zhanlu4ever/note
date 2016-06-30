package com.cbooy.service.impl;

import com.cbooy.service.CashierService;
import org.apache.thrift.TException;

import com.cbooy.beans.Caller;
import com.cbooy.beans.Order;
import com.cbooy.beans.ResponseMsg;

/**
 * Created by chenhao on 2016/6/28.
 */
public class CashierServiceImpl implements CashierService.Iface {
    public ResponseMsg createOrder(Caller caller, Order order) throws TException {

        System.out.println("caller : " + caller);
        System.out.println("order : " + order);

        ResponseMsg msg = new ResponseMsg();
        msg.setErrorMsg("createOrder success");
        msg.setErrorNO("200");
        return msg;
    }

    public String status() throws TException {
        return "success";
    }
}
