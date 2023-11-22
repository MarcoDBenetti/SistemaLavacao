/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package br.edu.ifsc.fln.model.database;

import java.sql.Connection;

/**
 *
 * @author marco
 */
public interface Database {    
    public Connection conectar();
    public void desconectar(Connection connection);
}
