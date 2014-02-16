package cat.montoya.gbd;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.color.Color;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.widget.Toast;
import cat.montoya.gbd.entity.Chip;
import cat.montoya.gbd.entity.Dice;
import cat.montoya.gbd.entity.Game;
import cat.montoya.gbd.game.elements.ChipTiledSprite;
import cat.montoya.gbd.game.elements.DiceAnimatedSprite;
import cat.montoya.gbd.game.elements.GameBoardSprite;
import cat.montoya.gbd.utils.FileUtils;

public class GameActivity extends SimpleBaseGameActivity implements IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static int CAMERA_WIDTH = 800;
	private static int CAMERA_HEIGHT = 600;

	private static final int LAYER_COUNT = 3;

	private static final int LAYER_BACKGROUND = 0;
	private static final int LAYER_CHIPS = LAYER_BACKGROUND + 1;
	private static final int LAYER_DICE = LAYER_CHIPS + 1;

	// ===========================================================
	// Fields
	// ===========================================================

	private ZoomCamera mZoomCamera;
	private Scene mScene;
	private SurfaceScrollDetector mScrollDetector;
	private PinchZoomDetector mPinchZoomDetector;
	private float mPinchZoomStartedCameraZoomFactor;
	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;
	private ITiledTextureRegion mTiledTextureRegion;

	// Game elements
	private Game game;
	private GameBoardSprite gameBoardSprite;
	private List<DiceAnimatedSprite> dices = new ArrayList<DiceAnimatedSprite>();
	private List<ChipTiledSprite> chips = new ArrayList<ChipTiledSprite>();
	private List<Shape> old_chips = new ArrayList<Shape>();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	

	@Override
	public EngineOptions onCreateEngineOptions() {
		game = (Game) getIntent().getSerializableExtra("game");
		if (game  == null){
			Toast.makeText(this, "ERROR LOADING GAME", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(this, GameGridViewActivity.class);
			startActivity(i);
		}
			
		
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		GameActivity.CAMERA_WIDTH = size.x;
		GameActivity.CAMERA_HEIGHT = size.y;

		this.mZoomCamera = new ZoomCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
//		this.mZoomCamera.setZoomFactor(0.2f);
//		this.mZoomCamera.setCenter(0, 0);

		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.mZoomCamera);

		if (MultiTouch.isSupported(this)) {
			if (MultiTouch.isSupportedDistinct(this)) {
//				Toast.makeText(this, "MultiTouch detected --> Both controls will work properly!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(
						this,
						"MultiTouch detected, but your device has problems distinguishing between fingers.\n\nControls are placed at different vertical locations.",
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(this,
					"Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch.)\n\nControls are placed at different vertical locations.",
					Toast.LENGTH_LONG).show();
		}

		return engineOptions;
	}
	

	@Override
	public void onCreateResources() {
		AssetManager am = this.getAssets();
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.NEAREST);
		mTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAssetDirectory(this.mBitmapTextureAtlas, this.getAssets(), "animatedsprites");
		
		DiceAnimatedSprite.resetTextureRegion();
		ChipTiledSprite.resetTextureRegion();
		this.createBoard();
		this.createAllDices();
		this.createAllChips();
		
		try {
			mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 0));
			mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Log.d("", e.getMessage());
		}
		
	}

	private void createBoard() {
		String sFolder = FileUtils.getRootFolderPath(this) + "/";
		this.gameBoardSprite = GameBoardSprite.getInstance(this, sFolder + game.getBoardURL(), 1866, 1860);
	}
	
	private void createAllDices() {
		if (this.game.getDices() == null ||this.game.getDices().isEmpty()){
			//TODO eliminar aixo, de moment posem daus dummy
			final float xTaulell = this.gameBoardSprite.getWidth();
			this.dices.add(DiceAnimatedSprite.getInstance(this, this.mTiledTextureRegion, xTaulell));
			this.dices.add(DiceAnimatedSprite.getInstance(this, this.mTiledTextureRegion, xTaulell));
		} else {
			for (Dice dice : this.game.getDices()) {
				this.dices.add(DiceAnimatedSprite.getInstance(this, this.mTiledTextureRegion, this.gameBoardSprite.getWidth()));
			}
		}
	}
	
	private void createAllChips() {
		
		
		
		if (this.game.getChips() == null || this.game.getChips().isEmpty()){

		for (int i = 0; i < 4; i++) {
			this.chips.add(ChipTiledSprite.getInstance(this, this.mTiledTextureRegion, 3));
			this.chips.add(ChipTiledSprite.getInstance(this, this.mTiledTextureRegion, 4));
			this.chips.add(ChipTiledSprite.getInstance(this, this.mTiledTextureRegion, 5));
//			final Shape rect1 = this.makeColoredRectangle(100, 200, 90, 90, 1);
//			this.old_chips.add(rect1);
//			final Rectangle rect2 = this.makeColoredRectangle(100, 200, 90, 90, 2);
//			this.old_chips.add(rect2);
//			final Rectangle rect3 = this.makeColoredRectangle(100, 200, 90, 90, 3);
//			this.old_chips.add(rect3);
//			final Rectangle rect4 = this.makeColoredRectangle(100, 200, 90, 90, 4);
//			this.old_chips.add(rect4);
		}
			
		} else {
			for (Chip chip : this.game.getChips()) {
				switch (chip.getType()) {
				case Chip.RECTANGLE:
					final Shape rect = this.makeColoredRectangle(1500, 200, chip.getSize(), chip.getSize(), chip.getColor());
					this.old_chips.add(rect);
					break;
				case Chip.CIRCLE:
					
					break;

				default:
					break;
				}
				
			}
			
		}
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.mScene = new Scene();
		this.mScene.setOnAreaTouchTraversalFrontToBack();

		this.mScene.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
		this.mScene.setBackgroundEnabled(false);
		this.mScene.attachChild(this.gameBoardSprite);
		centerAndZoomGameBoard();
		
		this.mScrollDetector = new SurfaceScrollDetector(this);
		this.mPinchZoomDetector = new PinchZoomDetector(this);

		this.mScene.setOnSceneTouchListener(this);
		this.mScene.setTouchAreaBindingOnActionDownEnabled(true);

		
		
		for (ChipTiledSprite chip : this.chips){
			this.mScene.attachChild(chip);
			this.mScene.registerTouchArea(chip);
		}

		for (DiceAnimatedSprite dice : this.dices) {
			this.mScene.attachChild(dice);
			this.mScene.registerTouchArea(dice);
		}
		return this.mScene;
	}

	/**
	 * Centrar el taulell en la pantalla i fer zoom per veure'l complert
	 */
	private void centerAndZoomGameBoard() {
		float zoomFactor = CAMERA_HEIGHT/this.gameBoardSprite.getHeight();
		this.mZoomCamera.setZoomFactor(zoomFactor);		
		final float centerX = this.gameBoardSprite.getWidth() / 2;
		final float centerY = this.gameBoardSprite.getHeight() / 2;
		this.mZoomCamera.setCenter(centerX,centerY);
	}

	private Rectangle makeColoredRectangle(final float pX, final float pY, float pWidth, float pHeight, int pColor) {
		final Rectangle coloredRect = new Rectangle(pX, pY, pWidth, pHeight, this.getVertexBufferObjectManager()) {
			boolean mGrabbed = false;

			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				switch (pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					this.setScale(1.25f);
					this.mGrabbed = true;
					break;
				case TouchEvent.ACTION_MOVE:
					if (this.mGrabbed) {
						this.setPosition(pSceneTouchEvent.getX() - 45, pSceneTouchEvent.getY() - 45);
					}
					break;
				case TouchEvent.ACTION_UP:
					if (this.mGrabbed) {
						this.mGrabbed = false;
						this.setScale(1.0f);
					}
					break;
				}
				return true;
			}
		};
		
		coloredRect.setColor(getColorFromInt(pColor));
		return coloredRect;
	}

	private Color getColorFromInt(int pColor) {
		Color ret = null;
		switch (pColor) {
		case 1:
			ret = Color.RED;
			break;
		case 2:
			ret = Color.BLUE;
			break;
		case 3:
			ret = Color.YELLOW;
			break;
		case 4:
			ret = Color.GREEN;
			break;
		case 5:
			ret = Color.PINK;
			break;

		default:
			ret = Color.RED;
			break;
		}
		return ret;
	}

	@Override
	public void onScrollStarted(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mZoomCamera.getZoomFactor();
		this.mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
	}

	@Override
	public void onScroll(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mZoomCamera.getZoomFactor();
		this.mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
	}

	@Override
	public void onScrollFinished(final ScrollDetector pScollDetector, final int pPointerID, final float pDistanceX, final float pDistanceY) {
		final float zoomFactor = this.mZoomCamera.getZoomFactor();
		this.mZoomCamera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
	}

	@Override
	public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent) {
		this.mPinchZoomStartedCameraZoomFactor = this.mZoomCamera.getZoomFactor();
	}

	@Override
	public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		this.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
	}

	@Override
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector, final TouchEvent pTouchEvent, final float pZoomFactor) {
		this.mZoomCamera.setZoomFactor(this.mPinchZoomStartedCameraZoomFactor * pZoomFactor);
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
		this.mPinchZoomDetector.onTouchEvent(pSceneTouchEvent);

		if (this.mPinchZoomDetector.isZooming()) {
			this.mScrollDetector.setEnabled(false);
		} else {
			if (pSceneTouchEvent.isActionDown()) {
				this.mScrollDetector.setEnabled(true);
			}
			this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
		}

		return true;
	}

}

