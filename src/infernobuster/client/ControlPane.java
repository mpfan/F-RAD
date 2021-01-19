package infernobuster.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;


import infernobuster.detector.Detector;
import infernobuster.parser.Action;
import infernobuster.parser.Direction;
import infernobuster.parser.IpTableParser;
import infernobuster.parser.Parser;
import infernobuster.parser.ParserException;
import infernobuster.parser.Protocol;
import infernobuster.parser.Rule;
import infernobuster.parser.UFWParser;

public class ControlPane extends JPanel {
	private static final long serialVersionUID = 1702526798477578409L;
	
	Table table;
	FWType fwType;
	
	public ControlPane() {
		setLayout(new BorderLayout());
		setBackground(Color.WHITE);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Menu");
		
		JMenuItem open = new JMenuItem("Open File..");
		open.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectType();
				if (fwType != null) {
					openFile();
				}
			}
		});
		
		JMenuItem export = new JMenuItem("Export As");
		export.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectType();
				if (fwType != null) {
					exportFile();
				}
			}
		});
		
		menu.add(open);
		menu.add(export);
		menuBar.add(menu);
		add(menuBar, BorderLayout.NORTH);
		
		table = new Table();
		add(table, BorderLayout.WEST);

	}
	
	/**
	 * 
	 */
	private void openFile() {
		JFileChooser chooser = new JFileChooser();
        // optionally set chooser options ...
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        	File file = chooser.getSelectedFile();
        	
        	BufferedReader br;
    		ArrayList<String> content = new ArrayList<String>();
            
            try {
    			br = new BufferedReader(new FileReader(file));
    			
    			String line;
    			while ((line = br.readLine()) != null) {
    				content.add(line);
    			}
    		} catch (FileNotFoundException e) {
    			System.out.println("File not found");
    			System.exit(1);
    		} catch (IOException e) {
    			System.out.println("Error reading file");
    			System.exit(1);
    		} 

            Parser parser = null;
            if (fwType == FWType.IPTABLES) {
        		parser = new IpTableParser();
            } else if (fwType == FWType.UFW) {
        		parser = new UFWParser();
            }

    		ArrayList<Rule> rules = null;
    		try {
    			rules = parser.parse(content);
        		table.getModel().setRules(rules);
    		} catch (Exception e) {
    			JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    		}
    		
        } else {
            // user changed their mind
        }
	}
	
	/**
	 *
	 */
	private void exportFile() {
		JFileChooser chooser = new JFileChooser();
        // optionally set chooser options ...
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        	// To be done
        } else {
            // user changed their mind
        }
	}

	/**
	 * 
	 */
	private void selectType() {
		Object[] possibilities = {"UFW", "IPTables"};
		String s = (String)JOptionPane.showInputDialog(
		                    new JFrame(), "Please select a firewall type:",
		                    "FireWall Selection",
		                    JOptionPane.PLAIN_MESSAGE,
		                    null, possibilities,
		                    "UFW");	
		if (s != null) {
			fwType = FWType.fromString(s);
		} else {
			fwType = null;
		}
	}
	
}
