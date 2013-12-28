package cat.montoya.gbd.game.elements;

import java.io.File;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.FileBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

public class GameBoardSprite extends Sprite {

	private GameBoardSprite(final float pX, final float pY, final ITextureRegion pTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pTextureRegion.getWidth(), pTextureRegion.getHeight(), pTextureRegion, pVertexBufferObjectManager, DrawType.STATIC);
	}

	public static GameBoardSprite getInstance(BaseGameActivity bga, String resource, int pWidth, int pHeigth) {

		BitmapTextureAtlas mBackgroundTexture = new BitmapTextureAtlas(bga.getTextureManager(), pWidth, pHeigth);
//		ITextureRegion mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBackgroundTexture, bga, resource, 0, 0);
		ITextureRegion mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(mBackgroundTexture, FileBitmapTextureAtlasSource.create(new File(resource)), 0, 0);
		mBackgroundTexture.load();
		return new GameBoardSprite(0, 0, mBackgroundTextureRegion, bga.getVertexBufferObjectManager());
	}
	
	
	@Override
	protected void applyRotation(final GLState pGLState) {
		final float rotation = this.mRotation;

		if(rotation != 0) {
			final float rotationCenterX = this.mRotationCenterX;
			final float rotationCenterY = this.mRotationCenterY;

			pGLState.translateModelViewGLMatrixf(rotationCenterX, rotationCenterY, 0);
			/* Note we are applying rotation around the y-axis and not the z-axis anymore! */
			pGLState.rotateModelViewGLMatrixf(rotation, 1, 0, 0);
			pGLState.translateModelViewGLMatrixf(-rotationCenterX, -rotationCenterY, 0);
		}
	}

}
