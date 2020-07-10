import javax.swing.*;
import java.awt.event.ComponentAdapter;

public class PostToGetWindow {
    private JPanel rootPanel;
    private JLabel urlDesc;
    private JTextField inputUrl;
    private JTextArea inputRequestParams;
    private JTextArea formatResult;
    private JPanel buttonLayout;
    private JButton formatParams;
    private JButton btnGetResult;

    public static void main(String[] args) {
        JFrame frame = new JFrame("销售易专用小工具");
        frame.setContentPane(new PostToGetWindow().rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public PostToGetWindow() {
        rootPanel.addComponentListener(new ComponentAdapter() {
            
        });
    }
}
