/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.ECategoria;
import br.edu.ifsc.fln.model.domain.ETipoCombustivel;
import br.edu.ifsc.fln.model.domain.Marca;
import br.edu.ifsc.fln.model.domain.Modelo;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author marco
 */
public class FXMLAnchorPaneCadastroModeloDialogController implements Initializable {

    @FXML
    private Button btnModeloDialogSalvar;

    @FXML
    private Button btnModeloDialogCancelar;

    @FXML
    private ComboBox<Marca> cbMarcaDB;
    
    @FXML
    private ChoiceBox<ETipoCombustivel> chbTipoCombustivel;
    
    @FXML
    private Spinner<Integer> spPotencia;
    
    @FXML
    private ChoiceBox<ECategoria> chbCategoria;

    @FXML
    private TextField tfDescricao;
    
    private Stage dialogStage;
    private boolean btnSalvarClicked = false;
    private Modelo modelo;  
    private final MarcaDAO marcaDAO = new MarcaDAO();
    private List<Marca> marcas = new ArrayList<>();
    private ObservableList<Marca> obsMarcas;
    private List<ECategoria> categorias;
    private ObservableList<ECategoria> obsCategorias;
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

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
        tfDescricao.setText(modelo.getDescricao());
        cbMarcaDB.getSelectionModel().select(modelo.getMarca());
        chbCategoria.setValue(modelo.getCategoria());
        spPotencia.getValueFactory().setValue(modelo.getMotor().getPotencia());
        chbTipoCombustivel.setValue(modelo.getMotor().getCombustivel()); 
    }

    @FXML
    void handleBtnModeloDialogSalvar(ActionEvent event) {
         if (validarEntradaDeDados()) {
            modelo.setDescricao(tfDescricao.getText());
            modelo.setMarca(cbMarcaDB.getSelectionModel().getSelectedItem());
            modelo.setCategoria(chbCategoria.getValue());
            modelo.getMotor().setPotencia(spPotencia.getValue());
            modelo.getMotor().setCombustivel(chbTipoCombustivel.getValue());

            btnSalvarClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    void handleBtnModeloDialogCancelar(ActionEvent event) {
        dialogStage.close();
    }
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marcaDAO.setConnection(connection);
        carregarMarcas();
        carregarTipoCombustivel();
        carregarSpinnerPotencia();
        carregarCategoria();
    } 
    
    public void carregarMarcas(){
        marcas = marcaDAO.listar();
        obsMarcas = FXCollections.observableArrayList(marcas);
        
        cbMarcaDB.setItems(obsMarcas);
    }
    
    public void carregarTipoCombustivel(){
        chbTipoCombustivel.setItems(FXCollections.observableArrayList(ETipoCombustivel.values()));
    }
    
    public void carregarCategoria(){
        chbCategoria.setItems(FXCollections.observableArrayList(ECategoria.values()));
    }
    
    public void carregarSpinnerPotencia(){
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 150,1);
        
        spPotencia.setValueFactory(valueFactory);
        
    }
    
    private boolean validarEntradaDeDados() {
        String errorMessage = "";
        
        if (tfDescricao.getText() == null || tfDescricao.getText().isEmpty()) {
            errorMessage += "Descrição inválida!\n";
        }
        
        if (cbMarcaDB.getSelectionModel().getSelectedItem() == null) {
            errorMessage += "Selecione uma marca!\n";
        }
        
        if (chbCategoria.getValue() == null) {
            errorMessage += "Categoria inválida!\n";
        }
        
        if (spPotencia.getValue() == 0) {
            errorMessage += "Potência inválida!\n";
        }
        
        if (chbTipoCombustivel.getValue() == null) {
            errorMessage += "Combustível inválido!\n";
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
