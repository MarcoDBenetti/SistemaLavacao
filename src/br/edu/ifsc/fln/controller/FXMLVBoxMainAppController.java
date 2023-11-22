/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author marco
 */
public class FXMLVBoxMainAppController implements Initializable {

    @FXML
    private MenuItem menuItemCadastroCor;
    @FXML
    private MenuItem menuItemCadastroMarca;
    @FXML
    private MenuItem menuItemCadastroModelo;
    @FXML
    private MenuItem menuItemCadastroVeiculo;
    @FXML
    private AnchorPane anchorPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleMenuItemCadastroCor(ActionEvent event) throws IOException{
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/FXMLAnchorPaneCadastroCor.fxml"));
        anchorPane.getChildren().setAll(a);
    }

    @FXML
    private void handleMenuItemCadastroMarca(ActionEvent event) throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/FXMLAnchorPaneCadastroMarca.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    void handleMenuItemCadastroModelo(ActionEvent event) throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/FXMLAnchorPaneCadastroModelo.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    void handleMenuItemCadastroVeiculo(ActionEvent event) throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("../view/FXMLAnchorPaneCadastroVeiculo.fxml"));
        anchorPane.getChildren().setAll(a);
    }
}
