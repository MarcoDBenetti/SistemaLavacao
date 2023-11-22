/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.edu.ifsc.fln.model.dao;

import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Marca;

/**
 *
 * @author marco
 */
public class ModeloDAO {
    private Connection connection;
    
    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public boolean inserir(Modelo modelo) {
        final String sql = "INSERT INTO modelo (descricao, id_marca, categoria) VALUES(?, ?, ?)";
        final String sqlMotor = "INSERT INTO motor (id_modelo, potencia, tipo_combustivel) values ((SELECT max(id) FROM modelo), ?, ?);";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modelo.getDescricao());
            stmt.setInt(2, modelo.getMarca().getId());
            stmt.setString(3, modelo.getCategoria().getDescricao());
            stmt.execute();
            //registra o motor do modelo imediatamente
            stmt = connection.prepareStatement(sqlMotor);
            stmt.setInt(1, modelo.getMotor().getPotencia());
            stmt.setString(2, modelo.getMotor().getCombustivel().getDescricao());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public boolean alterar(Modelo modelo) {
        final String sql = "UPDATE modelo SET descricao=?, id_marca=?, categoria=? WHERE id=?";
        final String sqlMotor ="UPDATE motor SET potencia=?, tipo_combustivel=? WHERE id_modelo=?;";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, modelo.getDescricao());
            stmt.setInt(2, modelo.getMarca().getId());
            stmt.setString(3, modelo.getCategoria().getDescricao());
            stmt.setLong(4, modelo.getId());
            stmt.execute();
            stmt = connection.prepareStatement(sqlMotor);
            stmt.setInt(1, modelo.getMotor().getPotencia());
            stmt.setString(2, modelo.getMotor().getCombustivel().getDescricao());
            stmt.setInt(3, modelo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Modelo modelo) {
        String sql = "DELETE FROM modelo WHERE id=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setLong(1, modelo.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
    public List<Modelo> listar() {
        String sql =  "SELECT * FROM modelo md INNER JOIN motor mt ON md.id = mt.id_modelo;";
        List<Modelo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Modelo modelo = populateVO(resultado);
                retorno.add(modelo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    public List<Modelo> listarPorMarca(Marca marca) {
        String sql = "SELECT * FROM modelo WHERE id_marca = ?;";
        List<Modelo> retorno = new ArrayList<>();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, marca.getId());
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Modelo modelo = populateVO(resultado, true);
                modelo.setMarca(marca);
                retorno.add(modelo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }

    public Modelo buscar(Modelo modelo) {
        String sql = "SELECT * FROM modelo md INNER JOIN motor mt ON md.id = mt.id_modelo WHERE md.id=?;";
        Modelo retorno = new Modelo();
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, modelo.getId());
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = populateVO(resultado);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ModeloDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    private Modelo populateVO(ResultSet rs) throws SQLException {
        Modelo modelo = new Modelo();
        
        modelo.setId(rs.getInt("id"));
        modelo.setDescricao(rs.getString("descricao"));
        modelo.setCategoria(Enum.valueOf(ECategoria.class, rs.getString("categoria")));
        Marca marca = new Marca();
        marca.setId(rs.getInt("id_marca"));
        modelo.getMotor().setPotencia(rs.getInt("potencia"));
        modelo.getMotor().setCombustivel(Enum.valueOf(ETipoCombustivel.class, rs.getString("tipo_combustivel")));
        MarcaDAO marcaDAO = new MarcaDAO();
        marcaDAO.setConnection(connection);
        marca = marcaDAO.buscar(marca);
        modelo.setMarca(marca);
        return modelo;
    }  
    
    private Modelo populateVO(ResultSet rs, boolean comMarca) throws SQLException {
        Modelo modelo = new Modelo();

        modelo.setId(rs.getInt("id"));
        modelo.setDescricao(rs.getString("descricao"));
        if (comMarca) {
            Marca marca = new Marca();
            marca.setId(rs.getInt("id_marca"));
            MarcaDAO marcaDAO = new MarcaDAO();
            marcaDAO.setConnection(connection);
            marca = marcaDAO.buscar(marca);
            modelo.setMarca(marca);
        }
        return modelo;
    }
}
