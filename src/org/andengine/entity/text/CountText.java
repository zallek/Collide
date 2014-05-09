package org.andengine.entity.text;

import org.andengine.opengl.font.IFont;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

/**
 * @author Nicolas Fortin
 * @since 09.17.2013
 */
public abstract class CountText extends Text {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	private final CountTextOptions mCountTextOptions;

	private float mSecondsElapsed;

	final private int intFrom;
	final private int intTo;
	final private boolean reverse;
	
	private Integer intCurrent = null;
	private boolean stop = true;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CountText(final float pX, final float pY, final IFont pFont, final int intFrom, final int intTo, final CountTextOptions pCountTextOptions, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pFont, "0123456789", pCountTextOptions, pVertexBufferObjectManager);

		this.mCountTextOptions = pCountTextOptions;
		
		this.intFrom = intFrom;
		this.intTo = intTo;
		this.reverse = intFrom > intTo ? true : false;
		
		init();
		
		//TODO
		//this.mDuration = this.mCharactersToDraw * this.mTickerTextOptions.mCharactersPerSecond;
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	@Override
	public CountTextOptions getTextOptions() {
		return (CountTextOptions) super.getTextOptions();
	}

	public float getCountPerSecond() {
		return this.mCountTextOptions.mCountPerSecond;
	}

	public void setCountPerSecond(final float pCountPerSecond) {
		this.mCountTextOptions.mCountPerSecond = pCountPerSecond;

		//this.mDuration = this.mCharactersToDraw * pCharactersPerSecond;
	}

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	protected void onManagedUpdate(final float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
		final Integer intPrevious = this.intCurrent;
		
		if(!this.stop){
			this.mSecondsElapsed = this.mSecondsElapsed + pSecondsElapsed;
					
			if(this.reverse){
				this.intCurrent = Math.round(intFrom - mSecondsElapsed * this.mCountTextOptions.mCountPerSecond);
				if(intPrevious != intCurrent) {
					setText(String.valueOf(intCurrent));
				}
				if(this.intCurrent <= intTo){
					this.stop = true;
					onFinished();
				}
			}
			else {
				this.intCurrent = Math.round(intFrom + mSecondsElapsed * this.mCountTextOptions.mCountPerSecond);
				if(intPrevious != intCurrent) {
					setText(String.valueOf(intCurrent));
				}
				if(this.intCurrent >= intTo){
					this.stop = true;
					onFinished();
				}
			}
		}
	}
	
	public abstract void onFinished();

	private void init() {
		this.intCurrent = this.intFrom;
		setText(String.valueOf(intCurrent));
	}
	
	public void pause() {
		this.stop = true;
	}
	
	public void resume() {
		this.stop = false;
	}
	
	public void start() {
		resume();
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	public static class CountTextOptions extends TextOptions {
		// ===========================================================
		// Constants
		// ===========================================================

		// ===========================================================
		// Fields
		// ===========================================================

		float mCountPerSecond;

		// ===========================================================
		// Constructors
		// ===========================================================

		public CountTextOptions() {
			this(1);
		}

		public CountTextOptions(final float pCountPerSound) {
			this(HorizontalAlign.CENTER, pCountPerSound);
		}

		public CountTextOptions(final HorizontalAlign pHorizontalAlign, final float pCountPerSound) {
			this(AutoWrap.NONE, 0, pHorizontalAlign, Text.LEADING_DEFAULT, pCountPerSound);
		}

		public CountTextOptions(final AutoWrap pAutoWrap, final float pAutoWrapWidth, final HorizontalAlign pHorizontalAlign, final float pCountPerSound) {
			this(pAutoWrap, pAutoWrapWidth, pHorizontalAlign, Text.LEADING_DEFAULT, pCountPerSound);
		}

		public CountTextOptions(final AutoWrap pAutoWrap, final float pAutoWrapWidth, final HorizontalAlign pHorizontalAlign, final float pLeading, final float pCountPerSound) {
			super(pAutoWrap, pAutoWrapWidth, pHorizontalAlign, pLeading);

			this.mCountPerSecond = pCountPerSound;
		}

		// ===========================================================
		// Getter & Setter
		// ===========================================================

		public float getCountPerSecond() {
			return this.mCountPerSecond;
		}

		public void setCharactersPerSecond(final float pCountPerSound) {
			this.mCountPerSecond = pCountPerSound;
		}

		// ===========================================================
		// Methods for/from SuperClass/Interfaces
		// ===========================================================

		// ===========================================================
		// Methods
		// ===========================================================

		// ===========================================================
		// Inner and Anonymous Classes
		// ===========================================================
	}
}
