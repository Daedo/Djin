package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import daedalusCodeComponents.DaedalusCompilationUnit;
import daedalusExecution.runner.DaedalusRunner;
import daedalusParser.DaedalusMain;
import daedalusParser.DaedalusParsingException;

import java.awt.GridBagLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.Toolkit;

public class DjinMainGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel textField;
	private JTextField textField_1;
	private boolean hasOpenFile;
	private String fileData,linedData;
	private JTextArea textArea;
	private JScrollPane scrollPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DjinMainGUI frame = new DjinMainGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DjinMainGUI() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("C:\\Users\\Dominik\\Desktop\\Programmieren\\Java\\git\\Djin\\Parser\\Icon.png"));
		this.hasOpenFile = false;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 768);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{0, 0, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		this.contentPane.setLayout(gbl_contentPane);
		
		this.textField = new JLabel("Select File to Open");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 0;
		this.contentPane.add(this.textField, gbc_textField);
		
		JButton btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String s = getLoadName();
				if(s!=null && !s.equals("")) {
					getFile().setText(s);
					loadFile(s);
				}
			}
		});
		GridBagConstraints gbc_btnOpen = new GridBagConstraints();
		gbc_btnOpen.insets = new Insets(0, 0, 5, 0);
		gbc_btnOpen.gridx = 1;
		gbc_btnOpen.gridy = 0;
		this.contentPane.add(btnOpen, gbc_btnOpen);
		
		this.scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		this.contentPane.add(this.scrollPane, gbc_scrollPane);
		
		this.textArea = new JTextArea();
		this.scrollPane.setViewportView(this.textArea);
		this.textArea.setEditable(false);
		
		this.textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 0, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 0;
		gbc_textField_1.gridy = 2;
		this.contentPane.add(this.textField_1, gbc_textField_1);
		this.textField_1.setColumns(10);
		
		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				runFile();
			}
		});
		GridBagConstraints gbc_btnRun = new GridBagConstraints();
		gbc_btnRun.gridx = 1;
		gbc_btnRun.gridy = 2;
		this.contentPane.add(btnRun, gbc_btnRun);
	}

	public JLabel getFile() {
		return this.textField;
	}
	public JTextField getInput() {
		return this.textField_1;
	}

	private String getLoadName() {
		JFileChooser fileChooser = new JFileChooser();
		//TODO Remove Debug
		fileChooser.setCurrentDirectory(new File("C:\\Users\\Dominik\\Desktop\\Dokumente\\Auﬂerschulisches\\Programmierung\\Daedalus Language\\"));
		fileChooser.setDialogTitle("Specify a file to open");    

		fileChooser.setFileFilter(new FileNameExtensionFilter("DaedalusFiles", "dae"));
		
		int userSelection = fileChooser.showOpenDialog(this);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File fileToSave = fileChooser.getSelectedFile();
			return fileToSave.getAbsolutePath();
		}
		return null;
	}
	
	private void loadFile(String path) {
		this.hasOpenFile = false;
		try {
			List<String> data = Files.lines(Paths.get(path)).collect(Collectors.toList());
			
			this.fileData = "";
			this.linedData = "";
			for(int i=0;i<data.size();i++) {
				String line = data.get(i);
				this.fileData+= line+"\n";
				this.linedData += (i+1)+"\t"+line+"\n";
			}
			getTextArea().setText(this.linedData);
			this.hasOpenFile = true;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public JTextArea getTextArea() {
		return this.textArea;
	}
	
	public boolean hasOpenFile() {
		return this.hasOpenFile;
	}

	public void runFile() {
		String[] args = ArgParser.parse(getInput().getText());
		
		
		if(this.hasOpenFile) {
			System.out.println(getInput().getText());
			System.out.println(this.fileData);
			try {
				DaedalusCompilationUnit unit = DaedalusMain.parseString(this.fileData);
				DaedalusRunner.init(unit);
				DaedalusRunner.run(args);
			} catch (DaedalusParsingException e) {
				e.printStackTrace();
			}
			
			System.out.println("Done");
		} else {
			JOptionPane.showMessageDialog(this, "Please open a file first.", "Couldn't start parsing", JOptionPane.WARNING_MESSAGE);
		}
		
	}
}
