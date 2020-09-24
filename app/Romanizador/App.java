/*
 * Copyright (C) 2015 Giuseppe Cardone <ippatsuman@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package Romanizador;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import static Romanizador.Junidecode.*;


public class App  extends JFrame{

    /**
     * Private constructor to avoid instatiation.
     */
	 public JTextArea texto = new JTextArea ();  
	 public JTextArea saida = new JTextArea (); 
	 private JButton evento = new JButton ();
	 
     public App () {  
        // Define o título da janela  
        super ("Meu Notepad"); 
        this.montaJanela ();  
        texto.setLayout(new FlowLayout());
        saida.setLayout(new FlowLayout());
        evento.setLayout(new FlowLayout());
     /*   evento.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println(unidecode("ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ"));
			}
		});*/
     }  
   
 	public void actionPerformed(ActionEvent e) {
		  this.saida.setText (converter(texto.getText()));  
	}
 	
     private void montaJanela () {  
    	 
        this.getContentPane ().add(texto); 
        this.getContentPane ().add(saida);
        this.getContentPane ().add(evento);
     } 

   
    /**
     * Main.
     * @param args Strings to transliterate. If <code>args.length == 0</code>
     * then the input will be read from stdin.
     */
    public static void main(String[] args) {
    	App korean = new App();
    	korean.setSize (640, 480); 
    	korean.setVisible(true);
       /* if (args.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : args) {
                sb.append(unidecode(s)).append(" ");
            }
            System.out.println(sb.toString().trim());
        } else {*/
           
    	System.out.println(unidecode("ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ"));
    }
    
public String converter(String texto) {
	  return unidecode(texto);
}
   
    }
