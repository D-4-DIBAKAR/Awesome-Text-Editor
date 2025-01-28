import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class TextEditor extends JFrame implements ActionListener {
	private JFrame frame;
	private JTextArea textArea;
	private JMenuBar menuBar;

	public TextEditor() {
		initializeEditor();
	}

	private void initializeEditor() {
		frame = new JFrame("Awesome Text Editor");
		textArea = new JTextArea();
		menuBar = new JMenuBar();

		// FILE MENU
		JMenu fileMenu = new JMenu("File");
		JMenuItem newItem = new JMenuItem("New");
		JMenuItem openItem = new JMenuItem("Open");
		JMenuItem saveItem = new JMenuItem("Save");
		JMenuItem exitItem = new JMenuItem("Exit");

		newItem.addActionListener(this);
		openItem.addActionListener(this);
		saveItem.addActionListener(this);
		exitItem.addActionListener(this);

		fileMenu.add(newItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);

		menuBar.add(fileMenu);

		// EDIT MENU
		JMenu editMenu = new JMenu("Edit");
		JMenuItem cutItem = new JMenuItem("Cut");
		JMenuItem copyItem = new JMenuItem("Copy");
		JMenuItem pasteItem = new JMenuItem("Paste");
		JMenuItem findReplaceItem = new JMenuItem("Find & Replace");

		cutItem.addActionListener(this);
		copyItem.addActionListener(this);
		pasteItem.addActionListener(this);
		findReplaceItem.addActionListener(this);

		editMenu.add(cutItem);
		editMenu.add(copyItem);
		editMenu.add(pasteItem);
		editMenu.add(findReplaceItem);

		menuBar.add(editMenu);

		// HELP MENU
		JMenu helpMenu = new JMenu("Help");
		JMenuItem aboutItem = new JMenuItem("About");

		aboutItem.addActionListener(this);

		helpMenu.add(aboutItem);

		menuBar.add(helpMenu);

		// Set frame properties
		frame.setJMenuBar(menuBar);
		frame.add(new JScrollPane(textArea));
		frame.setSize(750, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Center the frame on screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(screenSize.width / 2 - frame.getWidth() / 2, screenSize.height / 2 - frame.getHeight() / 2);

		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(TextEditor::new);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String currentItem = e.getActionCommand();

		switch (currentItem) {
		case "Cut" -> textArea.cut();
		case "Copy" -> textArea.copy();
		case "Paste" -> textArea.paste();
		case "New" -> textArea.setText("");
		case "Save" -> saveFile();
		case "Open" -> openFile();
		case "Exit" -> System.exit(0);
		case "Find & Replace" -> openFindReplaceDialog();
		case "About" -> showAboutDialog();
		}
	}

	private void saveFile() {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showSaveDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
			try (BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(file, false))) {
				bufferWriter.write(textArea.getText());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, "Error saving file: " + e.getMessage());
			}
		}
	}

	private void openFile() {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
			try (BufferedReader bufferReader = new BufferedReader(new FileReader(file))) {
				StringBuilder fileContent = new StringBuilder();
				String line;
				while ((line = bufferReader.readLine()) != null) {
					fileContent.append(line).append("\n");
				}
				textArea.setText(fileContent.toString());
			} catch (IOException e) {
				JOptionPane.showMessageDialog(frame, "Error opening file: " + e.getMessage());
			}
		}
	}

	private void openFindReplaceDialog() {
		JDialog findReplaceDialog = new JDialog(frame, "Find & Replace", true);
		findReplaceDialog.setLayout(new BoxLayout(findReplaceDialog.getContentPane(), BoxLayout.Y_AXIS));
		findReplaceDialog.setSize(400, 200);
		findReplaceDialog.setLocationRelativeTo(frame);

		// Input fields for Find and Replace
		JTextField findField = new JTextField();
		JTextField replaceField = new JTextField();
		JButton findButton = new JButton("Find");
		JButton replaceButton = new JButton("Replace");

		JPanel findPanel = new JPanel();
		findPanel.setLayout(new BoxLayout(findPanel, BoxLayout.Y_AXIS));
		findPanel.add(new JLabel("Find:"));
		findPanel.add(findField);

		JPanel replacePanel = new JPanel();
		replacePanel.setLayout(new BoxLayout(replacePanel, BoxLayout.Y_AXIS));
		replacePanel.add(new JLabel("Replace with:"));
		replacePanel.add(replaceField);

		JPanel buttonPanel = new JPanel();
		buttonPanel.add(findButton);
		buttonPanel.add(replaceButton);

		// Add panels to dialog
		findReplaceDialog.add(findPanel);
		findReplaceDialog.add(replacePanel);
		findReplaceDialog.add(buttonPanel);

		// Add functionality
		findButton.addActionListener(action -> {
			String findText = findField.getText();
			String content = textArea.getText();
			if (findText.isEmpty()) {
				JOptionPane.showMessageDialog(findReplaceDialog, "Please enter text to find.");
				return;
			}
			int index = content.indexOf(findText);
			if (index != -1) {
				textArea.select(index, index + findText.length());
				textArea.requestFocus();
			} else {
				JOptionPane.showMessageDialog(findReplaceDialog, "Text not found.");
			}
		});

		replaceButton.addActionListener(action -> {
			String findText = findField.getText();
			String replaceText = replaceField.getText();
			String content = textArea.getText();
			if (findText.isEmpty()) {
				JOptionPane.showMessageDialog(findReplaceDialog, "Please enter text to find.");
				return;
			}
			if (content.contains(findText)) {
				textArea.setText(content.replace(findText, replaceText));
				JOptionPane.showMessageDialog(findReplaceDialog, "Text replaced.");
			} else {
				JOptionPane.showMessageDialog(findReplaceDialog, "Text not found.");
			}
		});

		findReplaceDialog.setVisible(true);
	}

	private void showAboutDialog() {
		JOptionPane.showMessageDialog(frame, "Awesome Text Editor\nVersion 1.0\nDeveloped by Mr. DIBAKAR with ❤️");
	}
}
