package cn.jxnu.cxf.impl;

import cn.jxnu.cxf.HelloWorld;
import org.springframework.stereotype.Component;

import javax.jws.WebMethod;
import javax.jws.WebService;

@Component("helloWorld")
@WebService
public class HelloWorldImpl implements HelloWorld{
    @WebMethod
    public String say(String str) {
        return "Hello"+str;
    }
}
