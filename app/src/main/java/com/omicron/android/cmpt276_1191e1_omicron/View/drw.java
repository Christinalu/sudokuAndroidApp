package com.omicron.android.cmpt276_1191e1_omicron.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.omicron.android.cmpt276_1191e1_omicron.Model.Block;
import com.omicron.android.cmpt276_1191e1_omicron.Model.Pair;
import com.omicron.android.cmpt276_1191e1_omicron.SudokuGenerator;
import com.omicron.android.cmpt276_1191e1_omicron.WordArray;

public class drw
{
	private Paint paint;
	private boolean newSqrTouched;
	private Block[][] rectArr;
	private Canvas canvas;
	private RelativeLayout rectLayout;
	private RelativeLayout rectTextLayout;
	private TextMatrix textMatrix;
	private SudokuGenerator usrSudokuArr;

	private int px;
	private int py;
	private int[] zoomOn;
	private int[] drag;
	private int[] dX;
	private int[] dY;
	private int[] touchXZ;
	private int[] touchYZ;
	private int[] zoomButtonSafe;
	private int[] zoomClickSafe;
	private int[] zoomButtonDisableUpdate;
	private int[] btnClicked;
	private int bitmapSizeWidth;
	private int bitmapSizeHeight;
	private int barH;
	private ImageView imgView;
	private Bitmap bitMap;
	//private Canvas canvas;
	private WordArray wordArray;
	private float[] ZOOM_SCALE;
	private int WORD_COUNT; //stores the number of words in wordArray
	private int COL_PER_BLOCK; //stores how many columns will be inside a block; in 9x9 this would be 3
	private int ROW_PER_BLOCK;
	private int VERTICAL_BLOCK; //stores how many (vertical) blocks are in a puzzle; in 9x9 this would be 3 blocks
	private int HORIZONTAL_BLOCK;
	private Context context; //context of GameActivity
	private String lastColour;
	private int[] undoBtnPressed;

	
	public drw( Context context2, RelativeLayout rectLayout2 )
	{
		//only declare at this point
		//no initialization because not enough parameters were calculated
		//but this class was needed to store View objects Bitmap, ImageView and Canvas
		context = context2;
		rectLayout = rectLayout2;
	}
	
	
	public void drwInitialize( Block[][] rectArr2, Paint paint2, //Canvas canvas2,
				RelativeLayout rectTextLayout2, TextMatrix textMatrix2, SudokuGenerator usrSudokuArr2,
				int[] zoomOn2, int[] drag2, int[] dX2, int[] dY2,
				int[] touchXZ2, int[] touchYZ2, int[] zoomButtonSafe2, int[] zoomClickSafe2,
				int[] zoomButtonDisableUpdate2, int bitmapSizeWidth2, int bitmapSizeHeight2, WordArray wordArray2, int[] btnClicked2,
				float[] ZOOM_SCALE2, int COL_PER_BLOCK2, int ROW_PER_BLOCK2, int VERTICAL_BLOCK2, int HORIZONTAL_BLOCK2,
				int WORD_COUNT2, int barH2, int[] undoBtnPressed2 )
	{
		paint = paint2;
		rectArr = rectArr2;
		//canvas = canvas2;
		
		rectTextLayout = rectTextLayout2;
		textMatrix = textMatrix2;
		usrSudokuArr = usrSudokuArr2;
		zoomOn = zoomOn2;
		drag = drag2;
		dX = dX2;
		dY = dY2;
		touchXZ = touchXZ2;
		touchYZ = touchYZ2;
		zoomButtonSafe = zoomButtonSafe2;
		zoomClickSafe = zoomClickSafe2;
		zoomButtonDisableUpdate = zoomButtonDisableUpdate2;
		bitmapSizeWidth = bitmapSizeWidth2;
		bitmapSizeHeight = bitmapSizeHeight2;
		wordArray = wordArray2;
		btnClicked = btnClicked2;
		ZOOM_SCALE = ZOOM_SCALE2;
		COL_PER_BLOCK = COL_PER_BLOCK2;
		ROW_PER_BLOCK = ROW_PER_BLOCK2;
		VERTICAL_BLOCK = VERTICAL_BLOCK2;
		HORIZONTAL_BLOCK = HORIZONTAL_BLOCK2;
		WORD_COUNT = WORD_COUNT2;
		barH = barH2;
		undoBtnPressed = undoBtnPressed2;
	}

	
	public void createBitmap( int bitmapSizeWidth, int bitmapSizeHeight, int barH )
	{
			// INITIALIZE VIEW
		bitMap = Bitmap.createBitmap( bitmapSizeWidth, bitmapSizeHeight-barH, Bitmap.Config.ARGB_8888 );
		
		imgView = new ImageView(context);
		canvas = new Canvas( bitMap );
		imgView.setImageBitmap( bitMap );
		rectLayout.addView( imgView );
		
	}
	
	
	public void setDrawParameters( int[] touchX, int[] touchY, Pair lastRectColoured,
						Pair currentRectColoured )
	{
		if( zoomOn[0] == 0 )
		{

				/** DRAW ZOOM OUT MODE **/

			newSqrTouched = false; //reset
			//canvas.drawColor( Color.parseColor( "#5cddb1" ) );


			// loop to find selected rect
			// note: this loop must be separate; cannot be combined with colour loop because running
			// 		 these simultaneously wont work in case when user has selected a later square
			//		 because the deselected square wont get updated in time to be decoloured

			if( zoomButtonSafe[0] == 0 && zoomButtonDisableUpdate[0] == 0 ) // do not update on button click (zoomSafe == 0 means only test after going out of "zoom" mode, that is, do not check the 'click coordinate' if it is in a square because those coordinates do not count as a click)
			{																// when zoomButtonDisableUpdate[0] == 0, do not update currectRectColoured (when switching "zoom" modes)
				// TEST IF USER-TOUCH INSIDE A SQUARE
				for( int i=0; i<WORD_COUNT; i++ )
				{
					for( int j=0; j<WORD_COUNT; j++ )
					{
						if( rectArr[i][j].getRect( ).contains( touchX[0], touchY[0] )  ) // find if sqr was clicked
						{
							selectDeselectRectArrAtIndex( lastRectColoured, currentRectColoured, i, j );
							
							newSqrTouched = true;
							Log.d("TAG", "--touched-1: [" + i + "] [" + j + "]");
						}
					}
				}

				if( newSqrTouched == false && zoomClickSafe[0] == 0 ) // if user touched but did not touch a square -deselect
				{
					if( currentRectColoured.getRow() != -1 ) {
						rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()].deselect();
					}
					Log.d( "TAG-2", " --outside click" );
					currentRectColoured.update(-1, -1);
				}
			}

			
			

			//rectLayout.invalidate( ); //required to update to print to screen
		}
		else if( zoomOn[0] == 1 )
		{

				/** DRAW ZOOM IN MODE **/

			newSqrTouched = false; //reset

			// save and reset canvas
			//canvas.save();
			//canvas.drawColor( Color.parseColor( "#5cddb1" ) );

			if( drag[0] == 0 )
			{
				//scale to find square where user clicked
				px = (int)( ( touchXZ[0] + touchX[0] ) / ZOOM_SCALE[0] ); //divide to scale
				py = (int)( ( touchYZ[0] + touchY[0] ) / ZOOM_SCALE[0] );
			}

			Log.d( "TAG-2", " touchXZ: " + touchXZ[0] + ", touchX: " + touchX[0] );
			

				/* LOOP TO FIND IF TOUCH IS INSIDE SQUARE */
			
			// note: this loop must be separate; cannot be combined with colour loop (see above for reason)

			if( drag[0] == 0 && zoomButtonDisableUpdate[0] == 0 ) // do not update on button click (zoomSafe == 0 means only test after going in "zoom" mode, that is, do not check the 'click coordinate' until user touches screen again because those coordinates do not count as a click)
			{
				for( int i=0; i<WORD_COUNT; i++ )
				{
					for( int j=0; j<WORD_COUNT; j++ )
					{
						if (rectArr[i][j].getRect().contains(px, py) && zoomClickSafe[0] == 0) // find if sqr was clicked
						{
							// if in "zoom", check to see if clicking outside the zoomed map
							// before when clicked in empty space on BOTTOM side, it highlighted square because click was valid within rectLayout
							if( touchX[0] > bitmapSizeWidth || touchY[0] > bitmapSizeHeight )
							{
								// NOTE: this works only for hardcoded bitmap size : should change this code when adapting bitmap to screen size
								break; // do not count click (outside bound)
							}
							
							selectDeselectRectArrAtIndex( lastRectColoured, currentRectColoured, i, j );

							newSqrTouched = true;
							Log.d("TAG", "--touched-2: [" + i + "] [" + j + "]");
						}
					}
				}

				if( newSqrTouched == false && zoomClickSafe[0] == 0 ) // if user touched but did not touch a square : deselect
				{													  // zoomClickSafe is supposed to block deselection after user switched mode AND clicked button right away
					if( currentRectColoured.getRow() != -1 ) {
						rectArr[currentRectColoured.getRow()][currentRectColoured.getColumn()].deselect( );
					}
					currentRectColoured.update(-1, -1);
				}
			}
			
		}
	}
	
	
	private void selectDeselectRectArrAtIndex( Pair lastRectColoured, Pair currentRectColoured, int i, int j )
	{
		lastRectColoured.update(currentRectColoured.getRow(), currentRectColoured.getColumn());
		if (lastRectColoured.getRow() != -1) //avoid indexing out of array since -1 means nothing was previously selected
		{
			// this "if statement" must occur after setting lastRect.update() to currentRect
			rectArr[lastRectColoured.getRow()][lastRectColoured.getColumn()].deselect(); //deselect prev selected
		}
		
		currentRectColoured.update(i, j); // update this (must occur before rectArr.select()
		if (currentRectColoured.getRow() != -1) //bound check
		{
			rectArr[i][j].select(); //update array as well
		}
	}
	
	
	public void reDraw( Pair currentRectColoured, int usrLangPref, int currentSelectedIsCorrect )
	{
		/*
		 * After all coordinates and parameters were set, draw the rectangles
		 * 		currentSelectedIsCorrect represents if sqr that is currently selected, is correct (==1) or incorrect (==2); ==0 is treated as outlier
		 */
		
		if( zoomOn[0] == 1 ){
			canvas.save();
			
			//translate the canvas when zoomed
			if (drag[0] == 1) { //translate as the user drags the canvas
				canvas.translate( -touchXZ[0] + dX[0], -touchYZ[0] + dY[0] );
			} else { //else user only clicked
				canvas.translate(-touchXZ[0], -touchYZ[0]);
			}
			
			canvas.scale( ZOOM_SCALE[0], ZOOM_SCALE[0] );
		}
		
		canvas.drawColor( Color.parseColor( "#F1F1F2" ) ); //reset all canvas to colour
		
		boolean highlight = true;
		String selectPredefionedWord = "#626262";
		String selectEmptySquare = "#689ec1";
		String selectCorrect = "#228b22";
		String selectIncorrect = "#C60000";
		String selectFixedSquareDark = "#a2a2a2";
		String selectFixedSquareLight = "#c2c2c2";
		
		
			/**REDRAW ALL SQUARES IN ZOOM WITH CORRESPONDING COLOUR **/
			
		for( int i=0; i<WORD_COUNT; i++ )
		{
			for( int j=0; j<WORD_COUNT; j++ )
			{
				//for each word, mark if there is conflict
				if( usrSudokuArr.getconflictArr()[i][j] == 1 )
				{ rectArr[i][j].setConflict( true ); }
				else{ rectArr[i][j].setConflict( false ); } //no conflict

				//set colours; must occur after figuring out is rect contained
				if( rectArr[i][j].isSelected( ) ) //if a selected rect
				{
					Log.d( "highlight", "selected" );
					if( usrSudokuArr.PuzzleOriginal[i][j] != 0 ) //if an element that cannot be modified
					{
						paint.setColor(Color.parseColor( selectPredefionedWord )); //colour if selected a predefined word
						canvas.drawRect(rectArr[i][j].getRect(), paint);
						highlight = false;
						continue;
					}
					else if( usrSudokuArr.Puzzle[i][j] == 0 ) //if square selected is empty (no input so far)
					{
						paint.setColor(Color.parseColor( selectEmptySquare )); //colour if selected a empty sqr
						canvas.drawRect(rectArr[i][j].getRect(), paint);
						highlight = false;
						continue;
					}
					
					if( currentSelectedIsCorrect == 1 ) //selected is correct
					{
						paint.setColor(Color.parseColor( selectCorrect ));
						rectArr[i][j].setConflict( false );
						usrSudokuArr.getconflictArr()[i][j] = 0; //mark as no conflict
						rectArr[i][j].setLastColour( selectCorrect );
					}
					else if( currentSelectedIsCorrect == 2 ) //selected is incorrect
					{
						paint.setColor(Color.parseColor( selectIncorrect ));
						rectArr[i][j].setConflict( true );
						usrSudokuArr.getconflictArr()[i][j] = 1; //mark conflict
						rectArr[i][j].setLastColour( selectIncorrect );
					}
					else
					{
						//paint.setColor(Color.parseColor("#000000"));
						//paint.setColor( Color.parseColor( rectArr[i][j].getLastColour( ) ) ); //on drag, use previous colour of selected rect


						//if no button clicked, then currentSelectedIsCorrect == 0, so draw colour that was so far
						if( rectArr[i][j].getConflict() == true ) //if a word has conflict, preserve incorrect colour
						{
							paint.setColor(Color.parseColor( selectIncorrect )); // set already filled with incorrect colour
						}
						else
						{
							paint.setColor(Color.parseColor( selectFixedSquareLight )); // set lighter colour for fixed numbers
						}
					}

				} else if( usrSudokuArr.PuzzleOriginal[i][j] != 0 ) // if a element that cannot be modified
				{
					paint.setColor(Color.parseColor( selectFixedSquareDark )); // set darker colour for fixed numbers
				} else {
					
					if( rectArr[i][j].getConflict() == true ) //if a word has conflict, preserve incorrect colour
					{
						paint.setColor(Color.parseColor( selectIncorrect )); // set already filled with incorrect colour
					}
					else
					{
						paint.setColor(Color.parseColor( selectFixedSquareLight )); // set lighter colour for fixed numbers
					}
				}
				
				canvas.drawRect(rectArr[i][j].getRect(), paint);
			}
		}
		
		
		String highlightPredefinedDarker = "#828282";
		String highlightConflictDarker = "#dd5d2e";
		String highlightRegularDarker = "#037fc1";
		
		
			/** HIGHLIGHT ROW AND COL **/
		
		// loop and highlight row and column of whatever sqr is selected
		if( currentRectColoured.getRow() != -1 )
		{
			int i;
			int j;
			int k;
			for( int n=0; n<2; n++ ) // loop twice to cover col and row
			{
				// get row or col index
				// 	n==0 means loop through col
				// 	n==1 means loop through row
				
				if( n == 0 ){
					i = currentRectColoured.getRow( ); //loop though col; get fixed row
					//j=0;
					k = currentRectColoured.getColumn( );
				} else {
					i = currentRectColoured.getColumn( ); //loop through row; get fixed col
					//i=0;
					k = currentRectColoured.getRow( );
				}
				
				for ( j = 0; j<WORD_COUNT; j++)
				{
					// n==0 means j is col (k == col)
					// n==1 means j is row (k == row)
					
					if( k == j ){ // omit colouring square the user has selected
						continue; //void - keep same colour so far
					}
					
					if( n == 0 ) {
						loopHighlight( i, j, highlightPredefinedDarker, highlightConflictDarker,
								highlightRegularDarker );
					} else if( n == 1 ){
						loopHighlight( j, i, highlightPredefinedDarker, highlightConflictDarker, //switch i,j
								highlightRegularDarker );
					}
					
				}
			}
			
		}
		
		// DRAW TEXT OVERLAY + RESTORE
		// update text of currently selected square on button click
		if( currentRectColoured.getRow() != -1 && btnClicked[0] == 1 )
		{
			//note that also having this execute only on "btnClicked[0] == 1" does not reset the sliding animation each time a square is highlighted
			//note: animation will reset each time a button is clicked because
			textMatrix.chooseLangAndDraw( currentRectColoured.getRow(), currentRectColoured.getColumn(),
					  					  wordArray, usrSudokuArr, usrLangPref );
			Log.d( "text-1", "re-drawing text in drw..." );
		}
		
		if( zoomOn[0] == 1 )
		{
			Log.d( "text-1", "re-drawing text in drw..." );
			textMatrix.reDrawTextZoom(touchXZ, touchYZ, dX, dY);
			canvas.restore( ); //return canvas back to original before zoom
		}
		
		rectLayout.invalidate( );
	}
	
	private void loopHighlight( int i, int j, String highlightPredefinedDarker,
								String highlightConflictDarker, String highlightRegularDarker )
	{
		/*
		 * This function is an extension to reDraw(), in the HIGHLIGHT section
		 * It was placed as a separate function so that col/row index can be switched, and not use two identical loops
		 * The variables here follow similar structure to HIGHLIGHT part, except that i,j may be switched
		 */
		
		if( usrSudokuArr.PuzzleOriginal[i][j] != 0 ) //if a predefined word
		{
			paint.setColor( Color.parseColor( highlightPredefinedDarker ) ); //colour predefined darker
			
		} else { //else a regular sqr
			
			if( rectArr[i][j].getConflict( )  == true ) // if sqr with conflict, set conflict colour to darker shade
			{
				paint.setColor (Color.parseColor( highlightConflictDarker ) ); //colour conflict darker
			}
			else //assume a regular sqr
			{
				paint.setColor (Color.parseColor( highlightRegularDarker ) ); //colour regular darker
			}
		}
		
		canvas.drawRect(rectArr[i][j].getRect(), paint);
	}

	public void resetRectArrConflict( )
	{
		for( int i=0; i<WORD_COUNT; i++ )
		{
			for( int j=0; j<WORD_COUNT; j++ )
			{
				rectArr[i][j].setConflict( false );
			}
		}
	}

	public Block[][] getRectArr( )
	{
		return rectArr;
	}

	public void setRectArrSelectedAtIndex( int i, int j )
	{
		//in rectArr, mark new rect as selected (usually called across rotation to preserve selected colouring)
		rectArr[i][j].select( );
	}

	/*
	public int[][] getConflictArr( ){ return  conflictArr; }

	public void setConflictArr( int[][] arr ){ conflictArr = arr; }

	public int setConflictAtIndex( int i, int j ){
		//returns 1 on incorrect index
		if( i < 0 || j < 0 || i >= WORD_COUNT || j >= WORD_COUNT ){ return 1; }
		conflictArr[i][j] = 1;
		return 0;
	}

	public int removeConflictAtIndex( int i, int j ){
		//returns 1 on incorrect index
		if( i < 0 || j < 0 || i >= WORD_COUNT || j >= WORD_COUNT ){ return 1; }
		conflictArr[i][j] = 0;
		return 0;
	}
	*/
}


































