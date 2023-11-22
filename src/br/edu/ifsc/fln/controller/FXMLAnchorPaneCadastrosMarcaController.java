/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.MarcaDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Marca;
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
public class FXMLAnchorPaneCadastrosMarcaController implements Initializable {
     @FXML
    private AnchorPane anchorPaneInformacoes;
    
    @FXML
    private Button btnCadastrosMarcaAlterar;

    @FXML
    private Button btnCadastrosMarcaxcluir;

    @FXML
    private Button btnCadastrosMarcaInserir;

    @FXML
    private Label lbInformacoesMarca;

    @FXML
    private Label lbId;

    @FXML
    private Label lbIdDB;

    @FXML
    private Label lbNome;

    @FXML
    private Label lbNomeDB;
    
    @FXML
    private TableColumn<Marca, String> tableColumnMarca;

    @FXML
    private TableView<Marca> tableViewCadastrosMarca;
    
    private List<Marca> listaMarca;
    private ObservableList<Marca> observableListMarca;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final MarcaDAO marcaDAO = new MarcaDAO();

    @FXML
    void handleBtnCadastrosMarcaAlterar(ActionEvent event) throws IOException {
        Marca marca = tableViewCadastrosMarca.getSelectionModel().getSelectedItem();
        if (marca != null) {
            boolean btnSalvarClicked = showFXMLAnchorPaneCadastrosMarcaDialog(marca);
            if (btnSalvarClicked) {
                marcaDAO.alterar(marca);
                carregarTableViewMarca();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Marca na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    void handleBtnCadastrosMarcaExcluir(ActionEvent event) {
        Marca marca = tableViewCadastrosMarca.getSelectionModel().getSelectedItem();
        if (marca != null) {
            marcaDAO.remover(marca);
            carregarTableViewMarca();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Marca na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    void handleBtnCadastrosMarcaInserir(ActionEvent event) throws IOException {
        Marca marca = new Marca();
        boolean btnSalvarClicked = showFXMLAnchorPaneCadastrosMarcaDialog(marca);
        if (btnSalvarClicked) {
            marcaDAO.inserir(marca);
            carregarTableViewMarca();
        } 
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        marcaDAO.setConnection(connection);
        carregarTableViewMarca();

        tableViewCadastrosMarca.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue)
                        -> selecionarItemTableViewMarca(newValue));
    }    
    
    public void carregarTableViewMarca() {
        tableColumnMarca.setCellValueFactory( new PropertyValueFactory<>("Nome"));

        listaMarca = marcaDAO.listar();

        observableListMarca = FXCollections.observableArrayList(listaMarca);
        tableViewCadastrosMarca.setItems(observableListMarca);
    }

    public void selecionarItemTableViewMarca(Marca marca) {
        if (marca != null) {
            lbIdDB.setText(String.valueOf(marca.getId()));
            lbNomeDB.setText(marca.getNome());
        } else {
            lbIdDB.setText("");
            lbNomeDB.setText("");
        }

    }
    
    private boolean showFXMLAnchorPaneCadastrosMarcaDialog(Marca marca) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastrosMarcaController.class.getResource("../view/FXMLAnchorPaneCadastrosMarcaDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Marca");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        FXMLAnchorPaneCadastrosMarcaDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setMarca(marca);
        
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();
        
        return controller.isBtnSalvarClicked();
    }
}
