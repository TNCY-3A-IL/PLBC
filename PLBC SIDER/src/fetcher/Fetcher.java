package fetcher;
/**
 * Classe qui permet de récupérer un fichier tar.gz et extrait son contenu (pour un tar qui contient un seul fichier) 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

public class Fetcher {
	public Fetcher(String url) {
		String fileName = url.substring(
				url.lastIndexOf('/') + 1);
		System.out.println("begin dl of " + fileName);
		InputStream input = null;
		FileOutputStream writeFile = null;

		try {
			URL u = new URL(url);
			URLConnection connection = u.openConnection();
			int fileLength = connection.getContentLength();

			if (fileLength == -1) {
				System.out.println("Invalide URL or file.");
				return;
			}

			input = connection.getInputStream();
			writeFile = new FileOutputStream(fileName);
			byte[] buffer = new byte[1024];
			int read;

			while ((read = input.read(buffer)) > 0)
				writeFile.write(buffer, 0, read);
			writeFile.flush();
		} catch (IOException e) {
			System.out.println("Error while trying to download the file.");
			e.printStackTrace();
		} finally {
			try {
				writeFile.close();
				input.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("end dl of " + fileName);
	}

	public void extract(String path) {
		System.out.println("begin extract of " + path);
		FileOutputStream writeFile = null;
		InputStream input = null;
		try {
			input = new FileInputStream(new File(path));
			input = new GZIPInputStream(input);
			writeFile = new FileOutputStream(path.substring(0,
					path.length() - 3));
			byte[] buffer = new byte[1024];
			int read;

			while ((read = input.read(buffer)) > 0)
				writeFile.write(buffer, 0, read);
			writeFile.flush();

			writeFile.close();
			input.close();
		} catch (IOException e) {
			System.out.println("Error extracting the file.");
			e.printStackTrace();
		}
		finally {
			try {
				input.close();
				writeFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("end extract of " + path);
	}

	public void deleteFile(String path) {
		try {
			File file = new File(path);

			if (file.delete()) {
				System.out.println(file.getName() + " is deleted!");
			} else {
				System.out.println("Delete operation is failed.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}