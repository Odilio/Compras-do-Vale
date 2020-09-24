package Romanizador;

import static Romanizador.Junidecode.*;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class KoreanRomanizador extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KoreanRomanizador frame = new KoreanRomanizador();
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
	final JTextArea textArea = new JTextArea();
	final JTextArea textArea_1 = new JTextArea();
	public KoreanRomanizador() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		
		textArea.setColumns(16);
		contentPane.add(textArea, BorderLayout.WEST);
		
		
		textArea_1.setColumns(16);
		contentPane.add(textArea_1, BorderLayout.EAST);
		
		JButton btnConverter = new JButton("Converter");
		btnConverter.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				textArea_1.setText (haengul(textArea.getText()));  
			}
		});
		contentPane.add(btnConverter, BorderLayout.CENTER);
	}

}
