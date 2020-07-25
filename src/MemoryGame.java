import java.util.ArrayList;
import java.util.Collections;
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
import javafx.util.Duration;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.animation.FadeTransition;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.Pane;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class MemoryGame extends Application{

	//List of cards
	static List<Card> cards = new ArrayList<Card>();

	//Static constants
	private static final int NUM_OF_ROW = 4;
	private static final int NUM_OF_COL = 4;
	private static final int NUM_OF_PAIRS = 8;

	//static fields
	private static int matches = 0;
	private static IntegerProperty matches1 = new SimpleIntegerProperty(0);
	private static int flipped = 0;


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

	private static class Card extends StackPane {

		public ImageView card;
		static String card1Name = " ";
		static String card2Name = " ";
		private boolean isFlipped;
		static int card1Index = 0;
		static int card2Index = 0;
		Card c1, c2;

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
			Close();
		}

		public void Close() {
			isFlipped = false;
			FadeTransition ft = new FadeTransition(Duration.millis(100), card);
			ft.setToValue(0);
			ft.play();
		}

		public void CardClose() {
			isFlipped = false;
			FadeTransition ft = new FadeTransition(Duration.millis(300), card);
			ft.setToValue(0);
			ft.play();
		}

		public void Open(Runnable action) {
			isFlipped = true;
			FadeTransition ft = new FadeTransition(Duration.millis(700), card);
			ft.setToValue(2);
			ft.setOnFinished(e -> action.run());
			ft.play();			

		}


		public void Select() {

			card.setOnMouseClicked(e -> {			
				Open(()->{});

				
				if(flipped == 0) {

					/*This method for finding the name of the card(from image):
					 *https://stackoverflow.com/questions/25123115/
					 *get-image-path-javafx?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
					*/
					Card.card1Name = card.getImage() instanceof LocatedImage ? ((LocatedImage) card.getImage()).getName() : null;
					System.out.println("Card1Name: " + card1Name);
					Card.card1Index = FindIndex();
					System.out.println("Card1Index: " + card1Index);
					flipped++;
				}
				else
					if(flipped == 1){

						Card.card2Name = card.getImage() instanceof LocatedImage ? ((LocatedImage) card.getImage()).getName() : null;
						System.out.println("Card2Name: " + card2Name);
						Card.card2Index = FindIndex();
						System.out.println("Card2Index: " + card2Index);
						flipped++;
					}

				if(flipped == 2) {	

					Open(() -> {
						if(CheckMatch(card1Name, card2Name, card1Index, card2Index)) {

							c1.CardClose();
							c2.CardClose();

						}
						else {
							c1.CardClose();
							c2.CardClose();
						}
					});

				}								

			});

		}

		public int FindIndex() {
			int cardIndex = 0;
			for(int i = 0; i < cards.size(); i++) {
				if(cards.get(i).isFlipped && flipped == 0) {
					//System.out.println("Loop" + i);
					cardIndex = i;
					cards.get(i).isFlipped = false;
				}
				else 
					if (cards.get(i).isFlipped && flipped == 1) {
						cardIndex = i;
						cards.get(i).isFlipped = false;
					}

			}
			return cardIndex;
		}

		//Processes matches
		public boolean CheckMatch(String card1Name, String card2Name, int card1Index, int card2Index)
		{


			c1 = cards.get(card1Index);
			c2 = cards.get(card2Index);
			if(card1Index == card2Index)
			{
				flipped = 0;
				System.out.println("That is the same position! Sneaky sneaky...");
				System.out.println("Match Count: " + matches);	
				System.out.println("MATCH Observable count: " + matches1.intValue());
				return false;
			
			}
			else if(card1Name.equals(card2Name))
			{
				flipped = 0;					
				matches++;
				matches1.set(matches1.intValue() + 1);
				System.out.println("IT'S A MATCH");
				System.out.println("Match Count: " + matches);	
				System.out.println("MATCH Observable count: " + matches1.intValue());
				return true;

			}
			else
			{
				flipped = 0;	
				matches = 0;
				matches1.set(0);
				System.out.println("NOT A MATCH");						
				return false;
			}		

		}	

	}

	@Override
	public void start(Stage stage) throws Exception {

		BorderPane bdPane = new BorderPane();
		GridPane grdPane = new GridPane();
		HBox hbx = new HBox(100);
		Button qBttn = new Button("QUIT");		
		Pane qPane = new Pane();	
		Pane wPane = new Pane();


		qPane.setPrefSize(520, 640);
		Rectangle qRec = new Rectangle();
		qRec.widthProperty().bind(qPane.widthProperty());
		qRec.heightProperty().bind(qPane.heightProperty());
		qRec.setFill(Color.ORANGERED);		
		Text qText = new Text("YOU QUIT");
		qText.setFont(Font.font("Verdana", 60));
		qText.xProperty().bind(qPane.widthProperty().divide(4));
		qText.yProperty().bind(qPane.heightProperty().divide(2));
		qPane.getChildren().addAll(qRec, qText);
		
		wPane.setPrefSize(520, 640);
		Rectangle wRec = new Rectangle();
		wRec.widthProperty().bind(wPane.widthProperty());
		wRec.heightProperty().bind(wPane.heightProperty());
		wRec.setFill(Color.BLUE);		
		Text wText = new Text("YOU WIN!");
		wText.setFont(Font.font("Verdana", 60));
		wText.xProperty().bind(wPane.widthProperty().divide(4));
		wText.yProperty().bind(wPane.heightProperty().divide(2));
		wPane.getChildren().addAll(wRec, wText);

		
		Text matchText = new Text("Consecutive Matches: " + matches1.intValue());
		
		hbx.getChildren().addAll(matchText, qBttn);
		
		
		//Listener class for matches
		matches1.addListener(new InvalidationListener() {
			public void invalidated(Observable ov) {
				System.out.println("The new value of matches1 is " + 
						matches1.intValue());  
				
				matchText.setText("Consecutive Matches: " + matches1.intValue());
				
				//WIN CONDITION
				if(matches1.intValue() == 3)
				{
					grdPane.getChildren().clear();			
					bdPane.setCenter(wPane);
				}

			}
		});

		//Quit Button events
		qBttn.setOnMouseClicked(e -> {

			grdPane.getChildren().clear();			
			bdPane.setCenter(qPane);


		});

		

		//Cards		
		//ArrayList Generator		
		for(int n = 1; n <= NUM_OF_PAIRS; n++) {
			cards.add(new Card(new LocatedImage("card" + n + ".png")));			
			cards.add(new Card(new LocatedImage("card" + n + ".png")));
		}

		Collections.shuffle(cards);


		//CardFillers		
		int cardIndex = 0;
		for(int i = 0; i < NUM_OF_COL; i++) {
			for(int j = 0; j < NUM_OF_ROW; j++, cardIndex++) {								
				cards.get(cardIndex).Select();
				grdPane.add(cards.get(cardIndex), i, j);						

			}
		}		
		
	


		
		bdPane.setCenter(grdPane); 
		bdPane.setBottom(hbx);
		hbx.setAlignment(Pos.BOTTOM_RIGHT);

		Scene scene = new Scene(bdPane);
		stage.setScene(scene);
		stage.setTitle("MEMORY GAME");
		stage.show();	


	}		



	public static void main(String[] args) {

		launch(args);

	}

}

