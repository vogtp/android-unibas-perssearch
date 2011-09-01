package ch.unibas.urz.android.perssearch.contacts;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;
import ch.unibas.urz.android.perssearch.helper.Logger;
import ch.unibas.urz.android.perssearch.model.Person;

public class ContactAdderVcf {

	private static final int HOUR_IN_MILLIES = 1000 * 60 * 60;
	private static final String FILENAME_PREFIX = "perssearch";
	private static final String FILENAME_EXTENTION = ".vcf";

	public static void addContact(Context ctx, Person person) {
		try {
			File file = getVcfFile(ctx, person);
			Intent i = new Intent(Intent.ACTION_DEFAULT);
			i.setDataAndType(Uri.fromFile(file), "text/x-vcard");
			ctx.startActivity(i);
		} catch (IOException e) {
			Logger.w("Cannot add person to address book", e);
			Toast.makeText(ctx, "Cannot add person to address book", Toast.LENGTH_LONG).show();
		}
	}

	private static File getVcfFile(Context ctx, Person person) throws IOException {
		// FIXME: move to subdir and cleanup
		final File dir = new File(Environment.getExternalStorageDirectory(), "perssearch/tmp");
		if (!dir.isDirectory()) {
			dir.mkdirs();
		}
		File tmpFile = File.createTempFile(FILENAME_PREFIX, FILENAME_EXTENTION, dir);
		FileWriter fw = new FileWriter(tmpFile);
		fw.write(person.getVcard());
		fw.flush();
		fw.close();
		Handler h = new Handler();
		h.post(new Runnable() {
			@Override
			public void run() {
				try {
					final long now = System.currentTimeMillis();
					File[] oldFiles = dir.listFiles(new FileFilter() {
						@Override
						public boolean accept(File file) {
							if (now - file.lastModified() > HOUR_IN_MILLIES) {
								if (file.getName().endsWith(FILENAME_EXTENTION)) {
									return true;
								}
							}
							return false;
						}
					});
					for (File file : oldFiles) {
						file.delete();
					}
				} catch (Exception e) {
					Logger.i("Cannot delete temp files");
				}
			}
		});
		return tmpFile;
	}

}
