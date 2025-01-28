import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class TextEditor extends JFrame implements ActionListener {
	private JFrame frame;
	private JTextArea textArea;
	private JMenuBar menuBar;
	private Font defaultFont = new Font("Arial", Font.PLAIN, 16);
	private int currentFontSize = 16;

	public TextEditor() {
		initializeEditor();
	}

	private void initializeEditor() {
		frame = new JFrame("Awesome Text Editor");
		textArea = new JTextArea();
		textArea.setFont(defaultFont);
		textArea.setLineWrap(true); // Default word wrap enabled
		textArea.setWrapStyleWord(true);

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

		cutItem.addActionListener(this);
		copyItem.addActionListener(this);
		pasteItem.addActionListener(this);

		editMenu.add(cutItem);
		editMenu.add(copyItem);
		editMenu.add(pasteItem);
		menuBar.add(editMenu);

		// FORMAT MENU
		JMenu formatMenu = new JMenu("Format");
		JMenuItem boldItem = new JMenuItem("Bold");
		JMenuItem italicItem = new JMenuItem("Italic");
		JMenuItem normalItem = new JMenuItem("Normal");

		boldItem.addActionListener(this);
		italicItem.addActionListener(this);
		normalItem.addActionListener(this);

		formatMenu.add(boldItem);
		formatMenu.add(italicItem);
		formatMenu.add(normalItem);
		menuBar.add(formatMenu);

		// VIEW MENU
		JMenu viewMenu = new JMenu("View");
		JMenuItem toggleWrapItem = new JMenuItem("Toggle Word Wrap");
		JMenuItem zoomInItem = new JMenuItem("Zoom In");
		JMenuItem zoomOutItem = new JMenuItem("Zoom Out");
		JMenuItem resetZoomItem = new JMenuItem("Reset Zoom");

		toggleWrapItem.addActionListener(this);
		zoomInItem.addActionListener(this);
		zoomOutItem.addActionListener(this);
		resetZoomItem.addActionListener(this);

		viewMenu.add(toggleWrapItem);
		viewMenu.add(zoomInItem);
		viewMenu.add(zoomOutItem);
		viewMenu.add(resetZoomItem);
		menuBar.add(viewMenu);

		// HELP MENU
		JMenu helpMenu = new JMenu("Help");
		JMenuItem aboutItem = new JMenuItem("About");

		aboutItem.addActionListener(this);
		helpMenu.add(aboutItem);
		menuBar.add(helpMenu);

		// Set frame properties
		frame.setJMenuBar(menuBar);
		frame.add(new JScrollPane(textArea));
		frame.setSize(1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		// Center frame
		frame.setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(TextEditor::new);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String currentItem = e.getActionCommand();

		switch (currentItem) {
		case "New" -> textArea.setText("");
		case "Open" -> openFile();
		case "Save" -> saveFile();
		case "Exit" -> System.exit(0);
		case "Cut" -> textArea.cut();
		case "Copy" -> textArea.copy();
		case "Paste" -> textArea.paste();
		case "Bold" -> applyFontStyle(Font.BOLD);
		case "Italic" -> applyFontStyle(Font.ITALIC);
		case "Normal" -> applyFontStyle(Font.PLAIN);
		case "Toggle Word Wrap" -> toggleWordWrap();
		case "Zoom In" -> zoomIn();
		case "Zoom Out" -> zoomOut();
		case "Reset Zoom" -> resetZoom();
		case "About" -> showAboutDialog();
		}
	}

	private void openFile() {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showOpenDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			try (BufferedReader reader = new BufferedReader(new FileReader(fileChooser.getSelectedFile()))) {
				textArea.read(reader, null);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(frame, "Error opening file: " + ex.getMessage());
			}
		}
	}

	private void saveFile() {
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showSaveDialog(frame);
		if (result == JFileChooser.APPROVE_OPTION) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile()))) {
				textArea.write(writer);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(frame, "Error saving file: " + ex.getMessage());
			}
		}
	}

	private void applyFontStyle(int style) {
		Font currentFont = textArea.getFont();
		textArea.setFont(new Font(currentFont.getFontName(), style, currentFont.getSize()));
	}

	private void toggleWordWrap() {
		boolean currentWrapState = textArea.getLineWrap();
		textArea.setLineWrap(!currentWrapState);
		JOptionPane.showMessageDialog(frame, "Word Wrap " + (currentWrapState ? "Disabled" : "Enabled"));
	}

	private void zoomIn() {
		currentFontSize += 2;
		textArea.setFont(new Font(textArea.getFont().getFontName(), textArea.getFont().getStyle(), currentFontSize));
	}

	private void zoomOut() {
		if (currentFontSize > 8) {
			currentFontSize -= 2;
			textArea.setFont(
					new Font(textArea.getFont().getFontName(), textArea.getFont().getStyle(), currentFontSize));
		}
	}

	private void resetZoom() {
		currentFontSize = 16;
		textArea.setFont(new Font(textArea.getFont().getFontName(), textArea.getFont().getStyle(), currentFontSize));
	}

	private void showAboutDialog() {
		JOptionPane.showMessageDialog(frame, "Awesome Text Editor\nVersion 2.2\nDeveloped by Mr. DIBAKAR with ❤️");
	}
}
