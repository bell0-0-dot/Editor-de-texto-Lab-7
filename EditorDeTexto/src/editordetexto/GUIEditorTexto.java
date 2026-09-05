package editordetexto;

import javax.swing.*;
import javax.swing.text.StyleConstants;
import java.awt.*;

public class GUIEditorTexto extends JFrame {
    private JTextPane areaTexto;
    private JComboBox<String> Fuentes;
    private JComboBox<Integer> Tamanos;
    private JToggleButton btnBOLD, btnITALIC, btnSUBRAYAR, btnTACHAR;


    public GUIEditorTexto() {
        setTitle("Editor de Texto - Grupo#4");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setJMenuBar(BarraMenu());
        add(BarraHerramientas(), BorderLayout.NORTH);

        areaTexto = new JTextPane();
        areaTexto.setFont(new Font("Arial", Font.PLAIN, 12));

        JPanel panelEscribir = new JPanel(new GridBagLayout());
        panelEscribir.setBackground(new Color(225, 225, 225));
        areaTexto.setPreferredSize(new Dimension(650, 800));
        areaTexto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panelEscribir.add(areaTexto);
        JScrollPane scrollenTexto = new JScrollPane(panelEscribir);
        add(scrollenTexto, BorderLayout.CENTER);

        setVisible(true);
    }

    private JMenuBar BarraMenu() {
        JMenuBar menu = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");

        JMenuItem nuevoItem = new  JMenu("Nuevo");
        JMenuItem abrirItem = new  JMenuItem("Abrir");
        JMenuItem guardarItem = new  JMenuItem("Guardar");
        JMenuItem guardarComoItem = new  JMenuItem("Guardar como...");

        abrirItem.addActionListener(e -> abrirArchivo());
        guardarItem.addActionListener(e -> guardarArchivo());

        menuArchivo.add(nuevoItem);
        menuArchivo.add(abrirItem);
        menuArchivo.add(guardarItem);
        menuArchivo.add(guardarComoItem);
        menu.add(menuArchivo);

        return menu;
    }

    private JToolBar BarraHerramientas() {
        JToolBar herramientas = new JToolBar();
        herramientas.setFloatable(false);

        String[] arregloFuentes = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        Fuentes = new JComboBox<>(arregloFuentes);
        Fuentes.setSelectedItem("Arial");
        Fuentes.setMaximumSize(new Dimension(150, 30));
        Fuentes.addActionListener(e -> aplicarFormato());

        Integer[] arreglotamanos = {8, 9, 10, 11, 12, 14, 16, 18, 24, 36, 48, 72};
        Tamanos = new JComboBox<>(arreglotamanos);
        Tamanos.setSelectedItem(12);
        Tamanos.setMaximumSize(new Dimension(60, 30));
        Tamanos.addActionListener(e -> aplicarFormato());

        btnBOLD = new JToggleButton("B");
        btnBOLD.setFont(btnBOLD.getFont().deriveFont(Font.BOLD));
        btnITALIC = new JToggleButton("I");
        btnITALIC.setFont(btnITALIC.getFont().deriveFont(Font.ITALIC));
        btnTACHAR = new JToggleButton("T");
        btnSUBRAYAR = new JToggleButton("S");

        btnBOLD.addActionListener(e -> aplicarFormato());
        btnITALIC.addActionListener(e -> aplicarFormato());
        btnSUBRAYAR.addActionListener(e -> aplicarFormato());
        btnITALIC.addActionListener(e -> aplicarFormato());

        JButton btnColor = new JButton("Color");
        btnColor.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Color de fuente:", Color.BLACK);
            if (c != null) {
                aplicarAtributo(StyleConstants.Foreground, c);
            }
        });

        JButton btnTabla = new JButton(" Insertar Tabla");
        btnTabla.addActionListener(e -> mostrarDialogoTabla());

        herramientas.add(new JLabel("Fuente: "));
        herramientas.add(Fuentes);
        herramientas.addSeparator();
        herramientas.add(Tamanos);
        herramientas.addSeparator();
        herramientas.add(btnBOLD);
        herramientas.add(btnITALIC);
        herramientas.add(btnSUBRAYAR);
        herramientas.add(btnTACHAR);
        herramientas.addSeparator();
        herramientas.add(btnColor);
        herramientas.addSeparator();
        herramientas.add(btnTabla);

        return herramientas;
    }

    private void aplicarFormato(){}

    private void aplicarAtributo(Object clave, Object valor){}

    private void insertarComponenteTabla(int filas, int cols){}

    private void mostrarDialogoTabla(){}

    private void abrirArchivo(){}

    private void guardarArchivo(){}


}
