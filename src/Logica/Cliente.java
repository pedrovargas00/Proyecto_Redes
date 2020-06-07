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

    public Cliente(){
        
        this.contactos = new ArrayList();
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

        //se obtiene el servidor
        //String servidor = args[0];
        //se obtiene el puerto de conexion
        //int puerto = Integer.parseInt(args[1]);
        String servidor = "localhost";
        int puerto = 9999;
        String mensaje = "", mezcla, entry, finalMd5, contacto;
        String datos[] = new String[2];
        String clave = "_#::==:/$$$%%%//=/%:&:[fgdg][hjjuuyrf]adwd>>###VVV-V###>>>ghghghg///&&,&";
        Cifrado cifrado = new Cifrado();
        Md5 md5 = new Md5();

        try{
            Socket socket = new Socket(servidor, puerto);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter( new OutputStreamWriter(socket.getOutputStream() ) ,true);
            System.out.println("GetLogin: " + getLogin());
            while(login == -1)
                System.out.print("");
            
            System.out.println("--GetLogin: " + getLogin());
            switch(getLogin()){

                case 0:
                    System.out.println("\nRegistro");
                    //while(getUsuario().isEmpty()){System.out.println("Datos: " + datos[0] + " " + datos[1]);}
                    datos[0] = getUsuario();
                    datos[1] = getContrasenia();
                    System.out.println("--Datos: " + datos[0] + " " + datos[1]);
                    salida.println(cifrado.cifrar("0", clave));
                    salida.println(cifrado.cifrar(datos[0], clave));
                    salida.println(cifrado.cifrar(datos[1], clave));
                    while(true){
                        entry = cifrado.descifrar(entrada.readLine(), clave);
                        if(entry.equalsIgnoreCase("Ya existe")){
                            System.out.println("***EL USUARIO YA EXISTE***");
                            break;
                        } if(entry.equalsIgnoreCase("Agregado")){
                            System.out.println("***USUARIO AGREGADO***");
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
                                    //socket.close();
                                    //exit(1);
                                    //Continúa chat
                                    //Obtiene contactos
                                    while((contacto = entrada.readLine()) != null)
                                        if(!contacto.equalsIgnoreCase("-1"))
                                            getContactos().add(contacto);
                                        else
                                            break;
                                    System.out.println("---" + getContactos().get(1));
                                    controlador.permitido();
                                    //controlador.setPermiso(true);
                                }
                                
                            }
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

/*    public static void main(String[] args) throws Exception {

        /*Scanner in = new Scanner(System.in);
        int opcion = 5;

        while(opcion != -1){
            System.out.print("MENU\n1.- Registro\n2.- Login\n0.- Salir.\n--> ");
            opcion = in.nextInt();
            switch(opcion){
                case 1:
                    cliente(0);
                    break;
                case 2:
                    cliente(1);
                    break;
                case 0:
                    System.out.println("Saliendo");
                    cliente(-1);
                    opcion = -1;
                    break;
                default:
                    System.out.println("Opcion inválida");
            }
        }
        //cliente("Hola");
    }*/
}
