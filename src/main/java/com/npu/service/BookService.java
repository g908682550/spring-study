package com.npu.service;

import com.npu.dao.BookDao;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    private BookDao bookDao;

    @Override
    public String toString() {
        return "BookService{" +
                "bookDao=" + bookDao +
                '}';
    }

    public void print(){
        System.out.println(bookDao);
    }
}
