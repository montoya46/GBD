package cat.montoya.gbd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cat.montoya.gbd.DialogColorPicker.OnColorSelectedListener;
import cat.montoya.gbd.DialogDeleteListDices.OnDeleteDiceSelectedListener;
import cat.montoya.gbd.DialogDicePicker.OnDiceSelectedListener;
import cat.montoya.gbd.DialogShapePicker.OnShapeSelectedListener;
import cat.montoya.gbd.DialogDeleteListChips.OnChipSelectedListener;
import cat.montoya.gbd.dao.GameDAO;
import cat.montoya.gbd.dao.IGameDAO;
import cat.montoya.gbd.entity.Chip;
import cat.montoya.gbd.entity.Dice;
import cat.montoya.gbd.entity.Game;
import cat.montoya.gbd.entity.Dice.DiceType;
import cat.montoya.gbd.utils.FileUtils;
import cat.montoya.gbd.utils.ImageSelectorUtils;
import cat.montoya.gbd.utils.MD5Utils;

public class GameDetail extends Activity {
	private IGameDAO gameDAO;
	private int _currentShape;
	private int _currentColor;
	private Dice.DiceType _currentDice;
	private DialogShapePicker _dialogShape;
	private DialogDicePicker _dialogDicePicker;
	private DialogColorPicker _dialogColor;
	private DialogDeleteListChips _dialogDeleteChips;
	private DialogDeleteListDices _dialogDeleteDices;
	private Game _game = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_game_detail);
		
		_dialogShape = new DialogShapePicker();
		_dialogColor = new DialogColorPicker();
		_dialogDeleteChips = new DialogDeleteListChips();
		_dialogDicePicker = new DialogDicePicker();
		_dialogDeleteDices = new DialogDeleteListDices();
		_currentShape = _dialogShape.GetDefaultShape();
		_currentColor = _dialogColor.GetDefaultColor();
		_currentDice = _dialogDicePicker.GetDefaultDice();
		
		gameDAO = new GameDAO(this);

		Long id = getIntent().getLongExtra("id", -1);

		if (id != -1) {// Carregar Joc de la base de dades i emplenar fitxa
			Toast.makeText(this, "Carregar joc amb id: " + id, Toast.LENGTH_LONG).show();
			_game = gameDAO.getGame(id);
			loadGame();
		} else { 
			_game = new Game();
		}

		Spinner spinner = (Spinner) findViewById(R.id.numer_dices);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.number_dices_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		Spinner spinnerChips = (Spinner) findViewById(R.id.numer_chips);
		spinnerChips.setAdapter(adapter);
		Spinner spinnerSize = (Spinner) findViewById(R.id.number_size);
		spinnerSize.setAdapter(adapter);
		
		RelativeLayout rlColor = (RelativeLayout)findViewById(R.id.rlColor);
		ImageView ivShapes = (ImageView)findViewById(R.id.ibNewChip);
		ImageView ivSelectDice = (ImageView)findViewById(R.id.ibNewDice);

		ivSelectDice.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SelectDiceType();
			}
		});

		ivShapes.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SelectShape();
			}
		});

		rlColor.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
            	SelectColor();
            }
        });	
	}
	
	public void AddDice(View v){
		Spinner spNumeroDices = (Spinner)findViewById(R.id.numer_dices);
		int numeroFichas = Integer.parseInt(spNumeroDices.getSelectedItem().toString());
		
		for (int i = 0; i < numeroFichas; i++) {
			Dice d = new Dice();
			d.setId(UUID.randomUUID().getMostSignificantBits());
			d.setType(_currentDice);
			_game.getDices().add(d);		
		}
	}
	
	public void SelectDiceType(){
		_dialogDicePicker.show(getFragmentManager(), "fragment_dialog_dice_picker");
		_dialogDicePicker.setOnShapeSelectedListener(new OnDiceSelectedListener() {
			public void onDiceSelectedOccurred(View v, DiceType dice) {
				ImageView ivDices = (ImageView)findViewById(R.id.ibNewDice);
				ivDices.setImageResource(ImageSelectorUtils.SelectImg(dice));
				_currentDice = dice;
			}
		});
	}
	
	public void ModifyDices(View v){
		_dialogDeleteDices.SetDiceList(_game.getDices());
		_dialogDeleteDices.show(getFragmentManager(), "fragment_dialog_delete_dices");
		_dialogDeleteDices.setOnDeleteDiceSelectedListener(new OnDeleteDiceSelectedListener() {
			public void onDiceSelectedOccurred(View v, List<Dice> dicesToDelete) {
				for (Dice dice : dicesToDelete) {
					_game.getDices().remove(dice);
				}
			}
		});
	}
	
	public void AddChip(View v){
		Spinner spNumeroFichas = (Spinner)findViewById(R.id.numer_chips);
		int numeroFichas = Integer.parseInt(spNumeroFichas.getSelectedItem().toString());
		Spinner spSize = (Spinner) findViewById(R.id.number_size);
		int numeroSize = Integer.parseInt(spSize.getSelectedItem().toString());
		
		for (int i = 0; i < numeroFichas; i++) {
			Chip c = new Chip();
			c.setId(UUID.randomUUID().getMostSignificantBits());
			c.setColor(_currentColor);
			c.setType(_currentShape);
			c.setSize(numeroSize);
			_game.getChips().add(c);
		}
	}
	
	public void SelectShape(){
		_dialogShape.show(getFragmentManager(), "fragment_dialog_shape_picker");
		_dialogShape.setOnShapeSelectedListener(new OnShapeSelectedListener() {
			public void onShapeSelectedOccurred(View v, int shape) {
				ImageView ivShapes = (ImageView)findViewById(R.id.ibNewChip);
				ivShapes.setImageResource(shape);
				_currentShape = shape;
			}
		});
	}
	
	public void SelectColor() {
		_dialogColor.show(getFragmentManager(), "fragment_dialog_color_picker");
		_dialogColor.setOnColorSelectedListener(new OnColorSelectedListener() {
			@Override
			public void onColorSelectedOccurred(View v, int color) {
				RelativeLayout rlColor = (RelativeLayout)findViewById(R.id.rlColor);
				rlColor.setBackgroundColor(color);
				_currentColor = color;
			}
		});
	}
	
	public void SelectChip(View v){
		_dialogDeleteChips.SetChipList(_game.getChips());
		_dialogDeleteChips.show(getFragmentManager(), "fragment_dialog_delete_chips");
		_dialogDeleteChips.setOnChipSelectedListener(new OnChipSelectedListener() {
			@Override
			public void onChipSelectedOccurred(View v, List<Chip> chipsToDelete) {
				for (Chip chip : chipsToDelete) {
					_game.getChips().remove(chip);
				}
			}
		});
	}

	private void loadGame() {
		// ID (no cal fer res amb aixo?)
//		_game.getId();
		// NAME
		TextView name = (TextView) findViewById(R.id.edTitulo);
		name.setText(_game.getName());
		// HELP
		TextView help = (TextView) findViewById(R.id.edDescripcion);
		help.setText(_game.getHelp());
		
		// BOARD && BOARDTHUMBNAIL
		File imgFile = new  File(FileUtils.getRootFolder(this),_game.getBoardURL());
		if(imgFile.exists()){

		    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

		    ImageView myImage = (ImageView) findViewById(R.id.ibPreview);
		    myImage.setImageBitmap(myBitmap);

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_detail, menu);
		return true;
	}

	/**
	 * Called onClick save button
	 * 
	 * @param v
	 */
	public void saveGame(View v) {
		// NAME
		_game.setName(getStringFromTextView(R.id.edTitulo));
		// HELP
		_game.setHelp(getStringFromTextView(R.id.edDescripcion));
		// BOARD && BOARDTHUMBNAIL
		String file = writeBoardToFile();
		if (file != null){
			_game.setBoardURL(file);
			_game.setBoardThumbnailURL("tmb_"+file);
		}
		// Save the game
		_game = gameDAO.setGame(_game);

	}

	private List<Chip> viewToEntityChips() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Dice> viewToEntityDices() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method writes the image to aplication folder 
	 * 
	 * @return string array with the name of the board
	 */
	private String writeBoardToFile() {
		// TODO esta posat a saco
		ImageView preview = (ImageView) findViewById(R.id.ibPreview);
		if (preview != null) {
			if (preview.getDrawable() != null && preview.getDrawable() instanceof BitmapDrawable) {
				BitmapDrawable drawable = (BitmapDrawable) preview.getDrawable();
				Bitmap bitmap = drawable.getBitmap();

				if (bitmap != null) {
					// String extStorageDirectory =
					// Environment.getExternalStorageDirectory().toString();
					File rootFolder = FileUtils.getRootFolder(this);
					OutputStream outStream = null;
					
					File fBoard = new File(rootFolder, "board.PNG");
					try {
						outStream = new FileOutputStream(fBoard);
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
						outStream.flush();
					} catch (Exception e) {
						Log.e("Error", "Error writing images to disc", e);
					} finally {
						try {
							outStream.close();
						} catch (IOException e) {
							Log.w("Warn", "Ha petat algo guardant les imatgess");
						}
					}
					
					OutputStream outStreamThm = null;
					File fBoardThumbnail = new File(rootFolder, "tmb_board.PNG");
					Bitmap bitmaptmb=null;
					try {
						outStreamThm = new FileOutputStream(fBoardThumbnail);
						bitmaptmb = ThumbnailUtils.extractThumbnail(bitmap, 100, 100);
						bitmaptmb.compress(Bitmap.CompressFormat.PNG, 25, outStreamThm);
						outStreamThm.flush();
					} catch (Exception e) {
						Log.e("Error", "Error writing images to disc", e);
					} finally {
						try {
							outStreamThm.close();
						} catch (IOException e) {
							Log.w("Warn", "Ha petat algo guardant les imatgess");
						}
						bitmap.recycle();
						bitmaptmb.recycle();
					}
					
					String fBoardName = MD5Utils.md5String(fBoard);
					fBoard.renameTo(new File(rootFolder,fBoardName));
					fBoardThumbnail.renameTo(new File(rootFolder, "tmb_"+fBoardName));
					
					return fBoardName;
				}
			}
		}
		return null;
	}

	private String getStringFromTextView(int res) {
		String ret = null;
		TextView tvName = (TextView) findViewById(res);
		if (tvName.getText() != null) {
			ret = tvName.getText().toString();
		}
		return ret;
	}
}
