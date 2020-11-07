import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Window extends JFrame {
    private final JFileChooser fileChooser;
    private final JButton code, decode;
    private final JTextField strField, keyField;

    public Window() {
        super("Lab 3");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLocation(100, 100);
        setSize(1000, 900);

        JPanel grid = new JPanel();
        GridLayout layout = new GridLayout(0, 3, 12, 12);
        grid.setLayout(layout);

        FileFilter fileFilter = new FileNameExtensionFilter("DOCX, DOC", "docx", "doc");

        fileChooser = new JFileChooser();
        code = new JButton("Закодировать");
        decode = new JButton("Декодировать");

        fileChooser.setFileFilter(fileFilter);
        fileChooser.addChoosableFileFilter(fileFilter);

        strField = new JTextField("Текст", 10);
        keyField = new JTextField("Код", 10);

        grid.add(fileChooser);
        grid.add(code);
        grid.add(decode);

        grid.add(strField);
        grid.add(keyField);

        getContentPane().add(grid);

        aHandler handler = new aHandler();
        code.addActionListener(handler);
        decode.addActionListener(handler);
        fileChooser.addActionListener(handler);

        setVisible(true);
    }

    public class aHandler implements ActionListener {
        Algorithm algorithm = Algorithm.getInstance();
        String str = null;
        String key = null;

        @Override
        public void actionPerformed(ActionEvent e) {
            str = strField.getText();
            key = keyField.getText();
            try {
                if (fileChooser.equals(e.getSource())) {
                    algorithm.setFile(fileChooser.getSelectedFile());
                } else if (code.equals(e.getSource())) {
                    keyField.setText(algorithm.code(str));
                } else if (decode.equals(e.getSource())) {
                    strField.setText(algorithm.decode(key));
                } else {
                    throw new IOException("Произошла неизвестная ошибка");
                }
            }
            catch (IOException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
            catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Неправильный текст");
            }
        }
    }
}
