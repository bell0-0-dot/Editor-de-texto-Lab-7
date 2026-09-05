package editordetexto;

import persistencia.*;
import excepciones.*;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GUIEditorTexto extends JFrame {
    private JTextPane areaTexto;
    private JComboBox<String> Fuentes;
    private JComboBox<Integer> Tamanos;
    private JToggleButton btnBOLD, btnITALIC, btnSUBRAYAR, btnTACHAR;
    private JLabel labelEstado;

    private final GestorFormato gestorFormato = new GestorFormato();
    private final EdtWrite escritorBinario = new EdtWrite();
    private final EdtRead lectorBinario = new EdtRead();

    private File archivoActual = null;

    // Colores personalizados
    private final Color AZUL_OSCURO = new Color(26, 37, 48);
    private final Color AZUL_ACCENT = new Color(44, 62, 80);

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
        panelEscribir.setBackground(new Color(220, 224, 230));
        areaTexto.setPreferredSize(new Dimension(650, 800));
        areaTexto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 185, 190)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panelEscribir.add(areaTexto);
        JScrollPane scrollenTexto = new JScrollPane(panelEscribir);
        add(scrollenTexto, BorderLayout.CENTER);

        labelEstado = new JLabel("Palabras: 0");
        labelEstado.setForeground(Color.WHITE);
        labelEstado.setFont(new Font("SansSerif", Font.BOLD, 12));

        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelEstado.setBackground(AZUL_OSCURO);
        panelEstado.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panelEstado.add(labelEstado);
        add(panelEstado, BorderLayout.SOUTH);

        areaTexto.addCaretListener(e -> actualizarEstadosYBorde());

        setVisible(true);
    }

    private JMenuBar BarraMenu() {
        JMenuBar menu = new JMenuBar();
        menu.setBackground(AZUL_OSCURO);
        menu.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));

        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setForeground(Color.WHITE);
        menuArchivo.setFont(new Font("SansSerif", Font.BOLD, 12));

        JMenuItem nuevoItem = new JMenuItem("Nuevo");
        JMenuItem abrirItem = new JMenuItem("Abrir");
        JMenuItem guardarItem = new JMenuItem("Guardar");
        JMenuItem guardarComoItem = new JMenuItem("Guardar como...");

        nuevoItem.addActionListener(e -> nuevoDocumento());
        abrirItem.addActionListener(e -> abrirArchivo());
        guardarItem.addActionListener(e -> guardarArchivo());
        guardarComoItem.addActionListener(e -> guardarComoArchivo());

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
        herramientas.setBackground(AZUL_ACCENT);
        herramientas.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel lblFuente = new JLabel("Fuente: ");
        lblFuente.setForeground(Color.WHITE);
        lblFuente.setFont(new Font("SansSerif", Font.BOLD, 12));

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

        btnBOLD = crearBotonToggle("B", Font.BOLD);
        btnITALIC = crearBotonToggle("I", Font.ITALIC);
        btnSUBRAYAR = crearBotonToggle("S", Font.PLAIN);
        btnTACHAR = crearBotonToggle("T", Font.PLAIN);

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

        JButton btnColor = crearBoton("Color");
        btnColor.addActionListener(e -> {
            Color color = JColorChooser.showDialog(this, "Color de fuente:", Color.BLACK);
            if (color != null) {
                gestorFormato.aplicarColor(areaTexto, color);
            }
        });

        JButton btnTabla = crearBoton("Insertar Tabla");
        btnTabla.addActionListener(e -> mostrarDialogoTabla());

        herramientas.add(lblFuente);
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

    private JToggleButton crearBotonToggle(String texto, int estilo) {
        JToggleButton btn = new JToggleButton(texto);
        btn.setFont(new Font("SansSerif", estilo, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(AZUL_OSCURO);
        btn.setFocusPainted(false);
        return btn;
    }

    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(Color.WHITE);
        btn.setBackground(AZUL_OSCURO);
        btn.setFocusPainted(false);
        return btn;
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
        labelEstado.setText("Palabras: " + palabras);
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

    private void nuevoDocumento() {
        areaTexto.setText("");
        archivoActual = null;
        setTitle("Editor de Texto - Grupo#4");
    }

    private void abrirArchivo() {
        JFileChooser escogerArchivo = new JFileChooser();
        if (escogerArchivo.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = escogerArchivo.getSelectedFile();
            try {
                Documento doc = lectorBinario.abrir(archivo);
                renderizarDocumento(doc);

                archivoActual = archivo;
                setTitle("Editor de Texto - " + archivoActual.getName());

                JOptionPane.showMessageDialog(this, "Archivo cargado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (ExtensionInvalidaException | ArchivoCorruptoException | ArchivoTruncadoException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error de E/S al abrir el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void guardarArchivo() {
        if (archivoActual == null) {
            guardarComoArchivo();
        } else {
            ejecutarGuardado(archivoActual);
        }
    }

    private void guardarComoArchivo() {
        JFileChooser escogerArchivo = new JFileChooser();
        if (escogerArchivo.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File archivo = escogerArchivo.getSelectedFile();
            if (!archivo.getName().toLowerCase().endsWith(Constantes.EXTENSION)) {
                archivo = new File(archivo.getAbsolutePath() + Constantes.EXTENSION);
            }
            ejecutarGuardado(archivo);
        }
    }

    private void ejecutarGuardado(File archivo) {
        try {
            Documento doc = construirDocumentoDesdeGUI();
            escritorBinario.guardar(doc, archivo);

            archivoActual = archivo;
            setTitle("Editor de Texto - " + archivoActual.getName());

            JOptionPane.showMessageDialog(this, "Documento guardado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (ExtensionInvalidaException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Extensión inválida", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Documento construirDocumentoDesdeGUI() {
        Documento doc = new Documento();
        StyledDocument styledDoc = areaTexto.getStyledDocument();
        int offset = 0;
        int length = styledDoc.getLength();

        while (offset < length) {
            Element elem = styledDoc.getCharacterElement(offset);
            int start = elem.getStartOffset();
            int end = elem.getEndOffset();
            int fragmentLength = Math.min(end, length) - offset;

            try {
                String subTexto = styledDoc.getText(offset, fragmentLength);
                AttributeSet attr = elem.getAttributes();
                FormatoTexto fmt = gestorFormato.extraerFormato(attr);

                doc.addBloque(new Fragmento(subTexto, fmt));
            } catch (BadLocationException ignored) {}

            offset += fragmentLength;
        }

        return doc;
    }

    private void renderizarDocumento(Documento doc) {
        areaTexto.setText("");
        StyledDocument styledDoc = areaTexto.getStyledDocument();

        for (Object bloque : doc.getBloques()) {
            if (bloque instanceof Fragmento) {
                Fragmento frag = (Fragmento) bloque;
                FormatoTexto fmt = frag.getFormato();

                SimpleAttributeSet attrs = new SimpleAttributeSet();
                StyleConstants.setForeground(attrs, fmt.getColor());
                StyleConstants.setBold(attrs, fmt.isNegrita());
                StyleConstants.setItalic(attrs, fmt.isCursiva());
                StyleConstants.setUnderline(attrs, fmt.isSubrayado());
                StyleConstants.setStrikeThrough(attrs, fmt.isTachado());
                StyleConstants.setFontFamily(attrs, fmt.getFuente());
                StyleConstants.setFontSize(attrs, fmt.getsize());

                try {
                    styledDoc.insertString(styledDoc.getLength(), frag.getTexto(), attrs);
                } catch (BadLocationException ignored) {}
            } else if (bloque instanceof Tabla) {
                Tabla t = (Tabla) bloque;
                insertarComponenteTabla(t.getFilas(), t.getColumnas());
            }
        }
    }
}