package Vista;

import Controlador.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class VentanaUsuarios extends JFrame implements MouseListener {

    private ControladorGrafico controladorGrafico;
    private JPanel pBase;
    private ArrayList<JButton> bAgregar;
    private ArrayList<JLabel> lNombres;
    private final ImageIcon agregar = new ImageIcon(getClass().getResource("/Recursos/agregarContacto.png"));
    private final ImageIcon agregado = new ImageIcon(getClass().getResource("/Recursos/chek.png"));
    private final Font fuenteNombres = new Font("Comic sans MS", Font.PLAIN, 20);
    private final Color LightBlue = new Color(173, 216, 230);

    public VentanaUsuarios(ArrayList<String> nombresUsuarios){
        super("Usuarios");
        this.setSize(400, 550);
        this.setResizable(false);
        Image iconoPropio = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Recursos/usuarios-contactos.png"));
        setIconImage(iconoPropio);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        initComponentes(nombresUsuarios);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                controladorGrafico.mostrarContactos();
                cerrarUsuarios();
            }
        });
    }

    public ControladorGrafico getControladorGrafico() {
        return controladorGrafico;
    }

    public void setControladorGrafico(ControladorGrafico controladorGrafico) {
        this.controladorGrafico = controladorGrafico;
    }

    public final void initComponentes(ArrayList<String> nombresUsuarios){

        if(!nombresUsuarios.isEmpty()){
            bAgregar = new ArrayList<>();
            lNombres = new ArrayList<>();
            int i = 0, x1 = 0, x2 = 355, y = 0;

            for(i = 0; i < 11; i++){
                JLabel nombre = new JLabel();
                nombre.setFont(fuenteNombres);
                nombre.setText(nombresUsuarios.get(i));
                nombre.setSize(355, 45);
                nombre.setLocation(x1,y);
                nombre.setBackground(LightBlue);
                lNombres.add(nombre);

                JButton b = new JButton();
                b.setSize(45, 45);
                b.setIcon(agregar);
                b.setLocation(x2,y);
                b.addMouseListener(this);
                b.setBackground(LightBlue);
                b.setBorder(null);
                bAgregar.add(b);

                y += 47;
            }

            pBase = new JPanel();
            pBase.setLayout(null);
            pBase.setBackground(LightBlue);
            for(i = 0 ; i < 11; i ++){
                pBase.add(lNombres.get(i));
                pBase.add(bAgregar.get(i));
            }
            add(pBase);
    }
   }

    @Override
    public void mouseClicked(MouseEvent evento) {
        for(int i = 0; i < bAgregar.size(); i++)
            if (evento.getSource() == bAgregar.get(i)){
                //se agrega al array de contactos el usuario en la posición i
                bAgregar.get(i).setIcon(agregado);
                bAgregar.get(i).setEnabled(false);
            }
    }

    @Override
    public void mousePressed(MouseEvent me) {}

    @Override
    public void mouseReleased(MouseEvent me) {}


    @Override
    public void mouseExited(MouseEvent me) {}

    @Override
    public void mouseEntered(MouseEvent me) {}

    public void cerrarUsuarios(){
           this.setVisible(false);
    }
    public void mostrarUsuarios(){
          this.setVisible(true);
    }
}
