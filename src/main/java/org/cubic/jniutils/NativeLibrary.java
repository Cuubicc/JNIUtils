package org.cubic.jniutils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class NativeLibrary {

    private final String lib;

    private final LibFinder libFinder;

    private boolean loaded;

    public NativeLibrary(String lib, LibFinder libFinder) {
        this.lib = lib;
        this.libFinder = libFinder;
        this.loaded = false;
    }

    public void load(){
        this.loaded = true;
        String[] split = lib.split("/");
        String[] parts = split[split.length - 1].split("\\.");
        File tempFile;
        try {
            tempFile = File.createTempFile("tmp_jni_lib_" + parts[0], ".dll");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Files.copy(libFinder.findLibrary(lib).openStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch(Exception e){
            tempFile.delete();
            throw new RuntimeException(e);
        }
        try {
            System.load(tempFile.getAbsolutePath());
        } finally {
            if(OperatingSystem.isPosixCompliant()){
                tempFile.delete();
                return;
            }
            tempFile.deleteOnExit();
        }
    }

    public static void load(String lib){
        NativeLibrary nativeLibrary = new NativeLibrary(lib, new DefaultLibFinder());
        nativeLibrary.load();
    }
}
