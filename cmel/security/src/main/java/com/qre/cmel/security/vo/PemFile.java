package com.qre.cmel.security.vo;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.Key;

public class PemFile {

    /**
     * pemObject
     */
    private PemObject pemObject;

    /**
     *
     * @param key
     * @param description
     */
    public PemFile(Key key, String description) {
        this.pemObject = new PemObject(description, key.getEncoded());
    }

    /**
     *
     * @param filename
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void write(String filename) throws FileNotFoundException, IOException {
        PemWriter pemWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream(filename)));
        try {
            pemWriter.writeObject(this.pemObject);
        } finally {
            pemWriter.close();
        }
    }
}
