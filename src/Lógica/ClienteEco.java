package Lógica;

import Controlador.*;
import Vista.VentanaLogin;
import java.net.*;
import java.io.*;
import javax.swing.JFrame;
import java.util.ArrayList;

public class ClienteEco {
    
    private String usuario;
    private String contrasenia;
    private int login;
    private ArrayList<ClienteEco> contactos;
    private ControladorGrafico controlador;
    
    public ClienteEco(){}
    
    public void setControlador(ControladorGrafico controlador){
        
        this.controlador = controlador;
    }
    
    public void setUsuario(String usuario){
        
        this.usuario = usuario;
    }
    
    public String getUsuario(){
        
        return usuario;
    }
    
    public void setContrasenia(String contrasenia){
        
        this.contrasenia = contrasenia;
    }
    
    public String getContrasenia(){
        
        return contrasenia;
    }

    public int getLogin(){
        
        return login;
    }

    public void setLogin(int login){
        
        this.login = login;
    }

    public ArrayList<ClienteEco> getContactos(){
        
        return contactos;
    }

    public void setContactos(ArrayList<ClienteEco> contactos){
        
        this.contactos = contactos;
    }
    
    public String mezclar(String messageStr, String password){
        
        StringBuilder message =  new StringBuilder(messageStr);
        
        System.out.println("Length messageByte[] " + message.length());
        System.out.println("Length passwordByte[] " + password.length() + " " + password);
        
        for(int i = 0; i < password.length(); i++){
            message.insert(i * 42, password.charAt(i));
            System.out.println("Lon: " + i * 42 + " " + message.charAt(i * 42) + " " + password.charAt(i));
        }
        message.insert(1, password.length());
        System.out.println("Len: " + message.length());
        
        return message.toString();
    }
    
    private static void cliente(int opcion) throws Exception{
        
        //se obtiene el servidor
        //String servidor = args[0];
        //se obtiene el puerto de conexion
        //int puerto = Integer.parseInt(args[1]);
        String servidor = "localhost";
        int puerto = 9999;
        String mensaje = "", mezcla;
        String datos[] = new String[2], entry;
        String clave = "_#::==:/$$$%%%//=/%:&:[fgdg][hjjuuyrf]adwd>>###VVV-V###>>>ghghghg///&&,&";
        Cifrado cifrado = new Cifrado();
        
        try{
            Socket socket = new Socket(servidor, puerto);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter( new OutputStreamWriter(socket.getOutputStream() ) ,true);
            
            /*switch(opcion){
                
                case 0:
                    System.out.println("Registro");
                    datos = ingresoDatos();
                    System.out.println("Datos: " + datos[0] + " " + datos[1]);
                    //salida.println("0");
                    salida.println(cifrado.cifrar("0", clave));
                    //salida.println(datos[0]);
                    //salida.println(datos[1]);
                    salida.println(cifrado.cifrar(datos[0], clave));
                    salida.println(cifrado.cifrar(datos[1], clave));
                    while(true){
                        entry = cifrado.descifrar(entrada.readLine(), clave);
                        System.out.println("Entry: " + entry);
                        if(entry.equalsIgnoreCase("Ya existe")){
                            System.out.println("El usuario ya existe");
                            break;
                        }
                        if(entry.equalsIgnoreCase("Agregado")){
                            System.out.println("Usuario agregado exitosamente");
                            break;
                        }
                    }
                    break;
                case 1:
                    System.out.println("Login");
                    datos = ingresoDatos();
                    //salida.println("1");
                    salida.println(cifrado.cifrar("1", clave));
                    //salida.println(datos[0]);
                    salida.println(cifrado.cifrar(datos[0], clave));
                    salida.println(cifrado.cifrar(datos[1], clave));
                    System.out.println("Datos: " + datos[0] + " " + datos[1]);
                    while(true){
                        entry = cifrado.descifrar(entrada.readLine(), clave);
                        System.out.println("Entry: " + entry);
                        if(entry.equalsIgnoreCase("1")){
                            mensaje = entrada.readLine();
                            System.out.println("Mensaje: " + mensaje + " " + mensaje.length());
                            mezcla = mezclar(mensaje, datos[1]);
                            //salida.println("3");
                            System.out.println("Longitud mezcla: " + mezcla.length());
                            //salida.println(mezcla);
                            //salida.println(cifrado.cifrar(mezcla, clave));
                            System.out.println("El usuario ya existe");
                            break;
                        }
                        if(entry.equalsIgnoreCase("-1")){
                            System.out.println("El usuario no existe");
                            break;
                        }
                    }
                    break;
                case -1:
                    System.out.println("Saliendo de cliente");
                    //salida.println("-1");
                    salida.println(cifrado.cifrar("-1", clave));
                    break;
                default:
                    break;
                    
            }*/
            socket.close();
        }
        catch(UnknownHostException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        
    }
    
    public static void main(String[] args) throws Exception {
        
        ControladorGrafico controlador = new ControladorGrafico();
        VentanaLogin login = new VentanaLogin();
        
        login.setVisible(true);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setControladorGrafico(controlador);
        controlador.setvLogin(login);
    }
}
