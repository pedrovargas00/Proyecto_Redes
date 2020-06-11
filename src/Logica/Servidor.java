package Logica;
import java.io.*;
import static java.lang.System.exit;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Servidor{

    private static ArrayList<GestorPeticion> clientes;
    private static String ruta;

    public Servidor(){
    }

    private static void inicializar(){

      clientes = new ArrayList();
      ruta = "C:/Users/linda/OneDrive/Escuela/Modelo de Redes/proyecto/Proyecto_Redes/bd.txt";
    }

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

        //fw.write("\n");
        fw.write(user);
        fw.write(", ");
        fw.write(password);
        fw.write("\n");
        System.out.println("Se agrego: " + user + " " + password);

        fw.close();
    }

    private void agregarContacto(String usuario, String contacto) throws FileNotFoundException, IOException{

        RandomAccessFile archivo = new RandomAccessFile(ruta, "rw");
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
        BufferedReader bf = new BufferedReader(new FileReader(ruta));

        if(bf.ready() == false)
            System.out.println("El archivo esta vacio");
        while((auxiliar = bf.readLine()) != null){
            in = auxiliar.split(",");
            if(in[0].equalsIgnoreCase(usuario)){
                while((auxiliar = bf.readLine()) != null && auxiliar.startsWith("-")){
                    System.out.println("aux: " + auxiliar);
                    contactos.add(auxiliar.substring(1).trim());
                }
                return contactos;
            }
        }
        return null;
    }
    
    public ArrayList<String> obtenerUsuarios(String usuario)throws FileNotFoundException, IOException{
      
      String[] in;
      String auxiliar;
      ArrayList<String> usuarios = new ArrayList<String>();
      BufferedReader bf = new BufferedReader(new FileReader(ruta));
      if(bf.ready() == false)
          System.out.println("El archivo esta vacio - usuarios");
      System.out.println("Listar usuarios");
      while((auxiliar = bf.readLine()) != null){
          if(!auxiliar.startsWith("-")){
            in = auxiliar.split(",");
            if(!usuario.equals(in[0]))
              usuarios.add(in[0]);
          }
      }
      return usuarios;
    }

    public void eliminarContactos(String usuario, String contacto/*, ArrayList<String> contactos*/) throws FileNotFoundException, IOException{

        long posicion;
        String[] in;
        String auxiliar, cargaArchivo = "";
        RandomAccessFile archivo = new RandomAccessFile(ruta, "rw");

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
        BufferedReader bf = new BufferedReader(new FileReader(ruta));

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
        Random random = new Random();

        for(int i = 0; i < 2048; i++)
            mensajeAleatorio += (char)random.nextInt(130) + 33;

        return mensajeAleatorio;
    }

    public GestorPeticion buscarCliente(String contactoChat){
        for(int i=0;i < clientes.size(); i++){
            if(clientes.get(i).getNombre().equals(contactoChat))
                return clientes.get(i);
        }
        return null;
    }
      private static void servidor(String nombreArchivo) throws IOException{

        ServerSocket ss = null;
        Socket s = null;
        //ArrayList<Socket> clientes = new ArrayList();

        inicializar();
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
            GestorPeticion gets = new GestorPeticion(s, nombreArchivo);
            if(!clientes.contains(gets));
                clientes.add(gets);

            System.out.println("Nueva conexion aceptada: " + s);
            gets.start();
            s = null;
        }

    }

    public static void main(String[] args) throws IOException {

        servidor("C:/Users/linda/OneDrive/Escuela/Modelo de Redes/proyecto/Proyecto_Redes/" + args[0]);

    }
}


class GestorPeticion extends Thread {

    private Md5 md5;
    private Socket s;
    private String nombreArchivo;
    private Servidor servidor;
    private int opcion;
    private BufferedReader entrada;
    private PrintWriter salida;
    private String nombreUsuario;
    public GestorPeticion(Socket s, String nombreArchivo){

        this.s = s;
        this.nombreArchivo = nombreArchivo;
        this.servidor = new Servidor();
        this.opcion = 0;
        this.md5 = new Md5();
    }
    public BufferedReader getBufferEntrada(){
        return entrada;
    }
    public PrintWriter getBufferSalida(){
        return salida;
    }
    public String getNombre(){
        return nombreUsuario;
    }
    public void run(){
        /*El diccionario de usuarios está aquí*/
        
        Cifrado cifrado = new Cifrado();
        int opcion, ex = 0;
        String datos[] = new String[2], mensajeCombinado, aleatorio, finalMd5, clienteMd5;
        String contraUsuario;
        ArrayList<String> contactos;
        ArrayList<String> usuariosT = new ArrayList<String>();
        String clave = "_#::==:/$$$%%%//=/%:&:[fgdg][hjjuuyrf]adwd>>###VVV-V###>>>ghghghg///&&,&";
        //--------------------
        String nombreContacto, nombreEntrante, entraContacto;
        Socket socketContacto;
        BufferedReader entradaContacto;
        PrintWriter salidaContacto;
        
        try{
            entrada = new BufferedReader(new InputStreamReader(s.getInputStream()));
            salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(s.getOutputStream())), true);
            
            //entrada InputStream
            //salida OutputStream
            while(ex != -1){
                opcion = Integer.parseInt(cifrado.descifrar(entrada.readLine(), clave));
                //System.out.println("Opcion: " + opcion);
                switch(opcion){
                    case 0:
                        System.out.println("Registro");
                            datos[0] = cifrado.descifrar(entrada.readLine(), clave);
                            datos[1] = cifrado.descifrar(entrada.readLine(), clave);
                            if(servidor.verificarUsuario(datos[0]) == null){
                                servidor.darAlta(datos[0], datos[1], nombreArchivo);
                                salida.println(cifrado.cifrar("Agregado", clave));
                                System.out.println("Dado de alta");
                                
                            } else{
                                salida.println(cifrado.cifrar("Ya existe", clave));
                                System.out.println("Usuario existente");
                                
                            }
                        
                    break;
                    case 1:
                        System.out.println("Login");
                            //Recibe nombre de usuario
                            datos[0] = cifrado.descifrar(entrada.readLine(), clave);
                            System.out.println("Datos recibidos: " + datos[0]);
                            contraUsuario = servidor.verificarUsuario(datos[0]);
                            System.out.println("Datos: " + datos[0] + " " + datos[1]);
                            if(contraUsuario != null){
                                nombreUsuario = datos[0];
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

                                    //Carga usuarios

                                    usuariosT = servidor.obtenerUsuarios(datos[0]);
                                    if(usuariosT.isEmpty()){
                                      System.out.println("No hay usuarios-Login");
                                      exit(1);
                                    }
                                    System.out.println("Obteniendo usuarios");
                                    for(int i = 0; i < usuariosT.size(); i++){
                                        System.out.println(usuariosT.get(i));
                                        salida.println(usuariosT.get(i));
                                    }
                                    salida.println("-1");

                                    //Carga contactos
                                    contactos = servidor.obtenerContactos(datos[0]);
                                    if(contactos != null){
                                        for(int i = 0; i < contactos.size(); i++)
                                            salida.println(contactos.get(i));
                                    }
                                    contactos.clear();
                                    salida.println("-1");
                                    //Continúa para chat
                                } else{
                                    System.out.println("Entra a no final");
                                    salida.println(cifrado.cifrar("9", clave));
                                    salida.println(cifrado.cifrar("\n****NO TIENE ACCESO****", clave));
                                    System.out.println("Acceso denegado");
                                    
                                    //salida.close();
                                    //entrada.close();
                                    //exit(1);
                                }
                                //Continúa para Chat
                            } else{
                                salida.println(cifrado.cifrar("-1", clave));
                                break;
                            }
                        break;
                        
                    case 2:
                        System.out.println("Chat");
                        //Iniciar el chat
                        //Enviar la señal al cliente
                        nombreContacto = cifrado.descifrar(entrada.readLine(), clave);
                        GestorPeticion petDestino = servidor.buscarCliente(nombreContacto);
                        entradaContacto = petDestino.getBufferEntrada();
                        salidaContacto = petDestino.getBufferSalida();
                        salidaContacto.println(cifrado.cifrar("chat", clave));
                        salidaContacto.println(cifrado.cifrar(nombreContacto, clave));
                        System.out.println("Iniciare un chat entre"+ nombreContacto+ "  "+nombreUsuario);
                        // // socketContacto = servidor.buscarPuerto(usuarios, nombreContacto);
                        // entradaContacto = new BufferedReader(new InputStreamReader(socketContacto.getInputStream()));
                        // salidaContacto = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketContacto.getOutputStream())),true);

                        while(true){
                            if(!(entraContacto = cifrado.descifrar(entradaContacto.readLine(), clave)).isEmpty()){
                                salida.println(cifrado.cifrar(entraContacto, clave));
                                if(entraContacto.equals("x"))
                                    break;
                            }
                            if(!(entraContacto = cifrado.descifrar(entrada.readLine(), clave)).isEmpty()){
                                salidaContacto.println(cifrado.cifrar(entraContacto, clave));
                                if(entraContacto.equals("x"))
                                    break;
                            }
                        }
                       break;
                    case -2:
                        
                        /*
                         Entra a ciclo
                        C1 envíe el nombre del contacto
                        Obtiene el socket
                        entrada = new BufferedReader(new InputStreamReader(sC2.getInputStream()));
                        salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(sC2.getOutputStream())), true);
                        
                         */
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


// //Chat
//
// Socket cliente;
// metodo chat(Socket cliente2){
//
//   //Abrir flujos con cliente2
//   entradaCliente1---
//   salidaCliente1---
//
//   entradaCliente2---
//   salidaCliente2---
//
// }
