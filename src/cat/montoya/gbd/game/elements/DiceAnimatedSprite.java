package cat.montoya.gbd.game.elements;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class DiceAnimatedSprite extends AnimatedSprite {

	private static final int ROWS = 1;// Rows in the image
	private static final int COLUMNS = 6;// Columns in the image
	private static final int WIDTH = 300;// Width for all image
	private static final int HEIGHT = 50; // Height for all image
	private static final int DICE1_INDEX= 0;// Resource name

	private static ITiledTextureRegion mDiceTextureRegion;

	/**
	 * Per fer drag and drop
	 */
	boolean mGrabbed = false;
	float scale;

	private DiceAnimatedSprite(BaseGameActivity bga, float xTaulell) {
		super(0, 0, DiceAnimatedSprite.mDiceTextureRegion, bga.getVertexBufferObjectManager());
		this.setScale(2.0f);
		//El tamany dels daus el fem dependre de la mida del taulell
//		this.scale = xTaulell/20/this.getWidth();
//		this.setWidth(this.getWidth()*this.scale);
//		this.setHeight(this.getHeight()*this.scale);
		//O DIRECTAMENT li apliquem una scala
//		this.setScale(scale);
	}

	public static DiceAnimatedSprite getInstance(BaseGameActivity bga, ITiledTextureRegion mTiledTextureRegion, float xTaulell) {
		if (DiceAnimatedSprite.mDiceTextureRegion == null){
			ITexture tr = mTiledTextureRegion.getTextureRegion(DICE1_INDEX).getTexture();
			DiceAnimatedSprite.mDiceTextureRegion = TiledTextureRegion.create(tr, 0, 0, WIDTH, HEIGHT, COLUMNS, ROWS);	
		}
		return new DiceAnimatedSprite(bga, xTaulell);
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		

		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
//			this.setScale(2.0f);
			this.mGrabbed = true;
			break;
		case TouchEvent.ACTION_MOVE:
			if (this.mGrabbed) {
				this.setPosition(pSceneTouchEvent.getX() - this.getWidth()/2, pSceneTouchEvent.getY() -this.getHeight()/2);
			}
			break;
		case TouchEvent.ACTION_UP:
			if (this.mGrabbed) {
				this.mGrabbed = false;
//				this.setScale(1.0f);
			}
			throwDice();
			break;
		}

		return true;
	}

	public void throwDice() {
		// TODO implementar un llançament real de un dau
		if (this.isAnimationRunning()) {
			int stop = (int) (Math.random() * 6);
			stop--;
			if (stop <= 5 && stop >= 0) {
				this.stopAnimation(stop);
			} else {
				this.stopAnimation();
			}
		} else {
			this.animate(250);
		}

	}
	
	public static void resetTextureRegion(){
		DiceAnimatedSprite.mDiceTextureRegion = null;
	}

}
