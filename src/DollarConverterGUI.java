import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.json.JSONException;
import org.json.JSONObject;

public class DollarConverterGUI extends JFrame implements ActionListener {
	private JLabel labelTitle;
	private JTextField textFieldCurrInput;
	private JComboBox comboBoxCurrency1;
	private JButton convertButton;
	private JComboBox comboBoxCurrency2;
	private JTextField textFieldCurrOutput;
	private JLabel labelCurrentRate;
	private JTextField textFieldDateInput;
	private JLabel labelForCurrIcon;
	private JLabel labelDate;
	private JLabel labelPriorRateHeading;
	private JLabel labelOneDayPriorRate;
	private JLabel labelTwoDayPriorRate;
	private JLabel labelThreeDayPriorRate;
	private JLabel labelNote;
	private JLabel labelICanCode;
	private JLabel labelForCogIcon;
	private static DollarConverterGUI ex;
	private static boolean dateError;

	public static JSONObject jo = new JSONObject();
	public static Object[] currencyList = new Object[33];
	
	public static void main(String[] args) throws Exception {
		ex = new DollarConverterGUI();
		ex.setVisible(true);
	}
	
	public DollarConverterGUI() throws Exception {
		initUI();
		getCurrencyData(textFieldDateInput.getText());
		addUI();
		priorRates();
	}
	
	public void initUI() throws Exception {
		labelTitle = new JLabel();
		labelTitle.setBounds(200, 10, 150, 30);
		labelTitle.setText("CURRENCY CONVERTER");
		
		textFieldCurrInput = new JTextField();
		textFieldCurrInput.setBounds(5, 50, 100, 30);
		textFieldCurrInput.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				resetLabel();
			}
			@Override
			public void insertUpdate(DocumentEvent arg0) {
				resetLabel();
			}
			@Override
			public void removeUpdate(DocumentEvent arg0) {
				resetLabel();
			}
		});

		convertButton = new JButton("Convert");
		convertButton.setBounds(220, 50, 100, 30);
		convertButton.addActionListener(this);
		
		textFieldCurrOutput = new JTextField();
		textFieldCurrOutput.setBounds(430, 50, 100, 30);
		textFieldCurrOutput.setForeground(Color.blue);
		textFieldCurrOutput.setEditable(false);
		
		labelCurrentRate = new JLabel();
		labelCurrentRate.setBounds(230,75,150,30);
		labelCurrentRate.setForeground(Color.blue);
		
		textFieldDateInput = new JTextField();
		textFieldDateInput.setBounds(5, 100, 100, 30);
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String date = simpleDateFormat.format(new Date());
		textFieldDateInput.setText(date);
		
		labelForCurrIcon = new JLabel(new ImageIcon("resources/dc.jpg"));
		labelForCurrIcon.setBounds(220, 100, 100, 100);
		
		labelDate = new JLabel();
		labelDate.setBounds(115, 100, 150, 30);
		labelDate.setText("YYYY-MM-DD");
		
		labelPriorRateHeading = new JLabel();
		labelPriorRateHeading.setBounds(360, 100, 180, 30);
		labelPriorRateHeading.setForeground(Color.gray);
		Font font = labelPriorRateHeading.getFont();
		Map<TextAttribute, Object> attribute = new HashMap<>(font.getAttributes());
		attribute.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		labelPriorRateHeading.setFont(font.deriveFont(attribute));
		
		labelOneDayPriorRate = new JLabel();
		labelOneDayPriorRate.setBounds(360, 120, 150, 30);
		labelOneDayPriorRate.setForeground(Color.gray);
		
		labelTwoDayPriorRate = new JLabel();
		labelTwoDayPriorRate.setBounds(360,140, 150, 30);
		labelTwoDayPriorRate.setForeground(Color.gray);
		
		labelThreeDayPriorRate = new JLabel();
		labelThreeDayPriorRate.setBounds(360, 160, 150, 30);
		labelThreeDayPriorRate.setForeground(Color.gray);
		
		labelNote = new JLabel();
		labelNote.setBounds(5, 150, 250, 30);
		labelNote.setForeground(Color.red);
		labelNote.setText("<html>*Saturday & Sunday will <br> &nbsp use prior Friday rate</html>");
		
		labelICanCode = new JLabel();
		labelICanCode.setBounds(5, 245, 90, 30);
		labelICanCode.setFont(new Font("Bahnschrift SemiLight SemiConden", Font.BOLD, 13));
		labelICanCode.setText("<html><font color='blue'>{</font> i<font color='green'>C</font>an<font color='green'>C</font>ode <font color='blue'>}</font></html>");
		
		labelForCogIcon = new JLabel(new ImageIcon("resources/cog.jpg"));
		labelForCogIcon.setBounds(460, 190, 100, 100);
	}

	public void addUI() {
		comboBoxCurrency1 = new JComboBox(currencyList);
		comboBoxCurrency1.setSelectedItem(currencyList[31]);
		comboBoxCurrency1.setBounds(115, 50, 60, 30);
		comboBoxCurrency1.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        resetLabel();
		    }
		});
		
		comboBoxCurrency2 = new JComboBox(currencyList);
		comboBoxCurrency2.setSelectedItem(currencyList[15]);
		comboBoxCurrency2.setBounds(360, 50, 60, 30);
		comboBoxCurrency2.addActionListener(new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        resetLabel();
		    }
		});
		
		getContentPane().setLayout(null);
		getContentPane().add(labelTitle);
		getContentPane().add(textFieldCurrInput);
		getContentPane().add(comboBoxCurrency1);
		getContentPane().add(convertButton);
		getContentPane().add(comboBoxCurrency2);
		getContentPane().add(textFieldCurrOutput);
		getContentPane().add(labelCurrentRate);
		getContentPane().add(textFieldDateInput);
		getContentPane().add(labelForCurrIcon);
		getContentPane().add(labelDate);
		getContentPane().add(labelPriorRateHeading);
		getContentPane().add(labelOneDayPriorRate);
		getContentPane().add(labelTwoDayPriorRate);
		getContentPane().add(labelThreeDayPriorRate);
		getContentPane().add(labelNote);
		getContentPane().add(labelICanCode);
		getContentPane().add(labelForCogIcon);

		setTitle("Varun's Currency Converter");
		setSize(550,300);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		resetLabel();
		try {
			getCurrencyData(textFieldDateInput.getText());
			if(isNumeric(textFieldCurrInput.getText())) {
				String currVal = calculatedExchangeRate();
				if(!dateError) {	
					textFieldCurrOutput.setText("" + currVal);
					priorRates();
				}
			} else {
				JOptionPane.showMessageDialog(ex, "Invalid Dollar amount");
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public void resetLabel() {
		textFieldCurrOutput.setText("");
		labelCurrentRate.setText("");
		labelPriorRateHeading.setText("");
		labelOneDayPriorRate.setText("");
		labelTwoDayPriorRate.setText("");
		labelThreeDayPriorRate.setText("");
		dateError = false;
	}
	
	public static void getCurrencyData(String dateEntered) throws Exception {
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		try {
			Date date = simpleDateFormat.parse(dateEntered);
			if(date.after(new Date())) {
				dateError = true;
				JOptionPane.showMessageDialog(ex, "Date cannot be greater than current date");
			} else {
				String url = "https://api.exchangeratesapi.io/" +dateEntered+"?base=USD";
				System.out.println("URL used to get rates" + url);
				
				HttpURLConnection con = establishConnection(url);
				
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String data = in.readLine();
				jo = new JSONObject(data);
				JSONObject ratesObj = new JSONObject(jo.getJSONObject("rates").toString());
				
				ArrayList<String> keysList = new ArrayList<String>();
				Iterator<String> keysIterator = ratesObj.keys();
				while(keysIterator.hasNext()) {
					keysList.add(keysIterator.next());
				}
				currencyList = keysList.toArray();
				Arrays.sort(currencyList);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			dateError = true;
			JOptionPane.showMessageDialog(ex, "Incorrect date");
		}
	}
	
	private static HttpURLConnection establishConnection(String url) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		int responseCode = con.getResponseCode();
		System.out.println("Connection responseCode: " + responseCode);
		return con;
	}
	
	public static boolean isNumeric(String strNum) {
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException | NullPointerException nfe) {
			return false;
		}
		return true;
	}
	
	private void priorRates() throws Exception {
		String dateEntered = textFieldDateInput.getText();
		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		try {
			Date date = simpleDateFormat.parse(dateEntered);
			
			GregorianCalendar cal = new GregorianCalendar();
			cal.setTime(date);
			if(cal.after(new Date())) {
				dateError = true;
				JOptionPane.showMessageDialog(ex, "Date cannot be greater than current date");
			} else {
				for(int i=0; i<=3; i++) {
					if(i!=0) {
						cal.add(Calendar.DATE, -1);
					}
					String priorDate = simpleDateFormat.format(cal.getTime());
					String url = "https://api.exchangeratesapi.io/"+priorDate+"?base=USD";
					
					HttpURLConnection con = establishConnection(url);
					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String data = in.readLine();
					jo = new JSONObject(data);
					
					String currVal = calculatedExchangeRate();
					
					if(!dateError) {	
						labelPriorRateHeading.setText("Rate for previous 3 days");
						if(i==0) {
							labelCurrentRate.setText("Rate: " + currVal);
						} else if(i==1) {
							labelOneDayPriorRate.setText(" " + priorDate + "   :   " + currVal);
						} else if(i==2) {
							labelTwoDayPriorRate.setText(" " + priorDate + "   :   " + currVal);
						} else if(i==3) {
							labelThreeDayPriorRate.setText(" " + priorDate + "   :   " + currVal);
						}
					}
				}
			}
		} catch (Exception e) {
			dateError = true;
			JOptionPane.showMessageDialog(ex, "Incorrect date");
		}
	}
	
	private String calculatedExchangeRate() {
		String currVal = "";
		try {
			String inputCurrency = comboBoxCurrency1.getSelectedItem().toString();
			double inputCurrencyRate = 0;
			inputCurrencyRate = jo.getJSONObject("rates").getDouble(inputCurrency);
			
			String outputCurrency = comboBoxCurrency2.getSelectedItem().toString();
			double outputCurrencyRate = 0;
			outputCurrencyRate = jo.getJSONObject("rates").getDouble(outputCurrency);
			
			double currValue = outputCurrencyRate/inputCurrencyRate;
			DecimalFormat df = new DecimalFormat("###.###");
			currVal = df.format(currValue);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return currVal;
	}
}
