import java.util.ArrayList;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.KeyStroke;
import java.text.NumberFormat;
import java.text.DecimalFormat;
/**
 * Making a GUI calculator.
 * 
 * @author Ricky Dam
 * @version June 27, 2015
 * 
 * @author Ricky Dam
 * @version June 28, 2015
 * 
 * @author Ricky Dam
 * @version August 31, 2015
 * 
 * @author Ricky Dam
 * @version October 2, 2015
 * 
 * @author Ricky Dam
 * @version October 6, 2015
 * 
 * @author Ricky Dam
 * @version October 7, 2015
 * 
 * @author Ricky Dam
 * @version October 8, 2015
 * 
 * @author Ricky Dam
 * @version October 9, 2015
 * 
 * @author Ricky Dam
 * @version October 10, 2015
 * 
 * @author Ricky Dam
 * @version October 11, 2015
 */
public class Calculator implements ActionListener
{
    private JFrame frame;
    private Color white;
    private JMenuItem reset;
    private JMenuItem quit;
    
    private JPanel mainPanel;
    private JPanel displayPanel;
    private JLabel display;
    private JLabel current;
    
    private JPanel buttonsPanel;    
    private JButton[][] buttons; // A double array of JButtons
    private static final String[][] buttonsText = 
    {
            {"(", ")", "%", "AC"},
            {"7", "8", "9", "/"} ,
            {"4", "5", "6", "*"} ,
            {"1", "2", "3", "-"} ,
            {"0", ".", "=", "+"} 
    };
    
    private ArrayList<Double> numbers;
    private ArrayList<Integer> actions;
    private String tempNumberString = "";
    private String stringToShow = "";
    private Double answer;
    private NumberFormat nf;
    private boolean decimal;
    private boolean percent;
    private Double percentValue;
    private Double percentTotal;
    private int counterForPercent;
    /**
     * Constructor for objects of class Calculator
     */
    public Calculator()
    {        
        frame = new JFrame("Ricky's Calculator");
        frame.setPreferredSize(new Dimension(500, 790)); // (width, length);
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());
        
        display = new JLabel("0");
        display.setFont(new Font(null, Font.BOLD, 100));
        white = new Color(255, 255, 255);
        display.setBackground(white);
        display.setOpaque(true);
        display.setHorizontalAlignment(JLabel.RIGHT);
        
        current = new JLabel(" ");
        current.setFont(new Font(null, 0, 40));
        current.setBackground(white);
        current.setOpaque(true);
        current.setHorizontalAlignment(JLabel.RIGHT);
        
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(5, 4));
        buttons = new JButton[5][4];
        for(int row=0; row<5; row++) {
            for(int col=0; col<4; col++) {
                buttons[row][col] = new JButton(buttonsText[row][col]);
                buttons[row][col].setFont(new Font(null, 0, 60));
                buttonsPanel.add(buttons[row][col]);
                buttons[row][col].addActionListener(this);
                buttons[row][col].setFocusPainted(false);
            }
        }
        
        numbers = new ArrayList<Double>(); // Stores the numbers
        actions = new ArrayList<Integer>(); // Stores the operands        
        answer = 0.0;
        nf = new DecimalFormat("##.#####"); // The wanted format for the answer to be printed, as a double
        decimal = false;
        percent = false;
        percentValue = -1.0;
        percentTotal = -1.0;
        counterForPercent = -1;
        
        menu(); // Loads and initializes the file menu components such as the GUI for clear and quit
        
        /////////////////////////////////////////////////////
        displayPanel = new JPanel();
        displayPanel.setLayout(new BorderLayout());
        
        displayPanel.add(current, BorderLayout.NORTH);
        displayPanel.add(display, BorderLayout.SOUTH);
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        
        mainPanel.add(displayPanel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        
        contentPane.add(mainPanel, BorderLayout.CENTER);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Allow closing through red (X) button at the top right
        frame.pack(); // Pack and put everything together
        frame.setResizable(true); // Allow user to resize the window
        frame.setVisible(true); // Make the GUI visible
    }
    
    /**
     * Checks the action performed by the user.
     */
    public void actionPerformed(ActionEvent event)
    {
        Object object = event.getSource();
        if(object instanceof JButton) {
            JButton theButton = (JButton) event.getSource();
            // stringToShow needs to be cleared so there are no repeats when printing into the display for current
            stringToShow = "";
            
            // Print the buttons from 1 to and including 9
            for(int row=1; row<4; row++) {
                for(int col=0; col<3; col++) {
                    if(theButton == buttons[row][col]) {
                        tempNumberString += buttons[row][col].getText();
                        print();
                    }
                }
            }            
            // The "0" button
            if(theButton == buttons[4][0]) { 
                tempNumberString += buttons[4][0].getText();
                print();
            }
            
            // An instant operand has been pressed
            if(numbers.size() == 0 && tempNumberString == "") {
                tempNumberString += buttons[4][0].getText();
            }
            
            // Each operand will be represented as an integer
            if(theButton == buttons[1][3]) { // (/)
                actions.add(3); // Add the operand as an action
                makeNumber(); // Call makeNumber to turn the digits into a number
                print(); // Call the void print function to print out what has been done so far for the user to see
            }
            if(theButton == buttons[2][3]) { // (*)
                actions.add(2);
                makeNumber();
                print();
            }
            if(theButton == buttons[3][3]) { // (-)
                actions.add(1);
                makeNumber();
                print();
            }
            if(theButton == buttons[4][3]) { // (+)
                actions.add(0);
                makeNumber();
                print();                
            }
            
            // (=) pressed
            if(theButton == buttons[4][2]) {
                if(actions.size() == 0 && numbers.size() == 0) { // Just print the current digits
                    print();
                    answer = Double.parseDouble(tempNumberString);
                }
                else {
                    makeNumber(); // Turns the digits into numbers
                    calculate(); // Call calculate to get an answer for what the user has inputted so far
                    numbers.clear(); // After the answer has been calculated, the old numbers are no longer needed
                    actions.clear(); // The old operands are no longer needed either
                }
                clearExceptAnswer(); // The answer should not be cleared yet because it needs to be displayed
                display.setText(nf.format(answer));
            }
            
            // Decimal point pressed
            if(theButton == buttons[4][1]) {
                decimal = true; // Boolean set to be used in the print() method
                print();
            }
            
            // AC (All Clear) pressed
            if(theButton == buttons[0][3]) {
                clearAll();
                current.setText("All Cleared."); // Tell the user that it has been done to prevent ambiguity
            }
            
            // (%) pressed
            if(theButton == buttons[0][2]) {
                percent = true; // Boolean set to be used in the print() method
                print();
            }
        }
        else {
            JMenuItem element = (JMenuItem) object;
            if(element == reset) {
                clearAll();
                current.setText("All Cleared.");
            }
            if(element == quit) {
                System.exit(0);
            }
        }
    }
    
    /**
     * Turns the pressed digits into a number.
     */
    public void makeNumber()
    {
        if(percent) {
            percentValue = percentValue / 100; // Turn the percentValue into decimal, i.e, 13% becomes 0.13
            // Call the calculate() method to do the math for everything before the percent button was pressed and then
            // multiply it by percentValue to get a percentage
            calculate();
            numbers.add(percentTotal);
            tempNumberString = "";
            counterForPercent++; // Keep a counter for the spot where the percentage should be for the print() method
        }
        if(tempNumberString != "") {
            numbers.add(Double.parseDouble(tempNumberString)); // The digits will not be turned into a number
            tempNumberString = ""; // Clear the tempNumberString for the next number
            counterForPercent++;
        }
    }
    
    /**
     * Printing what the user will see.
     */
    public void print()
    {        
        if(numbers.size() != 0) { // The ArrayList is not empty
            int numbersSize = numbers.size();
            for(int i=0; i<numbersSize; i++) {
                stringToShow += nf.format(numbers.get(i));
                if(decimal) {
                    tempNumberString += ".";
                    decimal = false;
                }
                if(actions != null) {
                    if(actions.get(i) == 0) {
                        stringToShow += " + ";
                    }
                    if(actions.get(i) == 1) {
                        stringToShow += " - ";
                    }
                    if(actions.get(i) == 2) {
                        stringToShow += " * ";
                    }
                    if(actions.get(i) == 3) {
                        stringToShow += " / ";
                    }                    
                }
                if(percent && i==counterForPercent) { // Only print it into the appropriate spot
                    percentValue = Double.parseDouble(tempNumberString); // Hold the percentage value
                    makeNumber(); // Call makeNumber() to create the percentValue into a use-able decimal
                    stringToShow += nf.format(percentTotal); // To show the user their input
                    percent = false; // Reset the percent boolean back to false
                }
            }
            if(tempNumberString != "") {
                stringToShow += tempNumberString; // Show the user their most recent input
            }
        }
        else { // The ArrayList has nothing, no number yet, just print the current digit to show user            
            if(decimal) {
                tempNumberString += ".";
                decimal = false;
            }            
            stringToShow += tempNumberString;
        }
        current.setText(stringToShow);
    }
    
    /**
     * Calculate the current total.
     */
    public void calculate()
    {                        
        int operand = -1;
        for(int i=0; i<numbers.size(); i++) {
            // Getting the operand
            if(i>=1 && i <= actions.size()) { // When i==0, all it does it put the first number into answer, so skip it
                // i-1 to make sure the operand does not get an OutOfBoundsException since it starts when i==1 and
                // there cannot be more operands than there are numbers
                operand = actions.get(i-1);
            }
            
            // Very first number, just put the number into answer
            if(i == 0) { 
                answer = numbers.get(i);
            }
            
            // Sum answer and the value from ArrayList, numbers, at index i
            if(i != 0 && operand == 0) {
                answer = answer + numbers.get(i);
            }            
            
            // Substract the value from from ArrayList, numbers, at index i from answer
            if(i != 0 && operand == 1) {
                answer = answer - numbers.get(i);
            }
            
            // Multiply answer and the value from ArrayList, numbers, at index i
            if(i != 0 && operand == 2) {
                answer = answer * numbers.get(i);
            }
            
            // Divide answer and the value from ArrayList, numbers, at index i
            if(i != 0 && operand == 3) {
                answer = answer / numbers.get(i);
            }
            
            if(percent && i==counterForPercent) {
                percentTotal = answer * percentValue; // Calculate the answer then get the percentage
                break;
            }
        }
    }
    
    /**
     * Clear everything.
     */
    public void clearAll()
    {
        display.setText("0");
        current.setText(" ");
        tempNumberString = "";
        numbers.clear();
        actions.clear();
        stringToShow = "";
        answer = 0.0;
        decimal = false;
        percent = false;
        percentValue = -1.0;
        percentTotal = -1.0;
        counterForPercent = -1;
    }
    
    /**
     * Clear everything except the answer.
     */
    public void clearExceptAnswer()
    {
        display.setText("0");
        current.setText(" ");
        tempNumberString = "";
        numbers.clear();
        actions.clear();
        stringToShow = "";
        decimal = false;
        percentValue = -1.0;
        percentTotal = -1.0;
        counterForPercent = -1;
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
    
    /**
     * The main function that allows building a jar file from this class or running it from command prompt.
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
}