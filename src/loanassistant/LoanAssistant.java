package loanassistant;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class LoanAssistant {

	private JFrame frame;
	private JTextField balanceTextField;
	private JTextField interestTextField;
	private JTextField monthsTextField;
	private JTextField paymentTextField;
	private JButton exitButton;
	private JButton monthsButton;
	private JButton paymentButton;
	private JButton computeButton;
	private JButton newLoanButton;
	private JTextArea analysisTextArea;
	private boolean computePayment;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoanAssistant window = new LoanAssistant();
					window.frame.setVisible(true);
					window.frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void monthsButtonActionPerformed(ActionEvent e)
    {
        computePayment = false;
        paymentButton.setVisible(true);
        monthsButton.setVisible(false);
        monthsTextField.setText("");
        monthsTextField.setEditable(false);
        monthsTextField.setBackground(Color.yellow);
        paymentTextField.setEditable(true);
        paymentTextField.setBackground(Color.WHITE);
        paymentTextField.setFocusable(true);
        computeButton.setText("Compute Number of Payments");
        balanceTextField.requestFocus();
    }
	private void paymentButtonActionPerformed(ActionEvent e)
    {
        computePayment = true;
        paymentButton.setVisible(false);
        monthsButton.setVisible(true);
        monthsTextField.setEditable(true);
        monthsTextField.setBackground(Color.WHITE);
        monthsTextField.setFocusable(true);
        paymentTextField.setText("");
        paymentTextField.setEditable(false);
        paymentTextField.setBackground(Color.yellow);
        paymentTextField.setFocusable(false);
        computeButton.setText("Compute Monthly Payment");
        balanceTextField.requestFocus();
    }
	private void computeButtonActionPerformed(ActionEvent e)
    {
        double balance, interest, payment=0.0;
        int months;
        double monthlyInterest, multiplier;
        double loanBalance, finalPayment;
        if(validateDecimalNumber(balanceTextField)) {
            balance = Double.valueOf(balanceTextField.getText()).doubleValue();
        }
        else{
            JOptionPane.showConfirmDialog(null, "Invalid or empty Loan Balance entry.\nPlease correct.", "Balance Input Error", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if(validateDecimalNumber(interestTextField)){
            interest =Double.valueOf(interestTextField.getText()).doubleValue();
        }
        else{
            JOptionPane.showConfirmDialog(null, "Invalid or empty Interest Rate entry.\nPlease correct.", "Interest Input Error", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        monthlyInterest = interest / 1200;// Compute loan payment
        if(computePayment)
        {
            if(validateDecimalNumber(monthsTextField))
                months = Integer.valueOf(monthsTextField.getText()).intValue();
            else{
                JOptionPane.showConfirmDialog(null, "Invalid or empty Number of Payments entry.\nPlease correct.", "Number of Payments Input Error",JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if(interest == 0){
                payment = balance / months;
            }
            else {
                multiplier = Math.pow(1 + monthlyInterest, months);
                payment = balance * monthlyInterest * multiplier / (multiplier - 1);
            }
            paymentTextField.setText(new DecimalFormat("0.00").format(payment));
        }
        else 
        {
        	//System.out.print(balance * monthlyInterest + 1.0);
            if(validateDecimalNumber(paymentTextField)) 
            {
            	
                payment = Double.valueOf(paymentTextField.getText()).doubleValue();
	            if (payment <= (balance * monthlyInterest + 1.0))
	            {
	            	//System.out.print(balance * monthlyInterest + 1.0);
	                if (JOptionPane.showConfirmDialog(null, "Minimum payment must be $" +new DecimalFormat("0.00").format((int)(balance * monthlyInterest + 1.0)) + "\n" + "Do you want to use the minimum payment?", "Input Error", JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)
	                {
	                    paymentTextField.setText(new DecimalFormat("0.00").format((int)(balance * monthlyInterest + 1.0)));
	                    payment =Double.valueOf(paymentTextField.getText()).doubleValue();
	                }
	                else
	                    {
	                        paymentTextField.requestFocus();return;
	                    }
	            }
            }
	        else {
            	JOptionPane.showConfirmDialog(null, "Invalid or empty Monthly Payment entry.\nPlease correct.", "Payment Input Error", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
	            return;
	       }
            if(interest == 0){
            	months = (int)(balance / payment);
            }
            else {
            	months = (int) ((Math.log(payment) - Math.log(payment - balance * monthlyInterest)) / Math.log(1 + monthlyInterest));
            }
            monthsTextField.setText(String.valueOf(months));
        }
        payment =Double.valueOf(paymentTextField.getText()).doubleValue();
        analysisTextArea.setText("Loan Balance: $" + new DecimalFormat("0.00").format(balance));
        analysisTextArea.append("\n" + "Interest Rate: " + new DecimalFormat("0.00").format(interest) + "%");// process all but last payment
        loanBalance = balance;
        for (int paymentNumber = 1; paymentNumber <= months - 1; paymentNumber++)
        {
            loanBalance += loanBalance * monthlyInterest - payment;
        }// find final payment
        finalPayment = loanBalance;
        if (finalPayment > payment){// apply one more payment
            loanBalance += loanBalance * monthlyInterest - payment;
            finalPayment = loanBalance;months++;
            monthsTextField.setText(String.valueOf(months));
        }
        analysisTextArea.append("\n\n" + String.valueOf(months - 1) + " Payments of $" + new DecimalFormat("0.00").format(payment));
        analysisTextArea.append("\n" + "Final Payment of: $" + new DecimalFormat("0.00").format(finalPayment));
        analysisTextArea.append("\n" + "Total Payments: $" + new DecimalFormat("0.00").format((months - 1) * payment + finalPayment));
        analysisTextArea.append("\n" + "Interest Paid $" + new DecimalFormat("0.00").format((months - 1) * payment + finalPayment - balance));
        computeButton.setEnabled(false);
        newLoanButton.setEnabled(true);
        newLoanButton.requestFocus();
    }
	private void newLoanButtonActionPerformed(ActionEvent e)
    {
        if(computePayment){
            paymentTextField.setText("");
        }
        else{
            monthsTextField.setText("");
        }
        analysisTextArea.setText("");
        computeButton.setEnabled(true);
        newLoanButton.setEnabled(false);
        balanceTextField.requestFocus();
    }
	private void exitButtonActionPerformed(ActionEvent evt){
        System.exit(0);
    }
    public boolean validateDecimalNumber(JTextField tf){
        String s = tf.getText().trim();
        boolean hasDecimal = false;
        boolean valid = true;
        if(s.length() == 0){
            valid = false;
        }
        else{
            for(int i=0;i<s.length();i++){
                char c = s.charAt(i);
                if(c >= '0' && c <= '9'){
                    continue;
                }
                else if(c == '.' && !hasDecimal){
                    hasDecimal = true;
                }
                else{
                    valid = false;
                }
            }
        }
        tf.setText(s);
        if(!valid){
            tf.requestFocus();
        }
        return(valid);
    }
	/**
	 * Create the application.
	 */
	public LoanAssistant() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Loan Assistant");
		frame.setResizable(false);
		frame.setBounds(200, 100, 795, 442);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel balanceLable = new JLabel("Loan Balance");
		balanceLable.setFont(new Font("Tahoma", Font.PLAIN, 17));
		balanceLable.setBounds(10, 21, 130, 33);
		frame.getContentPane().add(balanceLable);
		
		JLabel interesetLable = new JLabel("Interest Rate");
		interesetLable.setFont(new Font("Tahoma", Font.PLAIN, 17));
		interesetLable.setBounds(10, 75, 130, 30);
		frame.getContentPane().add(interesetLable);
		
		JLabel monthsLable = new JLabel("Number of Payments");
		monthsLable.setFont(new Font("Tahoma", Font.PLAIN, 17));
		monthsLable.setBounds(10, 127, 170, 33);
		frame.getContentPane().add(monthsLable);
		
		JLabel paymentLable = new JLabel("Monthly Payment\r\n");
		paymentLable.setFont(new Font("Tahoma", Font.PLAIN, 17));
		paymentLable.setBounds(10, 176, 157, 33);
		frame.getContentPane().add(paymentLable);
		
		balanceTextField = new JTextField();
		balanceTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				balanceTextField.transferFocus();
			}
		});
		balanceTextField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		balanceTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		balanceTextField.setBounds(184, 22, 157, 33);
		frame.getContentPane().add(balanceTextField);
		balanceTextField.setColumns(10);
		
		interestTextField = new JTextField();
		interestTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				interestTextField.transferFocus();
			}
		});
		interestTextField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		interestTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		interestTextField.setBounds(184, 75, 157, 33);
		frame.getContentPane().add(interestTextField);
		interestTextField.setColumns(10);
		
		monthsTextField = new JTextField();
		monthsTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monthsTextField.transferFocus();
			}
		});
		monthsTextField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		monthsTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		monthsTextField.setBounds(184, 128, 157, 33);
		frame.getContentPane().add(monthsTextField);
		monthsTextField.setColumns(10);
		
		paymentTextField = new JTextField();
		paymentTextField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paymentTextField.transferFocus();
			}
		});
		paymentTextField.setFont(new Font("Tahoma", Font.PLAIN, 15));
		paymentTextField.setBackground(new Color(255, 255, 0));
		paymentTextField.setHorizontalAlignment(SwingConstants.RIGHT);
		paymentTextField.setBounds(184, 177, 157, 33);
		frame.getContentPane().add(paymentTextField);
		paymentTextField.setColumns(10);
		
		monthsButton = new JButton("X");
		monthsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				monthsButtonActionPerformed(e);
				
			}
		});
		monthsButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		monthsButton.setBounds(351, 127, 65, 33);
		frame.getContentPane().add(monthsButton);
		
		paymentButton = new JButton("X");
		paymentButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				paymentButtonActionPerformed(e);
			}
		});
		paymentButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		paymentButton.setBounds(351, 176, 65, 33);
		frame.getContentPane().add(paymentButton);
		
		computeButton = new JButton("Compute Monthly Payment");
		computeButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		computeButton.setBounds(46, 267, 295, 45);
		frame.getContentPane().add(computeButton);
		computeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				computeButtonActionPerformed(e);
			}
		});
		
		newLoanButton = new JButton("New Loan Analysis\r\n");
		newLoanButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				newLoanButtonActionPerformed(e);
			}
		});
		newLoanButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		newLoanButton.setBounds(46, 328, 295, 45);
		frame.getContentPane().add(newLoanButton);
		
		analysisTextArea = new JTextArea();
		analysisTextArea.setFont(new Font("Courier New", Font.PLAIN, 17));
		analysisTextArea.setBounds(427, 58, 344, 291);
		frame.getContentPane().add(analysisTextArea);
		
		JLabel loanAnalysisLable = new JLabel("Loan Analysis");
		loanAnalysisLable.setFont(new Font("Tahoma", Font.PLAIN, 17));
		loanAnalysisLable.setBounds(424, 21, 130, 33);
		frame.getContentPane().add(loanAnalysisLable);
		
		exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exitButtonActionPerformed(e);
			}
		});
		paymentButton.doClick();
		newLoanButton.doClick();
        //setSize(693,350);
		exitButton.setFont(new Font("Tahoma", Font.PLAIN, 17));
		exitButton.setBounds(530, 359, 130, 36);
		frame.getContentPane().add(exitButton);
	}
}
