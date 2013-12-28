package cat.montoya.gbd.game.elements;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

public class DiceAnimatedSprite extends AnimatedSprite {

	private static final int ROWS = 1;// Rows in the image
	private static final int COLUMNS = 6;// Columns in the image
	private static final int WIDTH = 1024;// Width for all image
	private static final int HEIGHT = 256; // Height for all image
	private static final String DICE1_JPG = "dice1.jpg";// Resource name

	private static TiledTextureRegion mDiceTextureRegion;

	/**
	 * Per fer drag and drop
	 */
	boolean mGrabbed = false;
	float scale;

	private DiceAnimatedSprite(BaseGameActivity bga, float xTaulell) {
		super(0, 0, DiceAnimatedSprite.mDiceTextureRegion, bga.getVertexBufferObjectManager());
		//El tamany dels daus el fem dependre de la mida del taulell
//		this.scale = xTaulell/20/this.getWidth();
//		this.setWidth(this.getWidth()*this.scale);
//		this.setHeight(this.getHeight()*this.scale);
		//O DIRECTAMENT li apliquem una scala
//		this.setScale(scale);
	}

	public static DiceAnimatedSprite getInstance(BaseGameActivity bga, float xTaulell) {

		if (DiceAnimatedSprite.mDiceTextureRegion == null) {
			BuildableBitmapTextureAtlas mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(bga.getTextureManager(), WIDTH, HEIGHT, TextureOptions.NEAREST);
			DiceAnimatedSprite.mDiceTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, bga, DICE1_JPG, COLUMNS,
					ROWS);
			try {
				mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
				mBitmapTextureAtlas.load();
			} catch (TextureAtlasBuilderException e) {
				Debug.e(e);
			}
		}

		return new DiceAnimatedSprite(bga, xTaulell);
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
		

		switch (pSceneTouchEvent.getAction()) {
		case TouchEvent.ACTION_DOWN:
			this.setScale(2.0f);
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
				this.setScale(1.0f);
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

}
