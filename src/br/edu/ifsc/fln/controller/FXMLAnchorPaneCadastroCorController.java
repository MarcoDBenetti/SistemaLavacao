/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.CorDAO;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.domain.Cor;
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
public class FXMLAnchorPaneCadastroCorController implements Initializable {
     @FXML
    private AnchorPane anchorPaneInformacoes;
    
    @FXML
    private Button btnCadastrosCorAlterar;

    @FXML
    private Button btnCadastrosCorExcluir;

    @FXML
    private Button btnCadastrosCorInserir;

    @FXML
    private Label lbInformacoesCor;

    @FXML
    private Label lbId;

    @FXML
    private Label lbIdDB;

    @FXML
    private Label lbNome;

    @FXML
    private Label lbNomeDB;
    
    @FXML
    private TableColumn<Cor, String> tableColumnCor;

    @FXML
    private TableView<Cor> tableViewCadastrosCor;
    
    private List<Cor> listaCor;
    private ObservableList<Cor> observableListCor;
    
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final CorDAO corDAO = new CorDAO();

    @FXML
    void handleBtnCadastrosCorAlterar(ActionEvent event) throws IOException {
        Cor cor = tableViewCadastrosCor.getSelectionModel().getSelectedItem();
        if (cor != null) {
            boolean btnSalvarClicked = showFXMLAnchorPaneCadastroCorDialog(cor);
            if (btnSalvarClicked) {
                corDAO.alterar(cor);
                carregarTableViewCor();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cor na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    void handleBtnCadastrosCorExcluir(ActionEvent event) {
        Cor cor = tableViewCadastrosCor.getSelectionModel().getSelectedItem();
        if (cor != null) {
            corDAO.remover(cor);
            carregarTableViewCor();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Cor na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    void handleBtnCadastrosCorInserir(ActionEvent event) throws IOException {
        Cor cor = new Cor();
        boolean btnSalvarClicked = showFXMLAnchorPaneCadastroCorDialog(cor);
        if (btnSalvarClicked) {
            corDAO.inserir(cor);
            carregarTableViewCor();
        } 
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        corDAO.setConnection(connection);
        carregarTableViewCor();

        tableViewCadastrosCor.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue)
                        -> selecionarItemTableViewCor(newValue));
    }    
    
    public void carregarTableViewCor() {
        tableColumnCor.setCellValueFactory( new PropertyValueFactory<>("Nome"));

        listaCor = corDAO.listar();

        observableListCor = FXCollections.observableArrayList(listaCor);
        tableViewCadastrosCor.setItems(observableListCor);
    }

    public void selecionarItemTableViewCor(Cor cor) {
        if (cor != null) {
            lbIdDB.setText(String.valueOf(cor.getId()));
            lbNomeDB.setText(cor.getNome());
        } else {
            lbIdDB.setText("");
            lbNomeDB.setText("");
        }

    }
    
    private boolean showFXMLAnchorPaneCadastroCorDialog(Cor cor) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroCorController.class.getResource("../view/FXMLAnchorPaneCadastroCorDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Cor");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        FXMLAnchorPaneCadastroCorDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCor(cor);
        
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();
        
        return controller.isBtnSalvarClicked();
    }
}
