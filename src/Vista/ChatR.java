package Vista;

import Controlador.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import javax.swing.JFrame;


public class ChatR {

    public static void main(String[] args) throws FileNotFoundException{
              
        //el Controlador que va a manejar todo
        ControladorGrafico c = new ControladorGrafico();
        
        //instancia a la vista principal
        VentanaLogin log = new VentanaLogin();
        log.setVisible(true);
        log.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        log.setControladorGrafico(c);
        
        c.setvLogin(log);
    }
    
}
