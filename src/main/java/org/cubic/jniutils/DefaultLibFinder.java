package org.cubic.jniutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * TODO: Rewrite this shite code
 */
public class DefaultLibFinder implements LibFinder {
    @Override
    public URL findLibrary(String lib) {
        if(!System.getProperty("os.name").contains("win") && !System.getProperty("os.name").contains("Win"))
            throw new UnsupportedOperationException();
        URL url = DefaultLibFinder.class.getResource(lib);
        if(url != null)
            return url;
        FileInputStream in1;
        try {
            in1 = new FileInputStream(lib);
        } catch (FileNotFoundException e) {
            in1 = null;
        }
        if(in1 != null) {
            try {
                return new File(lib).toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        FileInputStream in2;
        try {
            in2 = new FileInputStream(new File("").getAbsolutePath() + "/src/main/resources/" + lib);
        } catch (FileNotFoundException e) {
            in2 = null;
        }
        if(in2 != null) {
            try {
                return new File(new File("").getAbsolutePath() + "/src/main/resources/" + lib).toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        FileInputStream in3;
        try {
            in3 = new FileInputStream(new File("").getAbsolutePath() + "/src/test/resources/" + lib);
        } catch (FileNotFoundException e) {
            in3 = null;
        }
        if(in3 != null) {
            try {
                return new File(new File("").getAbsolutePath() + "/src/test/resources/" + lib).toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        throw new RuntimeException();
    }
}
