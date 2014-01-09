package cat.montoya.gbd.game.elements;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class ChipTiledSprite extends TiledSprite {
	
	private static final int ROWS = 2;// Rows in the image
	private static final int COLUMNS = 3;// Columns in the image
	private static final int WIDTH = 96;// Width for all image
	private static final int HEIGHT = 64; // Height for all image
	private static final int CHIP1_INDEX = 1;// Resource name

	private static ITiledTextureRegion mChipTextureRegion;
	
	boolean mGrabbed = false;
	float scale;
		
	
	private ChipTiledSprite(BaseGameActivity bga, int tile) {
		super(0, 0, ChipTiledSprite.mChipTextureRegion, bga.getVertexBufferObjectManager());
		this.setScale(3.0f);
		this.setCurrentTileIndex(tile);
	}


	public static ChipTiledSprite getInstance(BaseGameActivity bga, ITiledTextureRegion mTiledTextureRegion, int tile) {
		if (ChipTiledSprite.mChipTextureRegion == null){
			ITexture tr = mTiledTextureRegion.getTexture();//Region(CHIP1_INDEX).getTexture();
			ChipTiledSprite.mChipTextureRegion = TiledTextureRegion.create(tr, 0, 50, WIDTH, HEIGHT, COLUMNS, ROWS);
		}
		return new ChipTiledSprite(bga, tile);
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
			break;
		}

		return true;
	}
	
	public  static void resetTextureRegion(){
		ChipTiledSprite.mChipTextureRegion = null;
	}

}
