/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.edu.ifsc.fln.controller;

import br.edu.ifsc.fln.model.dao.VeiculoDAO;
import br.edu.ifsc.fln.model.database.DatabaseFactory;
import br.edu.ifsc.fln.model.database.Database;
import br.edu.ifsc.fln.model.domain.Veiculo;
import java.io.IOException;
import java.sql.Connection;
import java.net.URL;
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
public class FXMLAnchorPaneCadastroVeiculoController implements Initializable {

    @FXML
    private AnchorPane anchorPaneInformacoes;

    @FXML
    private Button btnCadastroVeiculoAlterar;

    @FXML
    private Button btnCadastroVeiculoExcluir;

    @FXML
    private Button btnCadastroVeiculoInserir;

    @FXML
    private Label lbMarcaDB;

    @FXML
    private Label lbModeloDB;

    @FXML
    private Label lbCorDB;

    @FXML
    private Label lbObservacoesDB;

    @FXML
    private Label lbPlacaDB;

    @FXML
    private Label lbPotenciaDB;

    @FXML
    private Label lbTipoCombustivelDB;
    
    @FXML
    private Label lbCategoriaDB;

    @FXML
    private TableColumn<Veiculo, String> tableColumnVeiculo;

    @FXML
    private TableView<Veiculo> tableViewCadastroVeiculo;
    
    private List<Veiculo> listaVeiculo;
    private ObservableList<Veiculo> obsVeiculo;
    private final Database database = DatabaseFactory.getDatabase("mysql");
    private final Connection connection = database.conectar();
    private final VeiculoDAO veiculoDAO = new VeiculoDAO();

    @FXML
    void handleBtnCadastroVeiculoAlterar(ActionEvent event) throws IOException {
        Veiculo veiculo = tableViewCadastroVeiculo.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            boolean btnSalvarClicked = showFXMLAnchorPaneCadastroVeiculoDialog(veiculo);
            if (btnSalvarClicked) {
                veiculoDAO.alterar(veiculo);
                carregarTableViewVeiculo();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Veículo na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    void handleBtnCadastroVeiculoExcluir(ActionEvent event) {
        Veiculo veiculo = tableViewCadastroVeiculo.getSelectionModel().getSelectedItem();
        if (veiculo != null) {
            veiculoDAO.remover(veiculo);
            carregarTableViewVeiculo();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Esta operação requer a seleção \nde uma Veiculo na tabela ao lado");
            alert.show();
        }
    }

    @FXML
    void handleBtnCadastroVeiculoInserir(ActionEvent event) throws IOException {
        Veiculo veiculo = new Veiculo();
        boolean btnSalvarClicked = showFXMLAnchorPaneCadastroVeiculoDialog(veiculo);
        if (btnSalvarClicked) {
            veiculoDAO.inserir(veiculo);
            carregarTableViewVeiculo();
        } 
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        veiculoDAO.setConnection(connection);
        carregarTableViewVeiculo();

        tableViewCadastroVeiculo.getSelectionModel().selectedItemProperty().
                addListener((observable, oldValue, newValue)
                        -> selecionarItemTableViewVeiculo(newValue));
    }    
    
     public void carregarTableViewVeiculo() {
        tableColumnVeiculo.setCellValueFactory( new PropertyValueFactory<>("placa"));

        listaVeiculo = veiculoDAO.listar();

        obsVeiculo = FXCollections.observableArrayList(listaVeiculo);
        tableViewCadastroVeiculo.setItems(obsVeiculo);
    }
     
    public void selecionarItemTableViewVeiculo(Veiculo veiculo) {
        if (veiculo != null) {
            lbPlacaDB.setText(veiculo.getPlaca());
            lbObservacoesDB.setText(veiculo.getObservacoes());
            lbModeloDB.setText(veiculo.getModelo().getDescricao());
            lbMarcaDB.setText(veiculo.getModelo().getMarca().getNome());
            lbCorDB.setText(veiculo.getCor().getNome());
            lbCategoriaDB.setText(veiculo.getModelo().getCategoria().getDescricao());
            lbPotenciaDB.setText(String.valueOf(veiculo.getModelo().getMotor().getPotencia()));
            lbTipoCombustivelDB.setText(veiculo.getModelo().getMotor().getCombustivel().getDescricao());
        } else {
            lbPlacaDB.setText("");
            lbObservacoesDB.setText("");
            lbModeloDB.setText("");
            lbMarcaDB.setText("");
            lbCorDB.setText("");
            lbCategoriaDB.setText("");
            lbPotenciaDB.setText("");
            lbTipoCombustivelDB.setText("");
        }
    }
        private boolean showFXMLAnchorPaneCadastroVeiculoDialog(Veiculo veiculo) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroVeiculoController.class.getResource("../view/FXMLAnchorPaneCadastroVeiculoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Veiculo");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        FXMLAnchorPaneCadastroVeiculoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setVeiculo(veiculo);
        
        //apresenta o diálogo e aguarda a confirmação do usuário
        dialogStage.showAndWait();
        
        return controller.isBtnSalvarClicked();
    }
}
