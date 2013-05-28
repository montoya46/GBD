package cat.montoya.gbd;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.camera.ZoomCamera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.input.touch.detector.PinchZoomDetector;
import org.andengine.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.graphics.Point;
import android.view.Display;
import android.widget.Toast;
import cat.montoya.gbd.game.elements.DiceAnimatedSprite;
import cat.montoya.gbd.game.elements.GameBoardSprite;

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

	// Game elements
	private GameBoardSprite gameBoardSprite;
	private List<DiceAnimatedSprite> dices = new ArrayList<DiceAnimatedSprite>();

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================
	
	private boolean rotated = false;

	@Override
	public EngineOptions onCreateEngineOptions() {

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
				Toast.makeText(this, "MultiTouch detected --> Both controls will work properly!", Toast.LENGTH_SHORT).show();
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

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.gameBoardSprite = GameBoardSprite.getInstance(this, "parchis.jpg", 1866, 1860);
		this.dices.add(DiceAnimatedSprite.getInstance(this));
		this.dices.add(DiceAnimatedSprite.getInstance(this));
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

		for (int i = 0; i < 4; i++) {
			final Rectangle rect1 = this.makeColoredRectangle(1500, 200, 1, 0, 0);
			final Rectangle rect2 = this.makeColoredRectangle(100, 200, 0, 1, 0);
			final Rectangle rect3 = this.makeColoredRectangle(1500, 1500, 0, 0, 1);
			final Rectangle rect4 = this.makeColoredRectangle(100, 1500, 1, 1, 0);
		}
		
		
		final Rectangle rect = new Rectangle(-90, 0, 90, 90, this.getVertexBufferObjectManager()){
			
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				rotate();
				return true;
			}

		};
		
		rect.setColor(1,1,1);

		this.mScene.attachChild(rect);
		this.mScene.registerTouchArea(rect);

		for (DiceAnimatedSprite dice : dices) {
			this.mScene.attachChild(dice);
			this.mScene.registerTouchArea(dice);
		}
		

		return this.mScene;
	}
	
	private void rotate() {
		if (!this.rotated){
			this.rotated=true;
//			this.gameBoardSprite.registerEntityModifier(new LoopEntityModifier(new RotationModifier(1, 1, 45)));
		}
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

	private Rectangle makeColoredRectangle(final float pX, final float pY, final float pRed, final float pGreen, final float pBlue) {
		final Rectangle coloredRect = new Rectangle(pX, pY, 90, 90, this.getVertexBufferObjectManager()) {
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
		coloredRect.setColor(pRed, pGreen, pBlue);

		this.mScene.attachChild(coloredRect);
		this.mScene.registerTouchArea(coloredRect);

		return coloredRect;
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

