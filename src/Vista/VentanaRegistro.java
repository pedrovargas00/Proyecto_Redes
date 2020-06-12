package Vista;

import Controlador.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class VentanaRegistro extends JFrame{

    private ControladorGrafico controladorGrafico;
    private JPanel pBase;
    private JButton bRegistrar;
    private JTextField jtUsuario;
    private JPasswordField jtContrasena;
    private JLabel jlUsuario, jlContrasena, jlImagen, jlIndicaciones;
    private final Font fuente = new Font("Comic sans MS", Font.PLAIN, 20);
    private final ImageIcon registrarUsuario = new ImageIcon(getClass().getResource("/Recursos/registrar2.png"));
    private final ImageIcon exitoso = new ImageIcon(getClass().getResource("/Recursos/chek.png"));
    private final Color LightBlue = new Color(173, 216, 230);
    private final Color CadetBlue = new Color(95, 158, 160);

    public VentanaRegistro(){
        super("Registro");
        this.setSize(400, 550);
        this.setResizable(false);
        Image iconoPropio = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Recursos/registrar1.png"));
        setIconImage(iconoPropio);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        initComponentes();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e){
                controladorGrafico.mostrarLogin();
                controladorGrafico.indicadorLogin(1);
                cerrarRegistro();
            }
        });
    }

    public ControladorGrafico getControladorGrafico(){
        return controladorGrafico;
    }

    public void setControladorGrafico(ControladorGrafico controladorGrafico){
        this.controladorGrafico = controladorGrafico;
    }

    public final void initComponentes(){

        bRegistrar = new JButton("Registrar");
        bRegistrar.setBackground(LightBlue);
        bRegistrar.setSize(150, 40);
        bRegistrar.setFont(fuente);
        bRegistrar.setLocation(125, 420);

        jlUsuario = new JLabel("Ingrese un nombre de Usuario");
        jlUsuario.setBackground(LightBlue);
        jlUsuario.setFont(fuente);
        jlUsuario.setSize(290, 28);
        jlUsuario.setLocation(62, 224);

        jtUsuario = new JTextField();
        jtUsuario.setBackground(CadetBlue);
        jtUsuario.setSize(250, 28);
        jtUsuario.setLocation(72, 252);
//        jtUsuario.setBorder(null);

        jlContrasena = new JLabel("Ingrese una clave segura");
        jlContrasena.setBackground(LightBlue);
        jlContrasena.setFont(fuente);
        jlContrasena.setSize(290, 28);
        jlContrasena.setLocation(62, 300);

        jtContrasena = new JPasswordField();
        jtContrasena.setBackground(CadetBlue);
        jtContrasena.setSize(250, 28);
        jtContrasena.setLocation(72, 328);
    //    jtContrasena.setBorder(null);

        jlImagen = new JLabel();
        jlImagen.setBackground(LightBlue);
        jlImagen.setSize(150, 150);
        jlImagen.setIcon(registrarUsuario);
        jlImagen.setLocation(135,60);

        jlIndicaciones = new JLabel("Alta de usuarios");
        jlIndicaciones.setBackground(LightBlue);
        jlIndicaciones.setFont(fuente);
        jlIndicaciones.setSize(150, 28);
        jlIndicaciones.setLocation(125,25);

        pBase = new JPanel();
        pBase.setBackground(LightBlue);
        pBase.setLayout(null);
        pBase.add(jlIndicaciones);
        pBase.add(jlImagen);
        pBase.add(jlUsuario);
        pBase.add(jtUsuario);
        pBase.add(jtContrasena);
        pBase.add(jlContrasena);
        pBase.add(bRegistrar);

        add(pBase);

        bRegistrar.addActionListener((ActionEvent e) -> {
            char[] arrayC = jtContrasena.getPassword();
            String pass = new String(arrayC);
            String usuario = jtUsuario.getText();
            if(usuario.length() != 0 && pass.length() != 0){
                controladorGrafico.datosRegistro(usuario, pass);
                controladorGrafico.indicadorLogin(0);
            }
        });
    }

    public String getUsuario(){

        return jtUsuario.getText();
    }

    public String getPassword(){

        return String.valueOf(jtContrasena.getPassword());
    }

    public void cerrarRegistro(){
            this.dispose();
    }

    public void mostrarRegistro(){
          this.setVisible(true);
    }

    public void registroExitoso(boolean exito){

      if(exito){
        JOptionPane.showMessageDialog(null, "Successful ", "", JOptionPane.DEFAULT_OPTION, exitoso);
        controladorGrafico.mostrarLogin();
        controladorGrafico.indicadorLogin(1);
        cerrarRegistro();
      }else
        JOptionPane.showMessageDialog(null, "user already exists, try a different username", "", JOptionPane.ERROR_MESSAGE);
    }
}
