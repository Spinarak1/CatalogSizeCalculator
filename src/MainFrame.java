import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MainFrame extends JFrame{
    JTextField textField;
    JButton loadButton;
    JButton chooseDirectoryButton;
    JButton calculateSizeButton;
    JFileChooser fileChooser;
    JCheckBox checkBox;
    String previousText;
    Label calculatedSizeLabel;
    Thread calculatingThread;
    SizeCalculatingRunnable calculatingRunnable;
    Timer timer;

    public String getTextFromTextField(TextField textField){
        return textField.getText();
    }
    public void setTextField(){

    }
    public void checkCalculatingThreadState(){
        if(calculatingThread == null)
            return;
        if(!calculatingThread.isAlive()){
            calculateSizeButton.setText("Calculate");
            calculatedSizeLabel.setText("Total size: "+ calculatingRunnable.getFileSize());
            calculatingRunnable = null;
            calculatingThread = null;
        }
        else{
            calculatedSizeLabel.setText(""+ calculatingRunnable.getFileSize());
        }

    }
    public void calculateOrStop(){
        if(calculatingThread == null) {
            if (!textField.getText().equals("")) {
                String path = textField.getText();
                calculatingRunnable = new SizeCalculatingRunnable(path, checkBox.isSelected());
                calculatingThread = new Thread(calculatingRunnable);
                calculatingThread.start();
                calculateSizeButton.setText("Zakoncz");
                timer.start();
            }
        }
         else{
             calculatingRunnable.requestStop();
             try {
                 calculatingThread.join();
             }catch (Exception e){
                 e.printStackTrace();
             }
             calculateSizeButton.setText("Calculate");
             calculatingThread = null;
           //JOptionPane.showMessageDialog(this ,String.valueOf(calculateDirectorySize(files, checkBox.isSelected())));
        }
    }

    public void loadButtonPressed() {
        try {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setSelectedFile(new File(textField.getText()));
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                previousText = file.getPath();
                textField.setText(previousText);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Wczytywanie nie powiodlo sie:" + e.getMessage());
        }
    }
    public void chooseButtonClicked(){
        fileChooser = new JFileChooser();
    }
    public void createMenuBar(){
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenu help = new JMenu("Help");
        JMenuItem exitItem = new JMenuItem("exit");
        JMenuItem aboutProgramItem = new JMenuItem("About Program");
        exitItem.addActionListener(e -> {
            setVisible(false);
        });
        aboutProgramItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, " ");
        });
        menu.add(exitItem);
        help.add(aboutProgramItem);
        menuBar.add(menu);
        menuBar.add(help);
        setJMenuBar(menuBar);
    }
    public MainFrame(){
        setLayout(new GridLayout());
        textField = new JTextField();
        loadButton = new JButton("Load");
        chooseDirectoryButton = new JButton("Search");
        calculateSizeButton = new JButton("Calculate");
        checkBox = new JCheckBox("szukanie z podkatalogami");
        calculatedSizeLabel = new Label();
        timer = new Timer(500, (event) -> {
            checkCalculatingThreadState();
        });
        chooseDirectoryButton.addActionListener(e -> {
            chooseButtonClicked();
        });
        loadButton.addActionListener((event) -> loadButtonPressed());
        calculateSizeButton.addActionListener(event ->{
            calculateOrStop();
        });
        add(textField);
        add(loadButton);
        add(chooseDirectoryButton);
        add(checkBox);
        add(calculateSizeButton);
        add(calculatedSizeLabel);
        createMenuBar();
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        previousText = textField.getText();
        textField.setText(previousText);
    }
}
//poprawić layout
