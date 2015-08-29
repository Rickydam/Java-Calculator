import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
/**
 * Making a GUI calculator.
 * 
 * @author Ricky Dam
 * @version June 27, 2015
 * 
 * @author Ricky Dam
 * @version June 28, 2015
 */
public class Calculator implements ActionListener
{
    private JFrame frame;
    private JMenuItem reset;
    private JMenuItem quit;
    private JPanel mainPanel;
    private JLabel display;
    private Color white;
    private JPanel buttonsPanel;
    private JButton[][] buttons;
    private static final String[][] buttonsText = 
    {
            {"7", "8", "9", "/"} ,
            {"4", "5", "6", "*"} ,
            {"1", "2", "3", "-"} ,
            {"0", ".", "=", "+"} 
    };
    private int firstNumber;
    private int secondNumber;
    private boolean picked;
    private int actionToDo;
    private JPanel displayPanel;
    private JLabel current;
    private String tempString;
    private Border blackLine;
    private int tempNumber;
    /**
     * Constructor for objects of class Calculator
     */
    public Calculator()
    {
        white = new Color(255, 255, 255);
        
        frame = new JFrame("Ricky's Calculator");
        frame.setPreferredSize(new Dimension(500, 700)); // (width, length);
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        blackLine = BorderFactory.createLineBorder(Color.black);        
        
        display = new JLabel("0");
        display.setFont(new Font(null, Font.BOLD, 100));
        display.setBackground(white);
        display.setOpaque(true);
        display.setHorizontalAlignment(JLabel.RIGHT);
        
        displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        
        current = new JLabel(" ");
        current.setFont(new Font(null, 0, 40));
        current.setBackground(white);
        current.setOpaque(true);
        current.setHorizontalAlignment(JLabel.RIGHT);
        
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(4, 4));
        
        buttons = new JButton[4][4];
        for(int row=0; row<4; row++) {
            for(int col=0; col<4; col++) {
                buttons[row][col] = new JButton(buttonsText[row][col]);
                buttons[row][col].setFont(new Font(null, 0, 60));
                buttonsPanel.add(buttons[row][col]);
                buttons[row][col].addActionListener(this);
                buttons[row][col].setFocusPainted(false);
            }
        }
        
        firstNumber = 0;
        secondNumber = 0;
        picked = false;
        actionToDo = -1;
        
        menu();
        
        /////////////////////////////////////////////////////
        displayPanel.add(current, BorderLayout.NORTH);
        displayPanel.add(display, BorderLayout.SOUTH);
        mainPanel.add(displayPanel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        contentPane.add(mainPanel, BorderLayout.CENTER);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(true);
        frame.setVisible(true);
    }
    
    /**
     * The main function that allows building a jar file from this class.
     * 
     * @param args An array of strings
     */
    public static void main(String[] args)
    {
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            @Override public void run() {
                new Calculator();
            }
        }
        );
    }
    
    /**
     * Checks the action performed by the user.
     */
    public void actionPerformed(ActionEvent event)
    {
        Object object = event.getSource();
        if(object instanceof JButton) {
            JButton theButton = (JButton) event.getSource();
            if(!picked) {
                for(int row=0; row<3; row++) {
                    for(int col=0; col<3; col++) {
                        if(theButton == buttons[row][col]) {
                            firstNumber = Integer.parseInt(buttons[row][col].getText());
                            tempString = "" + firstNumber;
                            current.setText(tempString);                            
                        }
                    }
                }
                if(theButton == buttons[3][0]) {
                    firstNumber = Integer.parseInt(buttons[3][0].getText());
                    tempString = "" + firstNumber;
                    current.setText(tempString);
                }
                if(theButton == buttons[0][3]) {
                    actionToDo = 3;
                    tempString = firstNumber + " / ";
                    current.setText(tempString);
                }
                if(theButton == buttons[1][3]) {
                    actionToDo = 2;
                    tempString = firstNumber + " * ";
                    current.setText(tempString);
                }
                if(theButton == buttons[2][3]) {
                    actionToDo = 1;
                    tempString = firstNumber + " - ";
                    current.setText(tempString);
                }
                if(theButton == buttons[3][3]) {
                    actionToDo = 0;
                    firstNumber = 0;
                    tempString = firstNumber + " + ";
                    current.setText(tempString);
                }
                picked = true;
            }
            else {
                for(int row=0; row<3; row++) {
                    for(int col=0; col<3; col++) {
                        if(theButton == buttons[row][col]) {
                            secondNumber = Integer.parseInt(buttons[row][col].getText());
                            tempString += secondNumber;
                            current.setText(tempString);                            
                        }
                    }
                }
                if(theButton == buttons[3][0]) {
                    secondNumber = Integer.parseInt(buttons[3][0].getText());
                    tempString = "" + secondNumber;
                    current.setText(tempString);
                }
                if(theButton == buttons[0][3]) { // (/)
                    actionToDo = 3;
                    tempString += " / ";
                    current.setText(tempString);
                }
                if(theButton == buttons[1][3]) { // (*)
                    actionToDo = 2;
                    tempString += " * ";
                    current.setText(tempString);
                }
                if(theButton == buttons[2][3]) { // (-)
                    actionToDo = 1;
                    tempString += " - ";
                    current.setText(tempString);
                }
                if(theButton == buttons[3][3]) { // (+)
                    actionToDo = 0;
                    tempString += " + ";
                    current.setText(tempString);
                }
                if(theButton == buttons[3][2]) { // (=)
                    if(actionToDo == 0) {
                        tempNumber = firstNumber + secondNumber;
                        display.setText("" + tempNumber);
                    }
                    if(actionToDo == 1) {
                        tempNumber = firstNumber - secondNumber;
                        display.setText("" + tempNumber);
                    }
                    if(actionToDo == 2) {
                        tempNumber = firstNumber * secondNumber;
                        display.setText("" + tempNumber);
                    }
                    if(actionToDo == 3) {
                        tempNumber = firstNumber / secondNumber;
                        display.setText("" + tempNumber);
                    }
                    current.setText(" ");
                    tempString = " ";
                }
            }            
        }
        else {
            JMenuItem element = (JMenuItem) object;
            if(element == reset) {
                display.setText("0");
                current.setText(" ");
                tempString = " ";
                tempNumber = 0;
                firstNumber = 0;
                secondNumber = 0;
                actionToDo = -1;
                picked = false;
            }
            if(element == quit) {
                System.exit(0);
            }
        }
    }
    
    /**
     * Checks the action performed by the user.
     */
    public void menu()
    {
        JMenuBar menu = new JMenuBar();
        frame.setJMenuBar(menu);
            
        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(new Font(null, Font.BOLD, 25));
        menu.add(fileMenu);
            
        final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
            
        reset = new JMenuItem("Clear (CTRL+N)");
        reset.setFont(new Font(null, Font.BOLD, 30));
        fileMenu.add(reset);
        reset.addActionListener(this);
        reset.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, SHORTCUT_MASK));
            
        quit = new JMenuItem("Quit (Ctrl+Q)");
        quit.setFont(new Font(null, Font.BOLD, 30));
        fileMenu.add(quit);
        quit.addActionListener(this);
        quit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, SHORTCUT_MASK));        
    }
}



































