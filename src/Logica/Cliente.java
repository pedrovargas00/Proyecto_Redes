package Logica;
import Controlador.ControladorGrafico;
import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Cliente{

    private String usuario;
    private String contrasenia;
    private int login;
    private ArrayList<String> contactos;
    private String contacto;
    private String mensajeEntrante;
    private String mensajeSaliente;
    private ControladorGrafico controlador;
    private ArrayList<String> usuarios;
    private Cifrado cifrado;
    private Md5 md5;

    public Cliente(){

        this.contactos = new ArrayList();
        this.usuarios = new ArrayList();
        this.usuario = "";
        this.contrasenia = "";
        this.contacto = "";
        this.login = -1;
        this.mensajeEntrante = "";
        this.mensajeSaliente = "";
        this.cifrado = new Cifrado();
        this.md5 = new Md5();
    }

    public void setControlador(ControladorGrafico controlador){

        this.controlador = controlador;
    }

    public String getUsuario() {

        return usuario;
    }

    public void setUsuario(String usuario) {

        this.usuario = usuario;
        System.out.println("Usuario_" + usuario);
    }

    public String getContrasenia() {

        return contrasenia;
    }
    
    public void setContacto(String contacto){
        
        this.contacto = contacto;
    }
    
    public void setContrasenia(String contrasenia) {

        this.contrasenia = contrasenia;
        System.out.println("Con: " + contrasenia);
    }

    public int getLogin() {

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

    public ArrayList<String> getUsuarios(){

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

    public void menu() throws Exception {

      while(getLogin() != -2){
            //System.out.print("MENU\n1.- Registro\n2.- Login\n0.- Salir.\n--> ");
            //opcion = in.nextInt();
            switch(getLogin()){
                case 0:
                    registro();
                    break;
                case 1:
                    login();
                    break;
                case 2:
                    System.out.println("Chat");
                    chat();
                    break;
                case 3:
                    recibirChat();
                    break;
                default:
                    //System.out.println("Opcion inválida");
                    System.out.println("");
            }
        }
    }

    public void registro() throws Exception {

      String clave = "_#::==:/$$$%%%//=/%:&:[fgdg][hjjuuyrf]adwd>>###VVV-V###>>>ghghghg///&&,&";
      String entry;
      String datos[] = new String[2];

      try{
          Socket socket = new Socket("localhost", 9999);
          BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          PrintWriter salida = new PrintWriter( new OutputStreamWriter(socket.getOutputStream() ), true);

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
          socket.close();
      }catch(UnknownHostException e){
            e.printStackTrace();
      }catch(IOException e){
            e.printStackTrace();
      }
    }

    public void login() throws Exception {

      String clave = "_#::==:/$$$%%%//=/%:&:[fgdg][hjjuuyrf]adwd>>###VVV-V###>>>ghghghg///&&,&";
      String mensaje = "", mezcla, entry, finalMd5, contacto, usuarioT;
      String datos[] = new String[2];

      try{
          Socket socket = new Socket("localhost", 9999);
          BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
          PrintWriter salida = new PrintWriter( new OutputStreamWriter(socket.getOutputStream() ), true);

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
                      }else{
                        controlador.permitido(false);
                        setLogin(-1);
                      }
                  }
              }if(entry.equalsIgnoreCase("-1")){//No recordamos para qué se usa
                controlador.permitido(false);
                setLogin(-1);
                break;
              }
          }

          socket.close();
      } catch(UnknownHostException e){
          e.printStackTrace();
      } catch(IOException e){
          e.printStackTrace();
      }
    }

    public void chat() throws Exception{
      
        try{
            Socket socket = new Socket("localhost", 9999);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter( new OutputStreamWriter(socket.getOutputStream() ), true);
            String clave = "_#::==:/$$$%%%//=/%:&:[fgdg][hjjuuyrf]adwd>>###VVV-V###>>>ghghghg///&&,&";
            String mensaje = " ";
            
            salida.println(cifrado.cifrar("2", clave));
            salida.println(cifrado.cifrar(contacto, clave));
            while(true){
                if(!mensajeSaliente.isEmpty()){
                    salida.println(mensajeSaliente);
                    setMensajeSaliente("");
                    break;
                }
                if(!(mensaje = cifrado.descifrar(entrada.readLine(), clave)).isEmpty()){
                    System.out.println("mensaje Recibido: " + mensaje);
                    break;
                } 
            }
            socket.close();
        }catch(UnknownHostException e){
              e.printStackTrace();
        }catch(IOException e){
              e.printStackTrace();
        }
    }

    public void recibirChat()throws Exception{
        
        try{
            Socket socket = new Socket("localhost", 9999);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter( new OutputStreamWriter(socket.getOutputStream() ), true);
            String clave = "_#::==:/$$$%%%//=/%:&:[fgdg][hjjuuyrf]adwd>>###VVV-V###>>>ghghghg///&&,&";
            String mensaje = " ";
        
            controlador.mostrarChat(cifrado.descifrar(entrada.readLine(), clave));
            while(true){
                if(!mensajeSaliente.isEmpty()){
                    salida.println(mensajeSaliente);
                    setMensajeSaliente("");
                    break;
                }
                if(!(mensaje = cifrado.descifrar(entrada.readLine(), clave)).isEmpty()){
                    System.out.println("mensaje Recibido: " + mensaje);
                    break;
                } 
            }
            socket.close();
        }catch(UnknownHostException e){
              e.printStackTrace();
        }catch(IOException e){
              e.printStackTrace();
        }
    }
    /*public void cliente() throws Exception{

        int puerto = 9999;
        boolean accedio = false;
        String servidor = "localhost";
        String clave = "_#::==:/$$$%%%//=/%:&:[fgdg][hjjuuyrf]adwd>>###VVV-V###>>>ghghghg///&&,&";
        String mensaje = "", mezcla, entry, finalMd5, contacto, usuarioT;
        String datos[] = new String[2];


        try{
            Socket socket = new Socket(servidor, puerto);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter( new OutputStreamWriter(socket.getOutputStream() ), true);
            System.out.println("GetLogin: " + getLogin());
                  //while(login == -1)
                    //  System.out.print("");

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
              case -2:
              //Respaldar
                  System.out.println("Saliendo de cliente");
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
    }*/
}
