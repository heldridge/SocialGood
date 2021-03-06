package panels;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import main.Game;

//The Description Panel holds a JLabel which is used to display text describing the 
//Programming concepts used for this level. 
public class DescriptionPanel extends JPanel{

	// The description panel holds a "go" jbutton, and the jlabel to display the text.
	//RunButton goButton; 
	JLabel currentDesc;
	
	//Constructor to add a button and description to the DescriptionPanel
	public DescriptionPanel(String desc) {
		
		currentDesc = new JLabel(desc);
		initGUI();
		
		
	}
	
	//Initiate the GUI
	public void initGUI(){
		//Set Panel Size based on proportion
		Dimension descP = new Dimension((int)((Game.APPLET_WIDTH/5) * 4), (int)(Game.APPLET_HEIGHT/5) - 20);
		setPreferredSize(descP);
		setSize(descP);
		setMaximumSize(descP);
		setMinimumSize(descP);
		
		//Panel Background
		setBackground(Color.WHITE);
		//Panel Border
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		//The Description Panel has a subpanel for the Tips and Tricks Panel.
		Dimension labelP = new Dimension((int)((Game.APPLET_WIDTH/5) * 4) -40, (int)(Game.APPLET_HEIGHT/5) - 60);
		currentDesc.setPreferredSize(labelP);
		
		JLabel title = new JLabel("Tips and Tricks");
		title.setFont(new Font("Arial", Font.BOLD, 20));
		add(title);
		currentDesc.setFont(new Font("Arial", Font.PLAIN, 16));
		add(currentDesc, BorderLayout.SOUTH); 
	}
	
	//Allows us to set the currentDescription displayed on the Description Panel.
	public void setDescription(String newDesc) {
		currentDesc.setText("<html>" + newDesc + "</html>"); 
	}
	
}
