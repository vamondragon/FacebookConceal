package com.alfonso.facebookconceal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.Entity;
import com.facebook.crypto.util.SystemNativeCryptoLibrary;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Praxis on 21/04/2016.
 */
public class EncryptFile {

    private Context context;
    Common common;

    public EncryptFile(Context context) {
        this.context = context;
        common = new Common(context);
    }

    public void encodeAndSaveFile(Bitmap photo, String folderName, String filename) {

        try {

            Crypto crypto = new Crypto(new SharedPrefsBackedKeyChain(context),
                    new SystemNativeCryptoLibrary());

            if (!crypto.isAvailable()) {
                common.logMessages("Cipher no disponible:  ", Log.ERROR);
                return;
            }

            File file = new File(new File(common.getAppDirectory(folderName)), filename);

            OutputStream fileStream = new BufferedOutputStream(
                    new FileOutputStream(file));

            OutputStream outputStream = crypto.getCipherOutputStream(
                    fileStream, new Entity("id_image"));

            outputStream.write(bitmapToBytes(photo));
            outputStream.close();

        } catch (UnsupportedOperationException e) {
           common.logMessages("UnsupportedOperationException:  " + e.toString(), Log.ERROR);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            common.logMessages("FileNotFoundException:  " + e.toString(), Log.ERROR);
        } catch (IOException e) {
            common.logMessages("IOException:  " + e.toString(), Log.ERROR);
        } catch (Exception e) {
            common.logMessages("Exception:  " + e.toString(), Log.ERROR);
        }
    }

    // convert Bitmap to bytes
    private byte[] bitmapToBytes(Bitmap photo) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    // convert bytes to Bitmap
    private Bitmap bytesToBitmap(byte[] bytes) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }

    // decode encrypted file and returns Bitmap
    public Bitmap decodeFile(String folderName, String filename) {

        Crypto crypto = new Crypto(
                new SharedPrefsBackedKeyChain(context),
                new SystemNativeCryptoLibrary());

        File file = new File(new File(common.getAppDirectory(folderName)), filename);

        try {

            FileInputStream fileStream = new FileInputStream(file);
            InputStream inputStream = crypto.getCipherInputStream(fileStream,
                    new Entity("id_image"));

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            int read;
            byte[] buffer = new byte[1024];

            while ((read = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            inputStream.close();
            Bitmap bitmap = bytesToBitmap(out.toByteArray());

            return bitmap;

        } catch (UnsupportedOperationException e) {
            common.logMessages("UnsupportedOperationException:  " + e.toString(), Log.ERROR);
        } catch (FileNotFoundException e) {
            common.logMessages("FileNotFoundException:  " + e.toString(), Log.ERROR);
        } catch (IOException e) {
            common.logMessages("IOException:  " + e.toString(), Log.ERROR);
        } catch (Exception e) {
            common.logMessages("Exception:  " + e.toString(), Log.ERROR);
        }

        return null;
    }
}
