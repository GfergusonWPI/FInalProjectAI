import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    public ImageView ShowImage;
    @FXML
    public void checkClick(ActionEvent event){
        System.out.println("hi");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        String name = file.getName();
        //System.out.println(file.getAbsolutePath());
        //System.out.println(file.getName());

        if (name.substring(name.length()-4).equals(".wav")){
            fileName.setText(file.getName());
            MainTests.getSpectroFromFile(file.getAbsolutePath(), file.getName());

            File test = new File("Spectrograms\\Full-"+name+".png");
            Image image = new Image(test.toURI().toString());
            ShowImage.setImage(image);
        }
        else{
            fileName.setText("Please choose a .wav file");
        }
    }

    @FXML
        public void sendInFile(File selectedFile){

        }
}
