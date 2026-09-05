import javax.swing.*;
import java.awt.*;

public class GUIEditorTexto extends JFrame {
    private JTextPane areaTexto;
    private JComboBox<String> Fuentes;
    private JComboBox<Integer> Tamanos;
    private JToggleButton btnBOLD, btnITALIC, btnSUBRAYAR, btnTACHAR;


    public GUIEditorTexto() {
        setTitle("Procesador de Texto .EDT");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }
}
