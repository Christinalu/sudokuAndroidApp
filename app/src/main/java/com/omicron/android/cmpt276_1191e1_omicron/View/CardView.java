package com.omicron.android.cmpt276_1191e1_omicron.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.omicron.android.cmpt276_1191e1_omicron.Model.CardArray;
import com.omicron.android.cmpt276_1191e1_omicron.Model.Pair;
import com.omicron.android.cmpt276_1191e1_omicron.R;

public class CardView extends Activity
{
	/*
	 * This View class holds the image views of the cards
	 * It also holds listener initialization
	 */
	
	private int rowCount; //how many rows in card array
	private int colCount;
	private TableLayout tableLayout; //id of grid layout
	private TableLayout tableLayoutText;
	private int[] viewInvisible; //1==view is invisible (text shown)
	private int i = 0;
	private int j = 0;
	private int[] selectedLast; //first index stores first selected card (1D array format), second stores second selected
	private boolean[] allowToSelect = { true }; //only let user select another pair after animation ended
	
	private int size; //number of all cards
	private int rowLast = -1; //save to use after fade out animation runs
	private int colLast = -1;
	private int screenH;
	private int screenW;
	private int edgeOffset; //edge offset for relativeLayout
	
	private int cardWidth;
	private int cardHeight;
	private int barH; //height of top menu bar
	private final static int RIGHT_MARGIN_OFFSET = 50;
	private final static int BOTTOM_MARGIN_OFFSET = 20;
	
	private final int shortAnimationDuration = 300;
	private final int animationDelay = 1000;
	
	
	public CardView(int rowCount2, int colCount2, RelativeLayout relativeLayout, Context context,
					CardArray cardArray, int screenW2, int screenH2, int edgeOffset2, int barH2 )
	{
		rowCount = rowCount2;
		colCount = colCount2;
		size = rowCount * colCount;
		screenW = screenW2;
		screenH = screenH2;
		edgeOffset = edgeOffset2;
		barH = barH2;
		tableLayout = new TableLayout( context );
		tableLayoutText = new TableLayout( context );
		viewInvisible = new int[rowCount*colCount];
		
		selectedLast = new int[2];
		selectedLast[0] = -1; //no first card selected
		selectedLast[1] = -1; //no second card selected
		
		relativeLayout.addView( tableLayout );
		relativeLayout.addView( tableLayoutText );
		
		//gridLayout.setRowCount( rowCount );
		//gridLayout.setColumnCount( colCount );
		
		cardWidth = (int)( (screenW - 2*edgeOffset - (colCount-1)*RIGHT_MARGIN_OFFSET) /colCount );
		cardHeight = (int)( (screenH - 2*edgeOffset - (rowCount-1)*BOTTOM_MARGIN_OFFSET - barH)/rowCount );
		
		for( int i=0; i<rowCount; i++ )
		{
			//add new row
			TableRow tableRow = new TableRow( context );
			TableRow tableRowText = new TableRow( context );
			tableLayout.addView( tableRow );
			//tableLayout.addView( tableRowText );
			
			for( int j=0; j<colCount; j++ )
			{
				//Log.d( "cardView", "loop i: " + i + ", j: " + j );
				//RelativeLayout relativeLayoutForImg = new RelativeLayout( context );
				RelativeLayout relativeLayoutCard = new RelativeLayout( context );
				ImageView imgView = new ImageView( context );
				TextView textView = new TextView( context );
				
				textView.setText( cardArray.getCardStringAtIndex( i, j, colCount ) );
				textView.setBackgroundResource( R.drawable.card_text_selected );
				textView.setTextSize( 16 );
				textView.setEllipsize( TextUtils.TruncateAt.MARQUEE );
				textView.setSingleLine( true );
				textView.setMarqueeRepeatLimit( -1 );
				textView.setGravity( Gravity.CENTER );
			
				
				TableRow.LayoutParams param =new TableRow.LayoutParams();
				param.height = cardHeight; // GridLayout.LayoutParams.WRAP_CONTENT;
				param.width = cardWidth; // GridLayout.LayoutParams.WRAP_CONTENT;
				
				if( i<rowCount-1 ){ //add padding in between rows
					param.bottomMargin = BOTTOM_MARGIN_OFFSET;
				}
				if( j<colCount-1 ){ //add padding in between columns
					param.rightMargin = RIGHT_MARGIN_OFFSET;
				}
				
				
				imgView.setLayoutParams( param );
				imgView.setImageResource( R.drawable.card );
				
				relativeLayoutCard.addView( textView, param );
				relativeLayoutCard.addView( imgView, param );
				tableRow.addView( relativeLayoutCard );
				
			}
		}
		
		
		setUpListeners( cardArray );
	}
	
	
	private void setUpListeners(final CardArray cardArray )
	{
		/*
		 * This class sets up the card listeners to fade in and out
		 * and also respond to when two cards are selected
		 */
		
		
		//final int col;
		//final int row;
		
		for( i=0; i<rowCount; i++ ) //
		{
			final int row = i;
			for( j=0; j<colCount; j++ ) //
			{
				final int col = j;
				
				//add on touch listener to respond by fading card to show text
				( (RelativeLayout)( (TableRow)( tableLayout.getChildAt( i )) ).getChildAt(j) ).getChildAt( 1 )
										.setOnTouchListener( new View.OnTouchListener( )
				{
					@Override
					public boolean onTouch( View v, MotionEvent event )
					{
						switch( event.getAction( ) )
						{
							case MotionEvent.ACTION_DOWN:
								singleCardTouch( row, col, cardArray );
						}
						return true;
					}
				});
			}
		}
	}
	
	
	public void singleCardTouch( final int row, final int col, CardArray cardArray )
	{
			/* on ACTION_DOWN see if selected squares match */
			// FOR ONE CARD, PROCESS TOUCH
			// this fades the card to reveal the word
		
			// NOTE: TableLayout is a 2D grid, however the String and Card data are 1D
		
		//if card touched not previously selected; only allow to select after animation
		if( viewInvisible[row*colCount+col] == 0 && allowToSelect[0] == true )
		{
			Log.d( "cardArray", "selected card: ( " + row + ", " + col + " )" );
			
			viewInvisible[row*colCount+col] = 1; //mark as invisible - selected, show text
			
			if( selectedLast[0] != -1 ) //if another card previously selected (other than this), check for match
			{
				allowToSelect[0] = false; //do not allow to select while animating
				
					/* IF CARD PAIR SELECTED CORRECT */
				
				selectedLast[1] = row*colCount + col; //update with current selected coordinate Pair
				
				//get 2D coordinates from 1D
				rowLast = selectedLast[0] / colCount;
				colLast = selectedLast[0] - (selectedLast[0]/colCount)*colCount;
				
				Log.d( "cardArray", "rowLast: " + rowLast + " colLasr: " + colLast );
				
				if( cardArray.checkIfPairMatch( selectedLast[0], selectedLast[1] ) )
				{
					//change text background colour
					((RelativeLayout) (((TableRow) (tableLayout.getChildAt( row ))).getChildAt( col )))
							.getChildAt(0).setBackgroundResource( R.drawable.card_text_correct );
					((RelativeLayout) (((TableRow) (tableLayout.getChildAt( rowLast ))).getChildAt( colLast )))
							.getChildAt(0).setBackgroundResource( R.drawable.card_text_correct );
					
					
					Log.d( "cardArray", "card match..." );
					
					//add fade out animation to show text
					((RelativeLayout) (((TableRow) (tableLayout.getChildAt( row ))).getChildAt( col )))
							.getChildAt(1).animate().alpha(0f)
							.setDuration(shortAnimationDuration).setStartDelay(0)
							.setListener(new AnimatorListenerAdapter() {
								@Override
								public void onAnimationEnd(Animator animation) {
									//disable both card view
									((RelativeLayout) (((TableRow) (tableLayout.getChildAt( row )))
											.getChildAt( col ))).getChildAt(1).setVisibility( View.INVISIBLE );
									((RelativeLayout) (((TableRow) (tableLayout.getChildAt( rowLast )))
											.getChildAt( colLast ))).getChildAt(1).setVisibility( View.INVISIBLE );
									
									allowToSelect[0] = true; //once animation finished allow to select next pair
								}
							});
					
					//remove any selected Pair
					selectedLast[0] = -1; //remove first selected
					selectedLast[1] = -1; //remove current selected
					
					// TODO: check if all cards completed - finish game
					// TODO: check if user can select/play/cheat while card animating
					// TODO: change colour of selected vs correct
				}
				else
				{
						/* IF CARD PAIR SELECTED INCORRECT */
					
					//deselect cards from viewInvisible[][] since we hide cards again
					viewInvisible[row*colCount+col] = 0; //hide current
					viewInvisible[rowLast*colCount+colLast] = 0; //hide previously selected
					
					//remove any selected Pair
					selectedLast[0] = -1; //remove first selected
					selectedLast[1] = -1; //remove current selected
					
					//add fade out/in animation to show text
					allowToSelect[0] = false; //do not allow to select while animating
					
					((RelativeLayout) (((TableRow) (tableLayout.getChildAt( row ))).getChildAt( col )))
							.getChildAt(1).animate().alpha(0f)
							.setDuration(shortAnimationDuration).setStartDelay( 0 )
							.setListener(new AnimatorListenerAdapter() {
								@Override
								public void onAnimationEnd(Animator animation) {
									((RelativeLayout) (((TableRow) (tableLayout.getChildAt( row ))).getChildAt( col )))
											.getChildAt(1).setAlpha(0);
									
									//add fade in animation to hide words
									
									Log.d( "cardArray", "selectedPairLast[0] row: " + rowLast + " col: " + colLast );
									
									//hide first word
									((RelativeLayout) (((TableRow) (tableLayout.getChildAt( rowLast )))
											.getChildAt( colLast ))).getChildAt(1).animate().alpha(1f)
											.setDuration(shortAnimationDuration).setStartDelay( animationDelay )
											.setListener(new AnimatorListenerAdapter() {
												@Override
												public void onAnimationEnd(Animator animation) {
													((RelativeLayout) (((TableRow) (tableLayout.getChildAt( rowLast )))
															.getChildAt( colLast ))).getChildAt(1).setAlpha(1);
													allowToSelect[0] = true; //once animation finished allow to select next pair
												}
											});
									
									//hide current word
									((RelativeLayout) (((TableRow) (tableLayout.getChildAt( row ))).getChildAt( col )))
											.getChildAt(1).animate().alpha(1f)
											.setDuration(shortAnimationDuration).setStartDelay( animationDelay )
											.setListener(new AnimatorListenerAdapter() {
												@Override
												public void onAnimationEnd(Animator animation) {
													((RelativeLayout) (((TableRow) (tableLayout.getChildAt( row )))
															.getChildAt( col ))).getChildAt(1).setAlpha(1);
													allowToSelect[0] = true; //once animation finished allow to select next pair
												}
											});
								}
							});
					
					
					
					//todo: deselect cards afterwards (using selectedPair[]) in viewInvisible[][]
					//todo:		+ add fade in animation if incorrect; stop animation (but leave words) if correct
					//todo: reset selected pair in selectedPairLast[][]
				}
			}
			else //no other card previously selected - select this one
			{
				//add card selected to selectedPair
				selectedLast[0] = row*colCount + col;
				
				//add fade out animation to show text
				((RelativeLayout) (((TableRow) (tableLayout.getChildAt( row ))).getChildAt( col )))
						.getChildAt(1).animate().alpha(0f)
						.setDuration(shortAnimationDuration).setStartDelay( 0 )
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								((RelativeLayout) (((TableRow) (tableLayout.getChildAt( row )))
										.getChildAt( col ))).getChildAt(1).setAlpha(0);
							}
						});
			}
			
			
		}
	}
	
	
	public void saveDataForRotation( Bundle state )
	{
		/*
		 * This function adds all necessary data to intent to be saved for rotation
		 */
		
		state.putSerializable( "viewInvisible", viewInvisible );
		//state.putBoolean( "allowToSelect", allowToSelect[0] );
		state.putSerializable( "selectedLast", selectedLast[0] );
	}
	
	public void saveDataForResume( Intent intent )
	{
		/*
		 * This function adds all necessary data to intent to be saved for resume
		 */
		
		intent.putExtra( "viewInvisible", viewInvisible );
		//intent.putExtra( "allowToSelect", allowToSelect[0] );
		intent.putExtra( "selectedLast", selectedLast[0] );
	}
	
	
	public void reDrawOnRotation( Bundle state )
	{
		/*
		 * This function re draws the cards when device rotated
		 * and restores View data
		 */
		
		viewInvisible = (int[]) state.getSerializable( "viewInvisible" );
		//allowToSelect[0] = state.getBoolean( "allowToSelect" );
		selectedLast[0] = (int) state.getSerializable( "selectedLast" );
		
		reDrawCard( );
	}
	
	
	public void reDrawOnResume( Intent intent )
	{
		/*
		 * This function re draws the cards when resuming previous game
		 * and restores View data
		 */
		
		viewInvisible = (int[]) intent.getSerializableExtra( "viewInvisible" );
		//allowToSelect[0] = (boolean) intent.getSerializableExtra( "allowToSelect" );
		selectedLast[0] = (int) intent.getSerializableExtra( "selectedLast" );
		
		reDrawCard( );
	}
	
	
	private void reDrawCard( )
	{
		/*
		 * This function redraws all cards after rotation
		 */
		
		Log.d( "cardArray", "selectedLast: " + selectedLast[0] );
		
		// LOOP AND DRAW ALL CARDS SELECTED OR MATCHED
		for( int k=0; k<size; k++ ) //
		{
			// show all cards that were matched
			if( viewInvisible[k] == 1 ) //if card already matched or selected
			{
				int row = k / colCount;
				int col = k - (k/colCount)*colCount;
				
				//change text background colour
				((RelativeLayout) (((TableRow) (tableLayout.getChildAt( row ))).getChildAt( col )))
						.getChildAt(0).setBackgroundResource( R.drawable.card_text_correct );
					
				//reveal card
				((RelativeLayout) ((TableRow) (tableLayout.getChildAt( row ))).getChildAt( col )) //get card
						.getChildAt(1).setAlpha( 0f );
							
				//temporarely disable card
				((RelativeLayout) ((TableRow) (tableLayout.getChildAt( row ))).getChildAt( col )) //get card
						.getChildAt(1).setVisibility( View.INVISIBLE );
			}
		}
		
		// RE DRAW SINGLE SELECTED CARD
		if( selectedLast[0] != -1 ) //if previously selected a card
		{
			int n = selectedLast[0] / colCount;
			int m = selectedLast[0] - (selectedLast[0]/colCount)*colCount;
			
			//change text background colour
			((RelativeLayout) (((TableRow) (tableLayout.getChildAt( n ))).getChildAt( m )))
					.getChildAt(0).setBackgroundResource( R.drawable.card_text_selected );
			
			//enable card
			((RelativeLayout) ((TableRow) (tableLayout.getChildAt( n ))).getChildAt( m )) //get card
					.getChildAt(1).setVisibility( View.VISIBLE );
		}
	}
}








































