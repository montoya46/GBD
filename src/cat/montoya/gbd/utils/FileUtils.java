package cat.montoya.gbd.utils;

import java.io.File;

import android.app.Activity;

public class FileUtils {
	
	public static String getRootFolderPath(Activity activity) {
		return FileUtils.getRootFolder(activity).getAbsolutePath();
	}
	
	public static File getRootFolder(Activity activity) {
		File folder = activity.getExternalFilesDir(null);
		if (folder == null)
			folder = activity.getFilesDir();
		return folder;
	}

}
