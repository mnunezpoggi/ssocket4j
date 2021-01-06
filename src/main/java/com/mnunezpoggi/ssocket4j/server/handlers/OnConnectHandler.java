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
 * Interface for handling new connections from clients
 */
public interface OnConnectHandler {
    public void onConnect(Integer key);
}
