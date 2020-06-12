package Vista;

import Controlador.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class VentanaContactos extends JFrame{

    private ControladorGrafico controladorGrafico;
    private JPanel pBase;
    private JButton bAgregarContacto;
    private ArrayList<JButton> bConversar;
    private ArrayList<JLabel> lContactos;
    private final ImageIcon agregarContactos = new ImageIcon(getClass().getResource("/Recursos/mas.png"));
    private final ImageIcon mensaje = new ImageIcon(getClass().getResource("/Recursos/mensaje.png"));
    private final Font fuenteNombres = new Font("Comic sans MS", Font.PLAIN, 20);
    private final Color LightBlue = new Color(173, 216, 230);

    public VentanaContactos(ArrayList<String> nombresContactos){
        super("Contactos");
        this.setSize(400, 550);
        this.setResizable(false);
        Image iconoPropio = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Recursos/usuarios-contactos.png"));
        setIconImage(iconoPropio);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        initComponentes(nombresContactos);
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent eve) {
                controladorGrafico.mostrarLogin();
                controladorGrafico.indicadorLogin(1);
                cerrarContactos(0);
            }
        });
    }

    public ControladorGrafico getControladorGrafico(){
        return controladorGrafico;
    }

    public void setControladorGrafico(ControladorGrafico controladorGrafico){
        this.controladorGrafico = controladorGrafico;
    }

    public final void initComponentes(ArrayList<String> nombresContactos){

        bAgregarContacto = new JButton();
        bAgregarContacto.setSize(45, 45);
        bAgregarContacto.setIcon(agregarContactos);
        bAgregarContacto.setBackground(LightBlue);
        bAgregarContacto.setBorder(null);
        bAgregarContacto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                botones_actionPerformed(e);
            }
        });
        if(!nombresContactos.isEmpty()){
            int i, x1 = 0, x2 = 355, y = 0;
            bConversar = new ArrayList<>();
            lContactos = new ArrayList<>();
            for(i = 0; i < nombresContactos.size(); i++){
                JLabel nombre = new JLabel();
                nombre.setFont(fuenteNombres);
                nombre.setText(nombresContactos.get(i));
                nombre.setSize(355, 45);
                nombre.setLocation(x1,y);
                nombre.setBackground(LightBlue);
                lContactos.add(nombre);

                JButton b = new JButton();
                b.setSize(45, 45);
                b.setIcon(mensaje);
                b.setLocation(x2,y);
                b.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        botones_actionPerformed(e);
                    }
                });
                b.setBackground(LightBlue);
                b.setBorder(null);
                bConversar.add(b);

                y += 47;
            }

            bAgregarContacto.setLocation(x2,y);

            pBase = new JPanel();
            pBase.setLayout(null);
            pBase.setBackground(LightBlue);
            for(i = 0 ; i < nombresContactos.size(); i ++){
                pBase.add(lContactos.get(i));
                pBase.add(bConversar.get(i));
            }
                pBase.add(bAgregarContacto);
        }else{
            JLabel nombre = new JLabel();
            nombre.setFont(fuenteNombres);
            nombre.setText("No tiene contactos");
            nombre.setSize(355, 45);
            nombre.setLocation(0,0);
            nombre.setBackground(LightBlue);

            bAgregarContacto.setLocation(355,0);

            pBase = new JPanel();
            pBase.setLayout(null);
            pBase.setBackground(LightBlue);
            pBase.add(nombre);
            pBase.add(bAgregarContacto);

        }
        add(pBase);
    }

    public void botones_actionPerformed(ActionEvent e) {
      Object boton=e.getSource();
      if(boton.equals(bAgregarContacto)){
          controladorGrafico.mostrarUsuarios();
          cerrarContactos(1);
      }else{
        for(int i = 0; i < bConversar.size(); i++){
          if (boton.equals(bConversar.get(i))){
           controladorGrafico.indicadorLogin(2);
           controladorGrafico.mostrarChat(lContactos.get(i).getText());
           cerrarContactos(1);
          }
        }
      }
    }

    public void cerrarContactos(int opcion){
        if(opcion == 0)
            this.dispose();
        if(opcion == 1)
            this.setVisible(false);
    }

    public void mostrarContactos(){
          this.setVisible(true);
          controladorGrafico.indicadorLogin(5);
    }
}
