package br.com.mirabilis.sqlite.cypher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import android.util.Base64;
import android.util.Log;

public class CypherFileManager {

	private File file;
	private CypherType cypherType;

	public CypherFileManager(File file, CypherType cypherType) {
		this.file = file;
		this.cypherType = cypherType;
	}

	public void decrypt() throws IOException {
		BufferedReader reader = null;
		FileWriter fileWriter = null;
		reader = new BufferedReader(new FileReader(file));
		StringBuilder content = new StringBuilder();

		while (reader.ready()) {
			content.append(reader.readLine());
		}

		byte[] contentDecrypted = null;
		
		switch (cypherType) {
			case BASE64:
				contentDecrypted = Base64.decode(content.toString().getBytes(),Base64.DEFAULT);
				break;
		}

		File temp = new File(file.getAbsolutePath() + "_temp");

		if (!temp.exists()) {
			temp.createNewFile();
		}

		fileWriter = new FileWriter(temp);
		fileWriter.append(new String(contentDecrypted, "UTF-8"));
		fileWriter.flush();

		String fileOriginal = file.getAbsolutePath();
		file.delete();

		temp.renameTo(new File(fileOriginal));

		reader.close();
		fileWriter.close();

		reader.close();
	}

	public void encrypt() throws IOException {
		BufferedReader reader = null;
		FileWriter fileWriter = null;
		reader = new BufferedReader(new FileReader(file));

		StringBuilder content = new StringBuilder();

		while (reader.ready()) {
			content.append(reader.readLine());
		}

		byte[] contentDecrypted = null;
		
		switch (cypherType) {
			case BASE64:
				contentDecrypted = Base64.encode(content.toString().getBytes(), Base64.DEFAULT);
				break;
		}
		
		File temp = new File(file.getAbsolutePath() + "_temp");

		if (!temp.exists()) {
			temp.createNewFile();
		}

		fileWriter = new FileWriter(temp);
		fileWriter.append(new String(contentDecrypted, "UTF-8"));
		fileWriter.flush();

		String fileOriginal = file.getAbsolutePath();
		file.delete();

		temp.renameTo(new File(fileOriginal));

		reader.close();
		fileWriter.close();

		reader.close();
	}
}
