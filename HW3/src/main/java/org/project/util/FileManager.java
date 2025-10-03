package org.project.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    public void save(String name, String email) {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdir();
        }

        try(var br = new BufferedWriter(new FileWriter("data/data.txt", true))) {
            br.write(name + " " + email);
            br.newLine();
        } catch (IOException e) {}
    }

}
