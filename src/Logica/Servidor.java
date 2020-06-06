package Logica;
import java.io.*;
import static java.lang.System.exit;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Servidor{

    public Servidor(){}

    public String mezclar(String messageStr, String password){

        StringBuilder message =  new StringBuilder(messageStr);

        for(int i = 0; i < password.length(); i++)
            message.insert((i * 42) + password.charAt(i), password.charAt(i));
        message.insert(0, password.length());

        return message.toString();
    }

    public void darAlta(String user, String password, String nombreArchivo) throws IOException{

        File archivo = new File(nombreArchivo);
        FileWriter fw = new FileWriter(archivo, true);

        fw.write(user);
        fw.write(", ");
        fw.write(password);
        fw.write("\n");
        System.out.println("Se agrego: " + user + " " + password);

        fw.close();
    }

    private void agregarContacto(String usuario, String contacto) throws FileNotFoundException, IOException{

        RandomAccessFile archivo = new RandomAccessFile("C:/Users/Pedro-PC/Documents/NetBeansProjects/Parcial#3/bd.txt", "rw");
        String auxiliar, in[], cargaArchivo = "";
        long posicion;

        if(verificarUsuario(contacto) != null || verificarUsuario(usuario)!= null){
            System.out.println("Usuario no existe");
            return;
        }
        while((auxiliar = archivo.readLine()) != null){
            in = auxiliar.split(",");
            if(in[0].equalsIgnoreCase(usuario)){
                //aumento lo necesario
                posicion = archivo.getFilePointer() - 1;
                archivo.seek(posicion);
                while((auxiliar = archivo.readLine()) != null){
                    if(auxiliar.equalsIgnoreCase("-" + contacto)){
                        System.out.println("El contacto ya existe");
                        return;
                    }
                    cargaArchivo += auxiliar;
                    cargaArchivo += "\n";
                }
                archivo.seek(posicion);
                archivo.writeBytes("-" + contacto + cargaArchivo);
                return;
            }
        }
    }

    public ArrayList<String> obtenerContactos(String usuario) throws FileNotFoundException, IOException{

        String[] in;
        String auxiliar;
        ArrayList<String> contactos = new ArrayList<String>();
        BufferedReader bf = new BufferedReader(new FileReader("C:/Users/Pedro-PC/Documents/NetBeansProjects/Parcial#3/bd.txt"));

        if(bf.ready() == false)
            System.out.println("El archivo esta vacio");
        while((auxiliar = bf.readLine()) != null){
            in = auxiliar.split(",");
            System.out.println("in[0]: " + in[0] + " buscar: " + usuario);
            if(in[0].equalsIgnoreCase(usuario)){
                auxiliar = bf.readLine();
                while(auxiliar.contains("-")){
                    contactos.add(auxiliar.substring(1).trim());
                    auxiliar = bf.readLine();
                }
                return contactos;
            }
        }
        return null;
    }

    public void eliminarContactos(String usuario, String contacto/*, ArrayList<String> contactos*/) throws FileNotFoundException, IOException{

        long posicion;
        String[] in;
        String auxiliar, cargaArchivo = "";
        RandomAccessFile archivo = new RandomAccessFile("C:/Users/Pedro-PC/Documents/NetBeansProjects/Parcial#3/bd.txt", "rw");

        while((auxiliar = archivo.readLine()) != null){
            in = auxiliar.split(",");
            System.out.println("in[0]: " + in[0] + " buscar: " + usuario);
            if(in[0].equalsIgnoreCase(usuario)){
                while((auxiliar = archivo.readLine()) != null){
                    System.out.println("aux: " + auxiliar + " con " + contacto);
                    if(auxiliar.equalsIgnoreCase("-" + contacto)){
                        posicion = archivo.getFilePointer() - contacto.length() - 2;
                        while((auxiliar = archivo.readLine()) != null){
                            cargaArchivo += auxiliar;
                            cargaArchivo += "\n";
                        }
                        archivo.seek(posicion);
                        archivo.writeBytes(cargaArchivo);
                        return;
                    }
                }
            }
        }
    }

    public String verificarUsuario(String nombreBuscar)throws IOException {

        String[] in;
        String auxiliar;
        BufferedReader bf = new BufferedReader(new FileReader("C:/Users/Pedro-PC/Documents/NetBeansProjects/Parcial#3/bd.txt"));

        if(bf.ready() == false)
            System.out.println("El archivo esta vacio");
        while((auxiliar = bf.readLine()) != null){
            in = auxiliar.split(",");
            System.out.println("in[0]: " + in[0] + " buscar: " + nombreBuscar);
            if(in[0].equalsIgnoreCase(nombreBuscar)){
                System.out.println("Envia: " + in[1]);
                return in[1];
            }
        }

        return "";
    }

    public String mensajeAleatorio(){

        String mensajeAleatorio = "";
        Random random = new Random();

        for(int i = 0; i < 2048; i++)
            mensajeAleatorio += (char)random.nextInt(130) + 33;

        return mensajeAleatorio;
    }

    private static void servidor(String nombreArchivo) throws IOException{

        ServerSocket ss = null;
        Socket s = null;
        //ArrayList<Socket> clientes = new ArrayList();

        try {
            ss = new ServerSocket(9999);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("Escuchando en el puerto 9999: " + ss);

        while(true){
            s = ss.accept();
            /*if(!clientes.contains(s))
                clientes.add(s);*/
            System.out.println("Nueva conexion aceptada: " + s);
            new GestorPeticion(s, nombreArchivo).start();
            s = null;
        }

    }

    public static void main(String[] args) throws IOException {

        servidor("C:/Users/Pedro-PC/Documents/NetBeansProjects/Parcial#3/" + args[0]);

    }
}


class GestorPeticion extends Thread {

    private Md5 md5;
    private Socket s;
    private String nombreArchivo;
    private Servidor servidor;
    private int opcion;

    public GestorPeticion(Socket s, String nombreArchivo){

        this.s = s;
        this.nombreArchivo = nombreArchivo;
        this.servidor = new Servidor();
        this.opcion = 0;
        this.md5 = new Md5();
    }


    public void run(){
        /*El diccionario de usuarios está aquí*/
        BufferedReader entrada;
        PrintWriter salida;
        Cifrado cifrado = new Cifrado();
        Map<String, Integer> usuarios = new HashMap();
        int opcion, ex = 0;
        String datos[] = new String[2], mensajeCombinado, aleatorio, finalMd5, clienteMd5;
        String contraUsuario;
        ArrayList<String> contactos;
        String clave = "_#::==:/$$$%%%//=/%:&:[fgdg][hjjuuyrf]adwd>>###VVV-V###>>>ghghghg///&&,&";

        try{
            entrada = new BufferedReader(new InputStreamReader(s.getInputStream()));
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);

            while(ex != -1){
                opcion = Integer.parseInt(cifrado.descifrar(entrada.readLine(), clave));
                switch(opcion){
                    case 0:
                        System.out.println("Registro");
                        while(true){
                            datos[0] = cifrado.descifrar(entrada.readLine(), clave);
                            datos[1] = cifrado.descifrar(entrada.readLine(), clave);
                            if(servidor.verificarUsuario(datos[0]) == null){
                                servidor.darAlta(datos[0], datos[1], nombreArchivo);
                                salida.println(cifrado.cifrar("Agregado", clave));
                                System.out.println("Dado de alta");
                                break;
                            } else{
                                salida.println(cifrado.cifrar("Ya existe", clave));
                                System.out.println("Usuario existente");
                                break;
                            }
                        }
                    break;
                    case 1:
                        System.out.println("Login");
                        while(true){
                            //Recibe nombre de usuario
                            datos[0] = cifrado.descifrar(entrada.readLine(), clave);
                            System.out.println("Datos recibidos: " + datos[0]);
                            contraUsuario = servidor.verificarUsuario(datos[0]);
                            System.out.println("Datos: " + datos[0] + " " + datos[1]);
                            if(!contraUsuario.isEmpty()){
                                usuarios.put(datos[0], s.getPort());
                                contraUsuario = contraUsuario.substring(1, contraUsuario.length());
                                aleatorio = servidor.mensajeAleatorio();
                                mensajeCombinado = servidor.mezclar(aleatorio, contraUsuario);
                                salida.println(cifrado.cifrar("1", clave));
                                salida.println(aleatorio);
                                //Llama MD5
                                finalMd5 = md5.algoritmoMd5(mensajeCombinado);
                                clienteMd5 = entrada.readLine();
                                if(finalMd5.equalsIgnoreCase(clienteMd5)){
                                    salida.println(cifrado.cifrar("9", clave));
                                    salida.println(cifrado.cifrar("\n****TIENE ACCESO****", clave));
                                    System.out.println("Acceso permitido");
                                    //salida.close();
                                    //entrada.close();
                                    //exit(1);
                                    //Carga contactos
                                    contactos = servidor.obtenerContactos(datos[0]);
                                    if(contactos.isEmpty()){
                                        System.out.println("Sin contactos");
                                        exit(1);
                                    }
                                    for(int i = 0; i < contactos.size(); i++)
                                        salida.println(contactos.get(i));
                                    salida.println();
                                    //Continúa para chat
                                } else{
                                    System.out.println("Entra a no final");
                                    salida.println(cifrado.cifrar("9", clave));
                                    salida.println(cifrado.cifrar("\n****NO TIENE ACCESO****", clave));
                                    System.out.println("Acceso denegado");
                                    salida.close();
                                    entrada.close();
                                    exit(1);
                                }
                                //Continúa para Chat
                            } else{
                                salida.println(cifrado.cifrar("-1", clave));
                                break;
                            }
                        }
                        break;
                    case -1:
                        System.out.println("Saliendo de servidor");
                        ex = -1;
                        exit(1);
                    default:
                        break;
                }
                break;
            }
            salida.close();
            entrada.close();
        }catch(Exception e){
            e.printStackTrace();
            System.exit(-1);

        }
    }
}