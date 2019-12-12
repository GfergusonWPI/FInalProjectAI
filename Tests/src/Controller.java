import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {
    public FileChooser fileChoose = new FileChooser();
    @FXML
    public Text fileName;
    @FXML
    public Button a;
    @FXML
    public void checkClick(ActionEvent event){
        System.out.println("hi");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        System.out.println(file.getName());
        fileName.setText(file.getName());

    }
    @FXML
        public void sendInFile(File selectedFile){

        }
}
