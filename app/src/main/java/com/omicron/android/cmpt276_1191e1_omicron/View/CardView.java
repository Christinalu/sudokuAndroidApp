package com.omicron.android.cmpt276_1191e1_omicron.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AppComponentFactory;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

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
	private int[][] viewInvisible; //1==view is invisible
	private int i = 0;
	private int j = 0;
	private Pair[] selectedPairLast; //first pair stores first selected card, second stores second selected
	
	
	public CardView(int rowCount2, int colCount2, RelativeLayout relativeLayout, Context context )
	{
		rowCount = rowCount2;
		colCount = colCount2;
		tableLayout = new TableLayout( context );
		viewInvisible = new int[rowCount][colCount];
		
		selectedPairLast = new Pair[2];
		selectedPairLast[0] = new Pair( -1, -1); //no first card selected
		selectedPairLast[1] = new Pair( -1, -1); //no second card selected
		
		relativeLayout.addView( tableLayout );
		
		//gridLayout.setRowCount( rowCount );
		//gridLayout.setColumnCount( colCount );
		
		for( int i=0; i<rowCount; i++ )
		{
			//add new row
			TableRow tableRow = new TableRow( context );
			tableLayout.addView( tableRow );
			for( int j=0; j<colCount; j++ )
			{
				//Log.d( "cardView", "loop i: " + i + ", j: " + j );
				ImageView imgView = new ImageView( context );
				imgView.setBackgroundColor( Color.parseColor( "#00ffff" ) );
				
				TableRow.LayoutParams param =new TableRow.LayoutParams();
				param.height = 120; // GridLayout.LayoutParams.WRAP_CONTENT;
				param.width = 400; // GridLayout.LayoutParams.WRAP_CONTENT;
				
				if( j<colCount ) {
					param.rightMargin = 55;
					param.bottomMargin = 25;
				}
				//param.setGravity(Gravity.CENTER);
				//param.columnSpec = GridLayout.spec( j );
				//param.rowSpec = GridLayout.spec( i );
				
				imgView.setLayoutParams( param );
				imgView.setImageResource( R.drawable.newlogo );
				
				tableRow.addView( imgView, param );
				
			}
		}
		
		
		setUpListeners( );
		
		
		
		
		
		//// test ////////////////
		
		//shortAnimationDuration = getResources().getInteger( android.R.integer.config_shortAnimTime );
		
		
		
		
//		(tableLayout.getChildAt( 2 )).setOnTouchListener(new View.OnTouchListener()
//		{
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event)
//			{
//				switch( event.getAction( ) )
//				{
//					case MotionEvent.ACTION_DOWN:
//						Toast.makeText( v.getContext(), "img pressed", Toast.LENGTH_SHORT).show();
//				}
//				return true;
//			}
//		});
		
		//////////////////////////
		
		
		
		
		
		//relativeLayout.invalidate( );
	}
	
	
	private void setUpListeners( )
	{
		/*
		 * This class sets up the card listeners to fade in and out
		 * and also respond to when two cards are selected
		 */
		
		final int shortAnimationDuration = 200;
		//final int col;
		//final int row;
		
		for( i=0; i<rowCount; i++ ) //
		{
			final int row = i;
			for( j=0; j<colCount; j++ ) //
			{
				final int col = j;
				
				//add on touch listener to respond by fading showing text
				( ( (TableRow)( tableLayout.getChildAt( i )) ).getChildAt(j) )
										.setOnTouchListener( new View.OnTouchListener( )
				{
					@Override
					public boolean onTouch( View v, MotionEvent event )
					{
						switch( event.getAction( ) )
						{
							case MotionEvent.ACTION_DOWN:
								Log.d( "cardArray", "selected card: ( " + row + ", " + col + " )" );
								
								if( viewInvisible[row][col] == 0 ) //if card touched not previously selected
								{
									if( selectedPairLast[0].getRow() != -1 ) 	//if another card previously selected
																				// (other than this) - check for match
									{
										// --if second card selected correct
										
										// --if second card selected incorrect
										
										//todo: deselect cards afterwards (using selectedPair[])
										//todo:		+ add fade in animation if incorrect; stop animation (but leave words) if correct
									}
									else //no other card previously selected - select this one
									{
										viewInvisible[row][col] = 1; //mark as invisible - selected
										
										//add card selected to selectedPair
										selectedPairLast[0].update( row, col );
										
										//add fade out animation
										(((TableRow) (tableLayout.getChildAt( row ))).getChildAt( col )).animate().alpha(0f)
												.setDuration(shortAnimationDuration)
												.setListener(new AnimatorListenerAdapter() {
													@Override
													public void onAnimationEnd(Animator animation) {
														(((TableRow) (tableLayout.getChildAt( row ))).getChildAt( col )).setAlpha(0);
													}
												});
									}
								}
						}
						return true;
					}
				});
			}
		}
	}
}








































