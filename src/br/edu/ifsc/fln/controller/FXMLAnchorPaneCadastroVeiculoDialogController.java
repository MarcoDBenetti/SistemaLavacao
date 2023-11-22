/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cor;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author marco
 */
public class FXMLAnchorPaneCadastroVeiculoDialogController implements Initializable {

    @FXML
    private Label lbModelo;
    @FXML
    private Label lbId;
    @FXML
    private Label lbIdDB;
    @FXML
    private Label lbMarca;
    @FXML
    private ComboBox<Marca> cbMarcaDB;
    @FXML
    private ComboBox<Modelo> cbModeloDB;
    @FXML
    private Label lbPlaca;
    @FXML
    private Label lbObservacoes;
    @FXML
    private Label lbCor;
    @FXML
    private TextField tfPlaca;
    @FXML
    private ComboBox<Cor> cbCorDB;
    @FXML
    private TextField tfObservacoes;
    @FXML
    private Button btnVeiculoDialogCancelar;
    @FXML
    private Button btnVeiculoDialogSalvar;

    private Stage dialogStage;
    private boolean btnSalvarClicked = false;
    private Veiculo veiculo; 
    private final MarcaDAO marcaDAO = new MarcaDAO();
    private List<Marca> marcas = new ArrayList<>();
    private ObservableList<Marca> obsMarcas;
    private final ModeloDAO modeloDAO = new ModeloDAO();
    private List<Modelo> modelos = new ArrayList<>();
    private ObservableList<Modelo> obsModelos;
    private final CorDAO corDAO = new CorDAO();
    private List<Cor> cores = new ArrayList<>();
    private ObservableList<Cor> obsCores;
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
  
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

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
        tfPlaca.setText(veiculo.getPlaca());
        tfObservacoes.setText(veiculo.getObservacoes());
        cbModeloDB.getSelectionModel().select(veiculo.getModelo());
        cbCorDB.getSelectionModel().select(veiculo.getCor());
        //Verificar POG
        if(veiculo.getModelo() != null){
        cbMarcaDB.getSelectionModel().select(veiculo.getModelo().getMarca());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarMarcas();
        carregarCores();
        cbModeloDB.setMouseTransparent(true);
    }    
    
    public void carregarMarcas(){
        marcaDAO.setConnection(connection);
        marcas = marcaDAO.listar();
        obsMarcas = FXCollections.observableArrayList(marcas);
        
        cbMarcaDB.setItems(obsMarcas);
    }
    
    public void carregarModelos(){
        modeloDAO.setConnection(connection);
        modelos = modeloDAO.listarPorMarca(cbMarcaDB.getSelectionModel().getSelectedItem());
        obsModelos = FXCollections.observableArrayList(modelos);
        
        cbModeloDB.setItems(obsModelos);
    }
    
    public void carregarCores(){
        corDAO.setConnection(connection);
        cores = corDAO.listar();
        obsCores = FXCollections.observableArrayList(cores);
        
        cbCorDB.setItems(obsCores);
    }
    
    @FXML
    private void handleBtnVeiculoDialogCancelar(ActionEvent event) {
        dialogStage.close();
    }

    @FXML
    private void handleBtnVeiculoDialogSalvar(ActionEvent event) {
        if (validarEntradaDeDados()) {
            veiculo.setPlaca(tfPlaca.getText());
            veiculo.setObservacoes(tfObservacoes.getText());
            veiculo.setModelo(cbModeloDB.getSelectionModel().getSelectedItem());
            veiculo.setCor(cbCorDB.getSelectionModel().getSelectedItem());
            btnSalvarClicked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    void handleCBMarca(ActionEvent event) {
        cbModeloDB.setMouseTransparent(false);
        carregarModelos();
    }
    
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        
        if (tfPlaca.getText() == null || tfPlaca.getText().isEmpty()) {
            errorMessage += "Descrição inválida!\n";
        }
        
        if (cbModeloDB.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione um modelo!\n";
        }
        
        if (cbCorDB.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma cor!\n";
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campo(s) inválido(s), por favor corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
        }
}
