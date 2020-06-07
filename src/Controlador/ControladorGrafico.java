package Controlador;
import Vista.*;
import Logica.Cliente;
import java.util.ArrayList;

public class ControladorGrafico{

    //referencia a todas las ventanas del programa
    private VentanaLogin vLogin;
    private VentanaRegistro vRegistro;
    private VentanaContactos vC;
    private VentanaUsuarios vU;
    private VentanaChat vChat;
    private Cliente cliente;

    public ControladorGrafico(){}

    public VentanaLogin getvLogin() {
        return vLogin;
    }

    public void setCliente(Cliente cliente){
        this.cliente = cliente;
    }

    public Cliente getCliente(){
        return cliente;
    }

    public void setvRegistro(VentanaRegistro vRegistro){
        this.vRegistro = vRegistro;
    }

    public VentanaRegistro getvRegistro(){
        return vRegistro;
    }

    public void setvLogin(VentanaLogin vLogin) {
        this.vLogin = vLogin;
    }

    public VentanaContactos getvC() {
        return vC;
    }

    public void setvC(VentanaContactos vC) {
        this.vC = vC;
    }

    public VentanaUsuarios getvU() {
        return vU;
    }

    public void setvU(VentanaUsuarios vU) {
        this.vU = vU;
    }

    public VentanaChat getvChat() {
        return vChat;
    }

    public void setvChat(VentanaChat vChat) {
        this.vChat = vChat;
    }

    public void permitido(boolean tieneAcceso){
        this.getvLogin().mostrarContactos(tieneAcceso);
    }

    public void mostrarContactos(){
        if(this.getvC()!= null)
            vC.mostrarContactos();
        else{

            vC = new VentanaContactos(cliente.getContactos());
            vC.setControladorGrafico(this);
            this.setvC(vC);
            vC.setVisible(true);
        }
    }

    public void mostrarRegistro(){
        VentanaRegistro vR = new VentanaRegistro();
        vR.setControladorGrafico(this);
        vR.setVisible(true);
    }

    public void mostrarChat(String nombreContacto){
        if(this.getvChat() != null){
            if(this.getvChat().getTitle().equals(nombreContacto))
                this.getvChat().mostrarChat();
            else{
                VentanaChat vChat = new VentanaChat(nombreContacto);
                this.setvChat(vChat);
                vChat.setControladorGrafico(this);
                vChat.setVisible(true);
            }
        }else{
            VentanaChat vChat = new VentanaChat(nombreContacto);
            this.setvChat(vChat);
            vChat.setControladorGrafico(this);
            vChat.setVisible(true);
        }
    }

    public void mostrarUsuarios(){
        if(this.getvU() != null)
            this.getvU().mostrarUsuarios();
        else{
            ArrayList<String> a = new ArrayList<>();
            for(int i = 0; i < 11; i++)
                a.add("User  -> " + (i+1));
            vU = new VentanaUsuarios(a);
            vU.setControladorGrafico(this);
            this.setvU(vU);
            vU.setVisible(true);
        }
    }

    public void mostrarLogin(){
        vLogin.mostrarLogin();
    }

    public boolean existeUsuario(String usuario){
        System.out.println("Usuario: " + usuario );
    //return modelo.existeUsuraio(usuario)
      return false;
    }

    public void indicadorLogin(int login){

        cliente.setLogin(login);
    }

    public void datosLogin(String usuario, String contrasenia){

        cliente.setUsuario(usuario);
        cliente.setContrasenia(contrasenia);
    }

    public void datosRegistro(String usuario, String contrasenia){
        cliente.setUsuario(usuario);
        cliente.setContrasenia(contrasenia);
    }

    public void registro(boolean registrado){
      vRegistro.registroExitoso(registrado);
    }
}
