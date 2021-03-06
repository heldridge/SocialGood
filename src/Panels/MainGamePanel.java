package panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import main.Board;
import main.Game;
import main.Level;
import buttons.RunButton;

public class MainGamePanel extends JPanel implements ActionListener {
	
	Board board;						//Instantiates a board
	SelectPanel selectPanel;			//Instantiates the Panel which has buttons for the player to press to select a moving strategy.
	StratPanel stratPanel;				//Instantiates the Panel which displays the moving strategy the player has selected.
	RunButton goButton;					//Instantiates the button which runs the Player's strategy.
	DescriptionPanel descriptionPanel; 	//Instantiates the Panel which displays the description of the level. 
	private ArrayList<Level> levels;			//Instantiates an ArrayList holding the levels implemented in the game. 
	private int currentLevelIndex; 				//Instantiates an integer which holds the current Level being implemented in the game, used to iterate through the ArrayList<Level> levels
	Game game;							// reference to the game
	public JPanel topLevel;				//The toplevel JPanel holds the board, selectPanel, and stratPanel
	public JPanel bottomLevel;			//The buttonlevel JPanel holds the descriptionPanel and the goButton.
	private BufferedImage background;
	
	public MainGamePanel(Game g, Boolean loadImages) {
		game = g;
		initGUI(loadImages);
	}
	
	private void initGUI(Boolean loadImages){
		
		//We set the location and layout of the descriptionPanel to be along the BoxLayout's Page_Axis
		//descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.PAGE_AXIS));
	
		//Create the layout of the game:
		//Put the Selection panel on the left side, the game board in the center, 
		//the strategy panel on the right, and the description of the level on the bottom.
		
		//Game.APPLET_WIDTH //960
		//Game.APPLET_HEIGHT //600
		//Dimension: Width, Height
				
		background = null;
		
		//Instantiate a new Board with a default constructor
		board = new Board(this);
		// place the board
		Dimension size = board.getPreferredSize();
		board.setBounds((int) Game.APPLET_WIDTH / 5 + 50, 0, size.width, size.height);
		
		//The description Panel holds the goButton, as well as an introductory text.
		descriptionPanel = new DescriptionPanel("HELLO"); 
		size = descriptionPanel.getPreferredSize();
		descriptionPanel.setBounds(0, (int) (Game.APPLET_HEIGHT / 5) * 4 + 20, size.width, size.height);
		
		
		//Initializes the Level onto the board
		initLevels(loadImages);
		
		//Instantiate a new StrategyPanel
		stratPanel = new StratPanel(game);
		//Sets the number of available moves
		//stratPanel.setMaxAvailableMoves(getLevels().get(getCurrentLevelIndex()).getNumOfUsableMoves());
		
		selectPanel = new SelectPanel(stratPanel, game, board);
		//selectPanel.setMaxAvailableMovesInFunctions(getLevels().get(getCurrentLevelIndex()).getNumOfUsableMovesInFunctions());
		
		//Display the moves available to the player based on the currentLevelIndex. 
		//selectPanel.setSelectOptions(getLevels().get(getCurrentLevelIndex()).getAvailableMoves(), getLevels().get(getCurrentLevelIndex()).getCustomFunctionsAvailable());
		stratPanel.setSelectPanel(selectPanel);
		
		//Instantiate a new Button with text "Run". 
		goButton = new RunButton("RUN!");
		//Indicate that our goButton should have an ActionListener to listen for a press.
		goButton.addActionListener(this);
		
		setLayout(new BorderLayout());
		
		
		//Put the Panels on their correct holding JPanel
		topLevel = new JPanel();
		topLevel.setLayout(new BoxLayout(topLevel, BoxLayout.LINE_AXIS));
		topLevel.setPreferredSize(new Dimension((int) Game.APPLET_WIDTH, (int) (Game.APPLET_HEIGHT / 5) * 4));
		topLevel.add(selectPanel);
		topLevel.add(Box.createHorizontalGlue());
		topLevel.add(board);
		topLevel.add(Box.createHorizontalGlue());
		topLevel.add(stratPanel);
		
		
		
		//Put the Panels on their correct holding JPanel
		bottomLevel = new JPanel();
		bottomLevel.setLayout(new BoxLayout(bottomLevel, BoxLayout.LINE_AXIS));
		bottomLevel.setPreferredSize(new Dimension((int) Game.APPLET_WIDTH, (int) (Game.APPLET_HEIGHT / 5)));
		bottomLevel.add(descriptionPanel);
		bottomLevel.add(goButton);
		
		//Setting the backgrounds for the Panels
		Color myGreenBkg = Color.getHSBColor((float).3, (float).35,(float) .8);
		board.setBackground(myGreenBkg);
		topLevel.setBackground(myGreenBkg);
		setBackground(myGreenBkg);
		bottomLevel.setBackground(myGreenBkg);
		
		add(topLevel, BorderLayout.NORTH);
		add(bottomLevel, BorderLayout.SOUTH);
		 
		
		loadLevel(); 
		
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		
		//Run the Player's Strategy.
		if(board.testStrategy(stratPanel.getCurrentStrat())){
			System.out.println("YOU WIN"); 
			//If they're not at the final level, move them up to the next level.
			if(getCurrentLevelIndex() < getLevels().size() -1)
				setCurrentLevelIndex(getCurrentLevelIndex() + 1); 
			
			board.setCurrentLevel(getLevels().get(getCurrentLevelIndex()));
			//Clear the Strategy Panel in preparation of the new level.
			stratPanel.reset(false); 
			
			loadLevel(); 
			
		}

	}
	
	//Sets many of the constraints for each panel
	public void loadLevel(){
		descriptionPanel.setDescription(getLevels().get(getCurrentLevelIndex()).getDescription());
		selectPanel.setSelectOptions(getLevels().get(getCurrentLevelIndex()).getAvailableMoves(), getLevels().get(getCurrentLevelIndex()).getCustomFunctionsAvailable());
		
		
		stratPanel.setMaxAvailableMoves(getLevels().get(getCurrentLevelIndex()).getNumOfUsableMoves()); 
		selectPanel.setMaxAvailableMovesInFunctions(getLevels().get(getCurrentLevelIndex()).getNumOfUsableMovesInFunctions()); 
		
		
		selectPanel.resetNumFunctions();
	}
	
	
	@Override
	public void paintComponent(Graphics theGraphic) {
		Graphics2D g = (Graphics2D) theGraphic;
		if (background != null){
	      	g.drawImage(background, 0, 0, null);
	    }
		
		super.paintComponent(theGraphic);
		
		
		game.validate();
		
		selectPanel.revalidate();
		selectPanel.repaint();
		
		board.revalidate();
		board.repaint();
		
		stratPanel.revalidate();
		stratPanel.repaint();
		
		descriptionPanel.revalidate();
		descriptionPanel.repaint();
			
	}
	
	
	//We create the levels here.
	public void initLevels(Boolean loadImages)
	{
		//levels will hold each of our created levels.
		setLevels(new ArrayList<Level>()); 
		
		//Level 1: Our introduction level.
		//The player simply has to move the character 3 spaces to the right.
		Level l1 = new Level(Board.unitDimension, board); 
		l1.setPlayerSpawnPosition(4, 5);
		
		if(loadImages)
			l1.addGoalAtPosition(7,5, Game.getBufferedImage(Game.goalImage)); 
		else
			l1.addGoalAtPosition(7,5); 
		
		l1.setDescription("Welcome to Buster's Big Break! Add your commands to your strategy using"
						  +" the buttons on the left! Then, hit the RUN! button and try and"
						  +" see if you reach the goal!"); 
		l1.makeRightMoveAvailable();
		l1.setNumOfUsableMoves(400);
		//l1.makeUpMoveAvailable();
		
		levels.add(l1);
		
		//Level 2: Our second level. This requires them to move the character, and then turn after the correct number of spaces.
		Level l2 = new Level(Board.unitDimension, board); 
		l2.setPlayerSpawnPosition(3, 5);
		
		if(loadImages)
			l2.addGoalAtPosition(7,2, game.getBufferedImage(Game.goalImage));
		else
			l2.addGoalAtPosition(7,2);
			
		l2.setDescription("Careful, now! You'll have to turn this time..."); 
		l2.makeRightMoveAvailable();
		l2.makeUpMoveAvailable();
		
		levels.add(l2); 
		
		//Level 3: Our third level. This level introduces the first obstacle. The Player gets the choice of going above or below the obstacle, but cannot go through it.
		Level l3 = new Level(Board.unitDimension, board); 
		l3.setPlayerSpawnPosition(3, 5);
		
		if(loadImages){
			l3.addGoalAtPosition(7,5, game.getBufferedImage(Game.goalImage)); 
			l3.addObstacleAtPosition(5, 5, game.getBufferedImage(Game.enemyImage));
		}
		else{
			l3.addGoalAtPosition(7,5); 
			l3.addObstacleAtPosition(5, 5);
		}
		
		l3.setDescription("UH OH! The evil cats know we're here! Try using your commands to navigate around it."); 
		
		l3.makeDownMoveAvailable();
		l3.makeLeftMoveAvailable();
		l3.makeRightMoveAvailable();
		l3.makeUpMoveAvailable();
		//l3.setCustomFunctionsAvailable(true);
		levels.add(l3);
		
		//Level 4: This introduces while loops
		Level l4 = new Level(Board.unitDimension, board); 
		l4.setPlayerSpawnPosition(0, 5); 
		if(loadImages)
			l4.addGoalAtPosition(9, 5, game.getBufferedImage(Game.goalImage));
		else
			l4.addGoalAtPosition(9, 5);
		l4.setDescription("Wow, that goal sure is far away, Try using a loop "
				+ "to get yourself there without just using 'right' over and over again. "
				+ "To use a loop, use the Loop Button and then press the commands that you want to repeat.");
		
		l4.makeRightMoveAvailable();
		l4.makeWhileMoveAvailable(); 

		//l4.makeConditionalMoveAvailable();
		l4.setNumOfUsableMoves(4);
		
		getLevels().add(l4); 
		
		//Level 5: this makes while loops more complicated 
		Level l5 = new Level(Board.unitDimension, board); 
		l5.setPlayerSpawnPosition(0, 8);

		if(loadImages)
			l5.addGoalAtPosition(7, 1, game.getBufferedImage(game.goalImage));
		else
			l5.addGoalAtPosition(7, 1);
		l5.makeRightMoveAvailable();
		l5.makeUpMoveAvailable();
		l5.makeWhileMoveAvailable();

		l5.setNumOfUsableMoves(4);
		l5.setDescription("This one is really challenging!  Think you're up to the challenge?  Here you will need to make two separate moves loop! You only need to press Loop once, and then everything below it will loop.");
		
		getLevels().add(l5); 
		
		//Level 6: this introduces user defined functions
		Level l6 = new Level(Board.unitDimension, board); 
		l6.setPlayerSpawnPosition(0,  7);
		if(loadImages)
			l6.addGoalAtPosition(4, 5, game.getBufferedImage(game.goalImage));
		else
			l6.addGoalAtPosition(4, 5);
		
		l6.makeRightMoveAvailable();
		l6.makeUpMoveAvailable();
		l6.setCustomFunctionsAvailable(true); 
		l6.setNumOfUsableMoves(5); 
		l6.setNumOfUsableMovesInFunctions(4);
		l6.setDescription("Use a function! They only count as one move, and a function is like a nickname for a collection of moves."
				+" Press the Create a function button, then name it. Press moves you want to put in the function, and then press the Finish Function. Next, add the function to the Strategy by clicking on it. ");
		getLevels().add(l6); 

		
		//Level 7: This makes the player put their own functions in a while loop
		Level l7 = new Level(Board.unitDimension, board); 
		l7.setPlayerSpawnPosition(0, 8);
		if(loadImages)
			l7.addGoalAtPosition(7, 1, game.getBufferedImage(game.goalImage));
		else
			l7.addGoalAtPosition(7, 1);
		
		l7.makeRightMoveAvailable();
		l7.makeUpMoveAvailable();
		l7.makeWhileMoveAvailable();
		l7.setNumOfUsableMoves(2);
		l7.setCustomFunctionsAvailable(true); 
		l7.setDescription("Hey, this looks familiar! But this time you want to Loop your function. ");
		l7.setNumOfUsableMovesInFunctions(2); 
		getLevels().add(l7); 
		
		//Level 8: this introduces conditional Statements
		Level l8 = new Level(Board.unitDimension, board);
		BufferedImage evil = null; 
		if(loadImages){
			evil = game.getBufferedImage(Game.enemyImage);
		}
		
		l8.setPlayerSpawnPosition(0, 5);
		l8.addRedSquareAtPosition(2,5);
		l8.addRedSquareAtPosition(2,8); 
		if(loadImages)
			l8.addGoalAtPosition(9, 4, game.getBufferedImage(Game.goalImage));
		else
			l8.addGoalAtPosition(9, 4);
		l8.addObstacleAtPosition(2, 0, evil);
		l8.addObstacleAtPosition(2, 1, evil);
		l8.addObstacleAtPosition(2, 2, evil);
		l8.addObstacleAtPosition(2, 3, evil);
		l8.addObstacleAtPosition(3, 3, evil);
		l8.addObstacleAtPosition(4, 3, evil);
		l8.addObstacleAtPosition(5, 3, evil);
		l8.addObstacleAtPosition(6, 3, evil);
		l8.addObstacleAtPosition(7, 3, evil);
		l8.addObstacleAtPosition(8, 3, evil);
		l8.addObstacleAtPosition(9, 3, evil);
		l8.addObstacleAtPosition(3, 5, evil);
		l8.addObstacleAtPosition(4, 5, evil);
		l8.addObstacleAtPosition(5, 5, evil);
		l8.addObstacleAtPosition(6, 5, evil);
		l8.addObstacleAtPosition(7, 5, evil);
		l8.addObstacleAtPosition(8, 5, evil);
		l8.addObstacleAtPosition(2, 6, evil);
		l8.addObstacleAtPosition(2, 7, evil);
		l8.addObstacleAtPosition(2, 8, evil);
		l8.addObstacleAtPosition(2, 9, evil);//*/
		l8.setDescription("Try using the Conditional Block here!"
				+ " To use it, press the Conditional button, then the action you want the Conditional block to look out for. Next, press the move you want carried out if Buster is on the block."
				+ " Finally, press the } Button to end the Conditional block.");
		l8.makeRightMoveAvailable();
		l8.makeUpMoveAvailable();
		l8.makeDownMoveAvailable();
		l8.makeWhileMoveAvailable();
		l8.makeConditionalMoveAvailable();
		l8.setNumOfUsableMoves(5);
		
		
		getLevels().add(l8);
		
		//Load the correct Level.
		board.setCurrentLevel(getLevels().get(0));
		descriptionPanel.setDescription(getLevels().get(0).getDescription()); 
	}

	
	public void setPlayerImage(BufferedImage image)
	{
		board.setPlayerImage(image);
	}
	
	public void setPlayerVisitedMark(BufferedImage image){
		board.setPlayerVisitedMark(image); 
	}

	public ArrayList<Level> getLevels() {
		return levels;
	}

	public void setLevels(ArrayList<Level> levels) {
		this.levels = levels;
	}

	public int getCurrentLevelIndex() {
		return currentLevelIndex;
	}

	
	public void setCurrentLevelIndex(int newCurrentLevelIndex) {
		this.currentLevelIndex = newCurrentLevelIndex;
	}
	
	public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height, int type) throws IOException {  
        BufferedImage resizedImage = new BufferedImage(width, height, type);  
        Graphics2D g = resizedImage.createGraphics();  
        g.drawImage(originalImage, 0, 0, width, height, null);  
        g.dispose();
        return resizedImage;  
    }  
	
	
}
