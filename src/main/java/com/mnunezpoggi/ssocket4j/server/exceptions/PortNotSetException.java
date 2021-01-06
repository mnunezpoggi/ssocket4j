/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mnunezpoggi.ssocket4j.server.exceptions;

/**
 *
 * @author radxt
 * 
 * Custom Exception that raises when no port was set and the server was
 * called to start
 * 
 */
public class PortNotSetException extends Exception{
    
    public PortNotSetException(){
        super("Error: Please set port first");
    }
    
}
