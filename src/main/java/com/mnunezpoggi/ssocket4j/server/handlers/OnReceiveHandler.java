/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mnunezpoggi.ssocket4j.server.handlers;

/**
 *
 * @author radxt
 * 
 * * Interface for handling incoming data from clients
 */
public interface OnReceiveHandler {
     public void onReceive(String message, Integer key);
}
