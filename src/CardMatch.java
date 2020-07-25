/*
 * Class: CS 1302/03
 * Term: Spring 2018
 * Names: Julian Robinson and Evan Gevara
 * Instructor: Monisha Verma
 * Assignment: Project 2 Memory Game
 */


import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class CardMatch extends Application{
	//List of cards
	static List<Card> cards = new ArrayList<Card>();
	
	//static Constants
	private static final int NUM_OF_ROW = 4;
	private static final int NUM_OF_COL = 4;
	private static final int NUM_OF_PAIRS = 8;

	private static IntegerProperty matches = new SimpleIntegerProperty(0);
	private static boolean win = false;

	
	//New class to find name of card from Image
	class LocatedImage extends Image {
	    private final String name;

	    public LocatedImage(String name) {
	        super(name);
	        this.name = name;
	    }

	    public String getName() {
	        return name;
	    }
	}
	
	private class Card extends StackPane{

		public ImageView card;
			
		//Constructor
		public Card(LocatedImage iCard) {

			card = new ImageView(iCard);

			setPrefSize(130, 160);
			Rectangle rec = new Rectangle(128,158);
			rec.setFill(Color.TRANSPARENT);
			rec.setStroke(Color.DARKGREEN);
			rec.setStrokeWidth(2);
			card.setFitWidth(120);
			card.setFitHeight(150);
			setAlignment(Pos.CENTER);
			getChildren().addAll(rec, card);
			
		
		}
		
		//Method for checking if the two cards that are flipped are matches
		public void Check() {
		for(int i = 0, j = 0, k = 1; i <= 7; i++, j = j+2, k = k+2) {
			
			if(i == 0) {
				match(cards.get(i), cards.get(i+1));
			}
			else
				match(cards.get(j), cards.get(k));
		}
	  }
	}
		

	@Override
	public void start(Stage stage) throws Exception {

		BorderPane bdPane = new BorderPane();
		GridPane grdPane = new GridPane();
		HBox hbx = new HBox(50);
		Button qBttn = new Button("QUIT");		



		//Cards		
		//ArrayList Generator		
		for(int n = 1; n <= NUM_OF_PAIRS; n++) {
			cards.add(new Card(new LocatedImage("card" + n + ".png")));			
			cards.add(new Card(new LocatedImage("card" + n + ".png")));
		}

		
		
		//CardFillers		
		int cardIndex = 0;
		for(int i = 0; i < NUM_OF_COL; i++) {
			for(int j = 0; j < NUM_OF_ROW; j++, cardIndex++) {								
				cards.get(cardIndex);
				grdPane.add(cards.get(cardIndex), i, j);						

			}
		}	
		
		//Listener class for matches
		matches.addListener(new InvalidationListener() {
		      public void invalidated(Observable ov) {
		        System.out.println("The new value of matches is " + 
		          matches.intValue());
		        
		        
		      }
		    });
		
		cards.get(0).Check();
		

		hbx.getChildren().add(qBttn);
		bdPane.setCenter(grdPane); 
		bdPane.setBottom(hbx);
		hbx.setAlignment(Pos.BOTTOM_RIGHT);

		Scene scene = new Scene(bdPane);
				

		stage.setScene(scene);
		stage.setTitle("MEMORY GAME");
		stage.show();	

		
	}	

	public static void match(Card c1, Card c2)
	{
	
		/*This method for finding the name of the card(from image):
		 *https://stackoverflow.com/questions/25123115/
		 *get-image-path-javafx?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
		*/
				
		String name1 = c1.card.getImage() instanceof LocatedImage ? ((LocatedImage) c1.card.getImage()).getName() : null;
		String name2 = c2.card.getImage() instanceof LocatedImage ? ((LocatedImage) c2.card.getImage()).getName() : null;
		
		
		if(name1.equals(name2))
		{
			System.out.println(name1);
			System.out.println(name2);
			System.out.println("IT'S A MATCH!");
			matches.add(1);
			
		}
		else
		{
			System.out.print("NOT A MATCH");
			matches.set(0);
		}
	}


	public static void main(String[] args) {

		launch(args);

	}

}

