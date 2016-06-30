package com.cbooy;

import com.facebook.swift.codec.*;
import com.facebook.swift.codec.ThriftField.Requiredness;
import com.facebook.swift.service.*;
import com.google.common.util.concurrent.ListenableFuture;
import java.io.*;
import java.util.*;

@ThriftService("HelloService")
public interface HelloService
{
    @ThriftService("HelloService")
    public interface Async
    {
        @ThriftMethod(value = "hello")
        ListenableFuture<String> hello();
    }
    @ThriftMethod(value = "hello")
    String hello();

}