/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.domain.Marca;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author marco
 */
public class FXMLAnchorPaneCadastrosMarcaDialogController implements Initializable {

    @FXML
    private Button btnMarcaDialogCancelar;

    @FXML
    private Button btnMarcaDialogSalvar;
    
    @FXML
    private Label lbNome;

    @FXML
    private TextField tfNome;
    
    private Stage dialogStage;
    private boolean btnSalvarClicked = false;
    private Marca marca;   

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogeStage) {
        this.dialogStage = dialogeStage;
    }

    public boolean isBtnSalvarClicked() {
        return btnSalvarClicked;
    }

    public void setBtnSalvarClicked(boolean btnSalvarClicked) {
        this.btnSalvarClicked = btnSalvarClicked;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
        this.tfNome.setText(marca.getNome());
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
    @FXML
    void handleBtnMarcaDialogCancelar(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    void handleBtnMarcaDialogSalvar(ActionEvent event) {
        if (validarEntradaDeDados()) {
            marca.setNome(tfNome.getText());

            btnSalvarClicked = true;
            dialogStage.close();
        }
    }
    
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        if (this.tfNome.getText() == null || this.tfNome.getText().length() == 0) {
            errorMessage += "Descrição inválida.\n";
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            //exibindo uma mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Corrija os campos inválidos!");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
}
