package Vista;

import Controlador.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;


public final class VentanaChat extends JFrame{

    private ControladorGrafico controladorGrafico;
    private JPanel pBase, pMensajes;
    private JScrollPane pEmisor, pReceptor, spMensajes;
    private JTextField jtMensaje;
    private JTextArea jtEmisor, jtReceptor;
    private JButton bEnviar;
    private final ImageIcon enviarMensaje = new ImageIcon("src/Recursos/enviar.png");
    private final Color LightBlue = new Color(173, 216, 230);
    private final Color CadetBlue = new Color(95, 158, 160);
    private final Font fuenteChat = new Font("Comic sans MS", Font.PLAIN, 20);
    private int saltos= 0;
        
    public VentanaChat(String nombreContacto){
        super(nombreContacto);
        this.setSize(400, 550);
        this.setResizable(false);
        Image iconoPropio = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Recursos/contacto.png"));
        setIconImage(iconoPropio);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        initComponentes();
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                cerrarChat();
                controladorGrafico.mostrarContactos();
            }
        });
    }

    public ControladorGrafico getControladorGrafico() {
        return controladorGrafico;
    }

    public void setControladorGrafico(ControladorGrafico controladorGrafico) {
        this.controladorGrafico = controladorGrafico;
    }
       
    public void initComponentes(){

        bEnviar = new JButton();
        bEnviar.setIcon(enviarMensaje);
        bEnviar.setSize(50, 50);
        bEnviar.setLocation(350, 475);//350, 475
        bEnviar.setBackground(CadetBlue);
        bEnviar.setBorder(null);

        jtMensaje = new JTextField();
        jtMensaje.setSize(350, 50);
        jtMensaje.setLocation(0, 475);
        jtMensaje.setBackground(CadetBlue);

        jtEmisor = new JTextArea();
        jtEmisor.setLineWrap(true);
        jtEmisor.setEditable(false);
        jtEmisor.setBackground(LightBlue);
        jtEmisor.setFont(fuenteChat);
        jtEmisor.setBorder(null);
        jtEmisor.setSize(200, 475);

        jtReceptor = new JTextArea();
        jtReceptor.setLineWrap(true);
        jtReceptor.setEditable(false);
        jtReceptor.setBackground(LightBlue);
        jtReceptor.setFont(fuenteChat);
        jtReceptor.setBorder(null);
        jtReceptor.setSize(200, 475);

        pMensajes = new JPanel(new BorderLayout());
        pMensajes.add(jtReceptor, BorderLayout. WEST);
        pMensajes.add(jtEmisor, BorderLayout.EAST);
        pMensajes.setBackground(LightBlue);

        spMensajes = new JScrollPane(pMensajes);
        spMensajes.setSize(400, 475);
        spMensajes.setLocation(0, 0);
        spMensajes.setBorder(null);
        spMensajes.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);

        pBase = new JPanel();
        pBase.setBackground(LightBlue);            
        pBase.setLayout(null);
        pBase.add(bEnviar);
        pBase.add(jtMensaje);
        pBase.add(spMensajes);

        add(pBase);
        
        bEnviar.addActionListener((ActionEvent e) -> {
            String mensaje = jtMensaje.getText();
            jtMensaje.setText("");
            if(mensaje != null){
                String x = "";
                for(int i = 0; i < saltos; i++)
                    x += "\n";
                if(mensaje.length() < 20){
                    jtEmisor.append(x + mensaje);           
                    saltos = 1;
                }else{
                    jtEmisor.append(x);
                    saltos = 0;
                    String aux = mensaje;
                    while(aux.length() > 19){
                        jtEmisor.append(aux.substring(0,18)+ "\n");
                        saltos++;
                        aux = aux.substring(18);
                    }
                    jtEmisor.append(aux);
                    saltos++;
                }
                setMensaje("Feet, don’t fail me now Take me to the finish line Oh, my heart, it breaks every step that I take But I’m hoping at the gates, they’ll tell me that you’re mine");
                //Aquí se envía el mensaje al servidor para que lo ponga en el jtext del contacto
            }
        });
    }

    public void setMensaje(String mensaje){
        String x = "";
        for(int i = 0; i < saltos; i++)
            x += "\n";
        if(mensaje.length() < 20){
            jtReceptor.append(x +mensaje);           
            saltos = 1;
        }
        else{
            jtReceptor.append(x);
            saltos = 0;
            String aux = mensaje;
            while(aux.length() > 19){
                jtReceptor.append(aux.substring(0,18)+ "\n");
                saltos++;
                aux = aux.substring(18);
            }
            jtReceptor.append(aux);
            saltos++;
        }
    }
    
    public void cerrarChat(){
           this.dispose();
    }
    public void mostrarChat(){
          this.setVisible(true);
    }
}
