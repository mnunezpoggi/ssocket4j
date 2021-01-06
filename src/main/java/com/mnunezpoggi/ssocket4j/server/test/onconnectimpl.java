/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mnunezpoggi.ssocket4j.server.test;

import com.mnunezpoggi.ssocket4j.server.handlers.OnReceiveHandler;

/**
 *
 * @author radxt
 */
public class onconnectimpl implements OnReceiveHandler {


    @Override
    public void onReceive(String message, Integer key) {
        System.out.println("Recibido " + message);
       
    }
    
}
