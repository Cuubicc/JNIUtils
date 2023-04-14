package org.cubic.jniutils;

import java.nio.file.FileSystemNotFoundException;
import java.nio.file.FileSystems;
import java.nio.file.ProviderNotFoundException;
import java.util.Arrays;
import java.util.List;

public enum OperatingSystem {
    WindowsX64,
    WindowsX86,
    WindowsArm64,
    MacOSx64,
    MacOSarm64,
    LinuxX64,
    LinuxArm64,
    LinuxArm32;

    public List<String> getLibExtensions(){
        if(this.name().contains("Win"))
            return Arrays.asList("dll");
        if(this.name().contains("Mac"))
            return Arrays.asList("so", "dylib");
        if(this.name().contains("Linux"))
            return Arrays.asList("so", "dylib");
        throw new UnsupportedOperatingSystemException();
    }

    public static OperatingSystem current(){
        String name = System.getProperty("os.name");
        String arch = System.getProperty("os.arch");
        if(name.contains("Linux")){
            if(arch.startsWith("arm") || arch.startsWith("aarch64"))
                return arch.contains("64") || arch.startsWith("armv8") ?  LinuxArm64 : LinuxArm32;
            return LinuxX64;
        }
        if(name.contains("Mac")){
            return arch.startsWith("aarch64") ? MacOSarm64 : MacOSx64;
        }
        if(name.contains("Windows")){
            if(arch.contains("64"))
                return arch.startsWith("aarch64") ? WindowsArm64 : WindowsX64;
            return WindowsX86;
        }
        throw new UnsupportedOperatingSystemException();
    }

    public static boolean isPosixCompliant() {
        try {
            return FileSystems.getDefault()
                    .supportedFileAttributeViews()
                    .contains("posix");
        } catch (FileSystemNotFoundException
                | ProviderNotFoundException
                | SecurityException e) {
            return false;
        }
    }
}
