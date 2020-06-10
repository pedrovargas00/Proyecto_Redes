package Logica;

import Controlador.ControladorGrafico;
import Vista.VentanaLogin;
import javax.swing.JFrame;

public class Principal {

    public static void main(String []args) throws Exception{

        VentanaLogin login = new VentanaLogin();
        ControladorGrafico controlador = new ControladorGrafico();
        Cliente cliente = new Cliente();

        login.setVisible(true);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setControladorGrafico(controlador);
        controlador.setvLogin(login);
        controlador.setCliente(cliente);
        cliente.setControlador(controlador);
        cliente.menu();
    }
}
