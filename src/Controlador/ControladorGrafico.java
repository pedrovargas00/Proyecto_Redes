package Controlador;
import Vista.*;
import Logica.Cliente;
import java.util.ArrayList;

public class ControladorGrafico{

    //referencia a todas las ventanas del programa
    private VentanaLogin vLogin;
    private VentanaRegistro vR;
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

    public void setvRegistro(VentanaRegistro vR){
        this.vR = vR;
    }

    public VentanaRegistro getvR(){
        return vR;
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
        this.indicadorLogin(5);
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
        vR = new VentanaRegistro();
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
                cliente.setContacto(nombreContacto);
            }
        }else{
            VentanaChat vChat = new VentanaChat(nombreContacto);
            this.setvChat(vChat);
            vChat.setControladorGrafico(this);
            vChat.setVisible(true);
            cliente.setContacto(nombreContacto);
        }
       cliente.setLogin(2);
    }

    public void actualizarContactos(){
      vU.cerrarUsuarios();
      vC = new VentanaContactos(cliente.getContactos());
      vC.setControladorGrafico(this);
      this.setvC(vC);
    }

    public void mostrarUsuarios(){
        if(this.getvU() != null)
            this.getvU().mostrarUsuarios();
        else{
          System.out.println("ventana"+cliente.getUsuarios().get(0));
            vU = new VentanaUsuarios(cliente.getUsuarios(), cliente.getContactos());
            vU.setControladorGrafico(this);
            this.setvU(vU);
            vU.setVisible(true);
        }
    }

    public void mostrarLogin(){
        vLogin.mostrarLogin();
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
        vR.registroExitoso(registrado);
    }

    public boolean esContacto(String nombre){
      for(int i=0; i<cliente.getContactos().size();i++)
        if(nombre.equals(cliente.getContactos().get(i)))
          return true;
      return false;
    }

    public void eliminarContacto(String nombre){
      cliente.setContactoModificar(nombre);
      cliente.setLogin(4);
    }

    public void agregarContacto(String nuevoContacto){
      cliente.setContactoModificar(nuevoContacto);
      cliente.setLogin(6);
    }

    public void enviarMensaje(String mensaje){
        cliente.setMensajeSaliente(mensaje);
    }

    public void recibirMensaje(String mensaje){
        vChat.setMensaje(mensaje);
    }
}
