package editordetexto;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;

public class GUIEditorTexto extends JFrame {
    private JTextPane areaTexto;
    private JComboBox<String> Fuentes;
    private JComboBox<Integer> Tamanos;
    private JToggleButton btnBOLD, btnITALIC, btnSUBRAYAR, btnTACHAR;
    private JLabel labelEstado;

    private final GestorFormato gestorFormato = new GestorFormato();

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

        labelEstado = new JLabel("Listo | 0 palabras");
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEstado.setBorder(BorderFactory.createEtchedBorder());
        panelEstado.add(labelEstado);
        add(panelEstado, BorderLayout.SOUTH);

        areaTexto.addCaretListener(e -> actualizarEstadosYBorde());

        setVisible(true);
    }

    private JMenuBar BarraMenu() {
        JMenuBar menu = new JMenuBar();
        JMenu menuArchivo = new JMenu("Archivo");

        JMenuItem nuevoItem = new JMenuItem("Nuevo");
        JMenuItem abrirItem = new JMenuItem("Abrir");
        JMenuItem guardarItem = new JMenuItem("Guardar");
        JMenuItem guardarComoItem = new JMenuItem("Guardar como...");

        nuevoItem.addActionListener(e -> areaTexto.setText(""));
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
        Fuentes.addActionListener(e -> {
            if (Fuentes.getSelectedItem() != null) {
                gestorFormato.aplicarFuente(areaTexto, (String) Fuentes.getSelectedItem());
            }
        });

        Integer[] arregloTamanos = {8, 9, 10, 11, 12, 14, 16, 18, 24, 36, 48, 72};
        Tamanos = new JComboBox<>(arregloTamanos);
        Tamanos.setSelectedItem(12);
        Tamanos.setMaximumSize(new Dimension(60, 30));
        Tamanos.addActionListener(e -> {
            if (Tamanos.getSelectedItem() != null) {
                gestorFormato.aplicarTamano(areaTexto, (Integer) Tamanos.getSelectedItem());
            }
        });

        btnBOLD = new JToggleButton("B");
        btnBOLD.setFont(btnBOLD.getFont().deriveFont(Font.BOLD));

        btnITALIC = new JToggleButton("I");
        btnITALIC.setFont(btnITALIC.getFont().deriveFont(Font.ITALIC));

        btnSUBRAYAR = new JToggleButton("S");
        btnTACHAR = new JToggleButton("T");

        btnBOLD.addActionListener(e -> {
            gestorFormato.aplicarNegrita(areaTexto);
            actualizarEstadosYBorde();
        });

        btnITALIC.addActionListener(e -> {
            gestorFormato.aplicarCursiva(areaTexto);
            actualizarEstadosYBorde();
        });

        btnSUBRAYAR.addActionListener(e -> {
            gestorFormato.aplicarSubrayado(areaTexto);
            actualizarEstadosYBorde();
        });

        btnTACHAR.addActionListener(e -> {
            gestorFormato.aplicarTachado(areaTexto);
            actualizarEstadosYBorde();
        });

        JButton btnColor = new JButton("Color");
        btnColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Color de fuente:", Color.BLACK);
            if (color != null) {
                gestorFormato.aplicarColor(areaTexto, color);
            }
        });

        JButton btnTabla = new JButton("Insertar Tabla");
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

    private void actualizarEstadosYBorde() {
        int inicio = areaTexto.getSelectionStart();
        int fin = areaTexto.getSelectionEnd();

        btnBOLD.setSelected(gestorFormato.esNegritaActiva(areaTexto, inicio, fin));
        btnITALIC.setSelected(gestorFormato.esCursivaActiva(areaTexto, inicio, fin));
        btnSUBRAYAR.setSelected(gestorFormato.esSubrayadoActivo(areaTexto, inicio, fin));
        btnTACHAR.setSelected(gestorFormato.esTachadoActivo(areaTexto, inicio, fin));

        StyledDocument doc = areaTexto.getStyledDocument();
        int pos = (inicio == fin) ? Math.max(0, inicio - 1) : inicio;
        AttributeSet attr = doc.getCharacterElement(pos).getAttributes();

        String fuenteActual = StyleConstants.getFontFamily(attr);
        int tamanoActual = StyleConstants.getFontSize(attr);

        if (fuenteActual != null && !fuenteActual.equals(Fuentes.getSelectedItem())) {
            Fuentes.setSelectedItem(fuenteActual);
        }

        if (tamanoActual > 0 && !Integer.valueOf(tamanoActual).equals(Tamanos.getSelectedItem())) {
            Tamanos.setSelectedItem(tamanoActual);
        }

        String texto = areaTexto.getText().trim();
        int palabras = texto.isEmpty() ? 0 : texto.split("\\s+").length;
        labelEstado.setText("Listo | " + palabras + " palabras");
    }

    private void mostrarDialogoTabla() {
        JTextField campoFilas = new JTextField("2", 5);
        JTextField campoCols = new JTextField("2", 5);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Filas:"));
        panel.add(campoFilas);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(new JLabel("Columnas:"));
        panel.add(campoCols);

        int result = JOptionPane.showConfirmDialog(this, panel, "Insertar Tabla", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int filas = Integer.parseInt(campoFilas.getText());
                int cols = Integer.parseInt(campoCols.getText());
                insertarComponenteTabla(filas, cols);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void insertarComponenteTabla(int filas, int cols) {
        JTable tabla = new JTable(filas, cols);
        tabla.setRowHeight(25);
        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setPreferredSize(new Dimension(400, Math.min(150, filas * 28 + 25)));

        areaTexto.insertComponent(scrollTabla);
    }

    private void abrirArchivo() {
        JFileChooser escogerArchivo = new JFileChooser();
        if (escogerArchivo.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        }
    }

    private void guardarArchivo() {
        JFileChooser escogerArchivo = new JFileChooser();
        if (escogerArchivo.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
        }
    }
}