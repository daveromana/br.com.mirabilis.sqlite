package br.com.mirabilis.sqlite.cypher;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Class that crypt and decrypt file.
 *
 * @author Rodrigo Sim�es Rosa.
 */
public class CypherFileManager {

    private File file;
    private CypherType cypherType;

    /**
     * Constructor
     *
     * @param file
     * @param cypherType
     */
    public CypherFileManager(File file, CypherType cypherType) {
        this.file = file;
        this.cypherType = cypherType;
    }

    /**
     * Encrypt
     *
     * @param file
     * @param cypherType
     * @throws java.io.IOException
     */
    public static void encrypt(File file, CypherType cypherType)
            throws IOException {
        new CypherFileManager(file, cypherType).encrypt();
    }

    /**
     * Decrypt
     *
     * @param file
     * @param cypherType
     * @throws java.io.IOException
     */
    public static void decrypt(File file, CypherType cypherType)
            throws IOException {
        new CypherFileManager(file, cypherType).decrypt();
    }

    /**
     * Decrypt
     *
     * @throws java.io.IOException
     */
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
                contentDecrypted = Base64.decode(content.toString().getBytes(),
                        Base64.DEFAULT);
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

    /**
     * Encrypt
     *
     * @throws java.io.IOException
     */
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
                contentDecrypted = Base64.encode(content.toString().getBytes(),
                        Base64.DEFAULT);
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

    /**
     * {@link br.com.mirabilis.sqlite.cypher.CypherFileManager.CypherType} Type of cypher
     *
     * @author Rodrigo Sim�es Rosa
     */
    public enum CypherType {
        BASE64;
    }

}
