/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.ModeloDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Modelo;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author marco
 */
public class FXMLAnchorPaneCadastroModeloController implements Initializable {
    @FXML
    private AnchorPane anchorPaneInformacoes;

    @FXML
    private Button btnCadastrosModeloExcluir;

    @FXML
    private Button btnCadastrosModeloAlterar;

    @FXML
    private Button btnCadastrosModeloInserir;

    @FXML
    private Label lbDescricaoDB;

    @FXML
    private Label lbIdDB;

    @FXML
    private Label lbInformacoesModelo;

    @FXML
    private Label lbMarcaDB;

    @FXML
    private Label lbPotenciaDB;
    
    @FXML
    private Label lbCategoriaDB;

    @FXML
    private Label lbTipoCombustivelDB;

    @FXML
    private TableColumn<Modelo, String> tableColumnModelo;

    @FXML
    private TableView<Modelo> tableViewCadastrosModelo;
    
    private List<Modelo> listaModelo;
    private ObservableList<Modelo> observableListModelo;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final ModeloDAO modeloDAO = new ModeloDAO();

    @FXML
    void handleBtnCadastrosModeloExcluir(ActionEvent event) {
        Modelo modelo = tableViewCadastrosModelo.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            modeloDAO.remover(modelo);
            carregarTableViewModelo();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Modelo na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    void handleBtnCadastrosModeloAlterar(ActionEvent event) throws IOException {
        Modelo modelo = tableViewCadastrosModelo.getSelectionModel().getSelectedItem();
        if (modelo != null) {
            boolean btnSalvarClicked = showFXMLAnchorPaneCadastroModeloDialog(modelo);
            if (btnSalvarClicked) {
                modeloDAO.alterar(modelo);
                carregarTableViewModelo();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Modelo na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    void handleBtnCadastrosModeloInserir(ActionEvent event) throws IOException {
        Modelo modelo = new Modelo();
        boolean btnSalvarClicked = showFXMLAnchorPaneCadastroModeloDialog(modelo);
        if (btnSalvarClicked) {
            modeloDAO.inserir(modelo);
            carregarTableViewModelo();
        } 
    }   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modeloDAO.setConnection(connection);
        carregarTableViewModelo();

        tableViewCadastrosModelo.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue)
                        -> selecionarItemTableViewModelo(newValue));
    }    
    
    public void carregarTableViewModelo() {
        tableColumnModelo.setCellValueFactory( new PropertyValueFactory<>("Descricao"));

        listaModelo = modeloDAO.listar();

        observableListModelo = FXCollections.observableArrayList(listaModelo);
        tableViewCadastrosModelo.setItems(observableListModelo);
    }

    public void selecionarItemTableViewModelo(Modelo modelo) {
        if (modelo != null) {
            lbIdDB.setText(String.valueOf(modelo.getId()));
            lbDescricaoDB.setText(modelo.getDescricao());
            lbMarcaDB.setText(modelo.getMarca().getNome());
            lbCategoriaDB.setText(modelo.getCategoria().getDescricao());
            lbPotenciaDB.setText(String.valueOf(modelo.getMotor().getPotencia()));
            lbTipoCombustivelDB.setText(modelo.getMotor().getCombustivel().getDescricao());
        } else {
            lbIdDB.setText("");
            lbDescricaoDB.setText("");
            lbMarcaDB.setText("");
            lbCategoriaDB.setText("");
            lbPotenciaDB.setText("");
            lbTipoCombustivelDB.setText("");
        }

    }
    
    private boolean showFXMLAnchorPaneCadastroModeloDialog(Modelo modelo) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroModeloController.class.getResource("../view/FXMLAnchorPaneCadastroModeloDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Modelo");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        FXMLAnchorPaneCadastroModeloDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setModelo(modelo);
        
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();
        
        return controller.isBtnSalvarClicked();
    }
}
