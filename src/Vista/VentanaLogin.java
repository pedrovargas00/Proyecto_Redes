package Vista;

import Controlador.*;
import java.awt.Image;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.*;

public class VentanaLogin extends JFrame  implements MouseListener{

    private ControladorGrafico controladorGrafico;
    private ImageIcon loginImage;
    private JLabel passwordLabel, userLabel, loginIcon, registro;
    private JPanel image;
    private JTextField userText;
    private JPasswordField passwordText;
    private JButton login;
    private final Font fuente = new Font("Comic sans MS", Font.PLAIN, 20);
    private final Color LightBlue = new Color(173, 216, 230);
    private final Color CadetBlue = new Color(95, 158, 160);

    public VentanaLogin(){

        super("Login");
        this.setSize(400, 550);
        this.setResizable(false);
        Image iconoPropio = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Recursos/contacto.png"));
        setIconImage(iconoPropio);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        initComponents();
    }

    public ControladorGrafico getControladorGrafico(){
        return controladorGrafico;
    }

    public void setControladorGrafico(ControladorGrafico controladorGrafico){
        this.controladorGrafico = controladorGrafico;
    }

    public final void initComponents(){
        userLabel = new JLabel("Usuario");
        userLabel.setBackground(LightBlue);
        userLabel.setFont(fuente);
        userLabel.setBounds(155, 210, 80, 25);

        passwordLabel = new JLabel("  Clave  ");
        passwordLabel.setBackground(LightBlue);
        passwordLabel.setFont(fuente);
        passwordLabel.setBounds(140, 300, 150, 25);

        userText = new JTextField("", 20);
        userText.setBackground(CadetBlue);
        userText.setBounds(95, 260, 200, 20);

        passwordText = new JPasswordField("");
        passwordText.setBackground(CadetBlue);
        passwordText.setBounds(95, 340, 200, 20);

        login = new JButton("Ingresar");
        login.setBackground(LightBlue);
        login.setFont(fuente);
        login.setBounds(120, 380, 150, 40);

        registro = new JLabel("Registrarse");
        registro.setBackground(LightBlue);
        Font font = registro.getFont();
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        registro.setFont(font.deriveFont(attributes));
        registro.setBounds(160, 460, 150, 25);
        registro.addMouseListener(this);

        loginIcon = new JLabel();
        loginIcon.setIcon(loginImage);
        loginIcon.setIcon(new ImageIcon(getClass().getResource("/Recursos/Ingresar.png")));
        loginIcon.setBounds(130, 50, 150, 150);
        loginIcon.setVisible(true);

        getContentPane().add(login);
        getContentPane().add(loginIcon);
        getContentPane().add(userLabel);
        getContentPane().add(userText);
        getContentPane().add(passwordLabel);
        getContentPane().add(passwordText);
        getContentPane().add(registro);
        getContentPane().setLayout(null);
        getContentPane().setBackground(LightBlue);

        login.addActionListener((ActionEvent e) -> {
            //Aquí le debe avisar al controlador para que desde el modelo le mande el arreglo correspondiente de contactos
            //mientas le pongo uno
            char[] arrayC = passwordText.getPassword();
            String pass = new String(arrayC);
            String usuario = userText.getText();
            passwordText.setText("");
            userText.setText("");
            if(usuario.length() != 0 && pass.length() != 0){
                controladorGrafico.datosLogin(usuario, pass);
                controladorGrafico.indicadorLogin(1);
                //controladorGrafico.mostrarContactos();
            }
        });
    }

    public void mostrarContactos(boolean mostrar){
          if(mostrar){
            cerrarLogin();
            controladorGrafico.mostrarContactos();
          }else
            JOptionPane.showMessageDialog(null, "access denied", "", JOptionPane.ERROR_MESSAGE);

    }

    public String getUsuario(){

        return userText.getText();
    }

    public String getPassword(){

        return String.valueOf(passwordText.getPassword());
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if (me.getSource() == registro){
               passwordText.setText("");
               userText.setText("");
               controladorGrafico.mostrarRegistro();
               cerrarLogin();
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

    public void cerrarLogin(){
            this.setVisible(false);
    }
    public void mostrarLogin(){
          this.setVisible(true);
    }
}
