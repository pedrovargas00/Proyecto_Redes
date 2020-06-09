package Logica;
import Controlador.ControladorGrafico;
import Vista.VentanaLogin;
import java.util.Scanner;
import java.net.*;
import java.io.*;
import static java.lang.System.exit;
import java.util.ArrayList;
import javax.swing.JFrame;

public class Cliente{

    private static String usuario;
    private static String contrasenia;
    private static int login;
    private static ArrayList<String> contactos;
    private String mensajeEntrante;
    private String mensajeSaliente;
    private ControladorGrafico controlador;
    private static ArrayList<String> usuarios;
    public Cliente(){

        this.contactos = new ArrayList();
        this.usuarios = new ArrayList();
        this.usuario = "";
        this.contrasenia = "";
        this.login = -1;
        this.mensajeEntrante = "";
        this.mensajeSaliente = "";
    }

    public void setControlador(ControladorGrafico controlador){

        this.controlador = controlador;
    }

    public static String getUsuario() {

        return usuario;
    }

    public void setUsuario(String usuario) {

        this.usuario = usuario;
        System.out.println("Usuario_" + usuario);
    }

    public static String getContrasenia() {

        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {

        this.contrasenia = contrasenia;
        System.out.println("Con: " + contrasenia);
    }

    public static int getLogin() {

        return login;
    }

    public void setLogin(int login) {

        this.login = login;
    }

    public ArrayList<String> getContactos() {

        return contactos;
    }

    public void setContactos(ArrayList<String> contactos) {

        this.contactos = contactos;
    }

    public static ArrayList<String> getUsuarios(){

      return usuarios;
    }

    public String getMensajeEntrante() {

        return mensajeEntrante;
    }

    public void setMensajeEntrante(String mensajeEntrante) {

        this.mensajeEntrante = mensajeEntrante;
    }

    public String getMensajeSaliente() {

        return mensajeSaliente;
    }

    public void setMensajeSaliente(String mensajeSaliente) {

        this.mensajeSaliente = mensajeSaliente;
    }

    private static String mezclar(String messageStr, String password){

        StringBuilder message =  new StringBuilder(messageStr);
        for(int i = 0; i < password.length(); i++)
            message.insert((i * 42) + password.charAt(i), password.charAt(i));

        message.insert(0, password.length());
        return message.toString();
    }

    public void cliente() throws Exception{

        int puerto = 9999;
        boolean accedio = false;
        String servidor = "localhost";
        String clave = "_#::==:/$$$%%%//=/%:&:[fgdg][hjjuuyrf]adwd>>###VVV-V###>>>ghghghg///&&,&";
        String mensaje = "", mezcla, entry, finalMd5, contacto, usuarioT;
        String datos[] = new String[2];
        Cifrado cifrado = new Cifrado();
        Md5 md5 = new Md5();

        try{
            Socket socket = new Socket(servidor, puerto);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter( new OutputStreamWriter(socket.getOutputStream() ) ,true);
            System.out.println("GetLogin: " + getLogin());

        while(!accedio){
          while(login == -1)
              System.out.print("");

          System.out.println("--GetLogin: " + getLogin());

          switch(getLogin()){

              case 0:
                  System.out.println("\nRegistro");
                  datos[0] = getUsuario();
                  datos[1] = getContrasenia();
                  System.out.println("--Datos: " + datos[0] + " " + datos[1]);
                  salida.println(cifrado.cifrar("0", clave));
                  salida.println(cifrado.cifrar(datos[0], clave));
                  salida.println(cifrado.cifrar(datos[1], clave));
                  while(true){
                      entry = cifrado.descifrar(entrada.readLine(), clave);
                      if(entry.equalsIgnoreCase("Ya existe")){
                        controlador.registro(false);
                        System.out.println("***EL USUARIO YA EXISTE***");
                        setLogin(-1);
                        break;
                      } if(entry.equalsIgnoreCase("Agregado")){
                        System.out.println("***USUARIO AGREGADO***");
                        controlador.registro(true);
                        setLogin(-1);
                        break;
                      }
                  }
                  break;

              case 1:
                  System.out.println("\nLogin");
                  datos[0] = getUsuario();
                  datos[1] = getContrasenia();
                  System.out.println("Datos: " + datos[0] + " " + datos[1]);
                  salida.println(cifrado.cifrar("1", clave));
                  salida.println(cifrado.cifrar(datos[0], clave));
                  while(true){
                      entry = cifrado.descifrar(entrada.readLine(), clave);
                      if(entry.equalsIgnoreCase("1")){
                          mensaje = entrada.readLine();
                          mezcla = mezclar(mensaje, datos[1]);
                          finalMd5 = md5.algoritmoMd5(mezcla);
                          salida.println(finalMd5);
                          if(cifrado.descifrar(entrada.readLine(), clave).equalsIgnoreCase("9")){
                              if(cifrado.descifrar(entrada.readLine(), clave).equalsIgnoreCase("\n****TIENE ACCESO****")){
                                  System.out.println("Acceso permitido");

                                  while ((usuarioT = entrada.readLine())!= null)
                                      if(!usuarioT.equalsIgnoreCase("-1"))
                                        getUsuarios().add(usuarioT);
                                      else
                                        break;

                                  while((contacto = entrada.readLine()) != null)
                                      if(!contacto.equalsIgnoreCase("-1"))
                                          getContactos().add(contacto);
                                      else
                                          break;
                                  controlador.permitido(true);
                                  accedio = true;
                              }else{
                                controlador.permitido(false);
                                setLogin(-1);
                              }
                          }
                      }
                      if(entry.equalsIgnoreCase("-1")){//No recordamos para qué se usa
                          controlador.permitido(false);
                          setLogin(-1);
                          break;
                      }
                  }
                  break;
              case -1:
              //Respaldar
                  System.out.println("Saliendo de cliente");
                  salida.println(cifrado.cifrar("-1", clave));
                  break;
              default:
                  break;
          }
        }
            socket.close();
        }
        catch(UnknownHostException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
