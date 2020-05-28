package Lógica;

import Controlador.*;
import java.io.*;
import static java.lang.System.exit;
import java.net.*;
import java.util.ArrayList;

public class ServidorEco {
    
    public ServidorEco(){}
    
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
    
    public void darAlta(String user, String password, String nombreArchivo) throws IOException{
        
        String str;
        File archivo = new File(nombreArchivo);
        FileWriter fw = new FileWriter(archivo, true);
        
        //fw.write("\n");
        fw.write(user);
        fw.write(",");
        fw.write(password);
        fw.write("\n");
        System.out.println("Se agrego: " + user + " " + password);
        
       fw.close();
    }
    
    private void agregarContacto(String usuario, String contacto) throws FileNotFoundException, IOException{
    
        RandomAccessFile archivo = new RandomAccessFile("bd.txt", "rw");
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
    
    public static ArrayList<String> obtenerContactos(String usuario) throws FileNotFoundException, IOException{
        
        String[] in;
        String auxiliar;
        ArrayList<String> contactos = new ArrayList<String>();
        BufferedReader bf = new BufferedReader(new FileReader("bd.txt"));

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
    
    public static void eliminarContactos(String usuario, String contacto/*, ArrayList<String> contactos*/) throws FileNotFoundException, IOException{
        
        long posicion;
        String[] in;
        String auxiliar, cargaArchivo = "";
        RandomAccessFile archivo = new RandomAccessFile("bd.txt", "rw");

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
        BufferedReader bf = new BufferedReader(new FileReader("bd.txt"));

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

        return null;
      }
    
    public String mensajeAleatorio(){
        
        String mensajeAleatorio = "";
        
        for(int i = 0; i < 2048; i++)
            mensajeAleatorio += Character.toString((char)((Math.random() * 255) + 1));
        System.out.println("Mensaje: " + mensajeAleatorio);
        
        return mensajeAleatorio;
    }
    
    private static void servidor(String nombreArchivo) throws IOException{
    
        ServerSocket ss = null;
        Socket s = null;

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
            System.out.println("Nueva conexion aceptada: " + s);
            new GestorPeticion(s, nombreArchivo).start();
            s = null;
        }
    }
    
    public static void main(String[] args) throws IOException {
        
        servidor(args[0]);

    }
}


class GestorPeticion extends Thread {
    
    private Socket s;
    private String nombreArchivo;
    private ServidorEco servidor;
    private int opcion;
    
    public GestorPeticion(Socket s, String nombreArchivo){
        
        this.s = s;
        this.nombreArchivo = nombreArchivo;
        this.servidor = new ServidorEco();
        this.opcion = 0;
    }


    public void run(){ 
        
        System.out.println("Entra a run");
        BufferedReader entrada;
        PrintWriter salida;
        Cifrado cifrado = new Cifrado();
        int opcion, exit = 0;
        String datos[] = new String[2], mensajeCombinado, aleatorio;
        String clave = "_#::==:/$$$%%%//=/%:&:[fgdg][hjjuuyrf]adwd>>###VVV-V###>>>ghghghg///&&,&";
        
        try{
            entrada = new BufferedReader(new InputStreamReader(s.getInputStream()));
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
            
            
            /*while(exit != -1){  
                opcion = Integer.parseInt(cifrado.descifrar(entrada.readLine(), clave));
                System.out.println("Recibo opc: " + opcion);
                switch(opcion){
                    case 0:
                        System.out.println("Registro");
                        while(true){
                            datos[0] = cifrado.descifrar(entrada.readLine(), clave);
                            datos[1] = cifrado.descifrar(entrada.readLine(), clave);
                            System.out.println("Datos: " + datos[0] + " " + datos[1]);
                            if(servidor.buscar(datos[0]) == null){
                                servidor.darAlta(datos[0], datos[1], nombreArchivo);
                                salida.println(cifrado.cifrar("Agregado", clave));
                                System.out.println("Dado de alta");
                                break;
                            } else{
                                salida.println(cifrado.cifrar("Ya existe", clave));
                                break;
                            }
                        }
                    break;
                    case 1:
                        System.out.println("Login");
                        while(true){
                            datos[0] = cifrado.descifrar(entrada.readLine(), clave);
                            datos[1] = cifrado.descifrar(entrada.readLine(), clave);
                            System.out.println("Datos: " + datos[0]);
                            if(servidor.buscar(datos[0]) != null){
                                aleatorio = servidor.mensajeAleatorio();
                                mensajeCombinado = servidor.mezclar(aleatorio, datos[1]);
                                System.out.println("Aleatorio: " + aleatorio.length());
                                salida.println(cifrado.cifrar("1", clave));
                                salida.println(aleatorio);
                                //mensajeCombinado = cifrado.descifrar(entrada.readLine(), clave);
                                System.out.println("Mensaje Combinado: " + mensajeCombinado);
                                //Llamar MD5
                                break;
                            } else{
                                salida.println(cifrado.cifrar("-1", clave));
                                break;
                            }
                        }
                    break;
                    case -1:
                        System.out.println("Saliendo de servidor");
                        exit = -1;
                        exit(1);
                    default:
                        break;    
                }
                break;
            }*/
            System.out.println("---------------");
            salida.close();
            entrada.close();
        }catch(Exception e){
            e.printStackTrace();
            System.exit(-1);
        
        }
    }
}
