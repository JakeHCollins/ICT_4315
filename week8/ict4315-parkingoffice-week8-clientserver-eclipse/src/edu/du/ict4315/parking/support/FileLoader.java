///////////////////////////
// The FileLoader class loads CSV files using a simplistic parser.
// It has specialty parsers for ParkingLot records and for User records.
// File: FileLoader.java
// Author: M I Schwartz
///////////////////////////
package edu.du.ict4315.parking.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class FileLoader {

    private static final Logger logger = Logger.getLogger(FileLoader.class.getName());

    public static String[][] loadCsvFile(String filePath) {
        List<String[]> result = new ArrayList<>();
        try {
            File f = new File(filePath);
            if (f.exists()) {
                if (f.canRead()) {
                    BufferedReader reader = new BufferedReader(new FileReader(f));
                    String record;
                    while ((record = reader.readLine()) != null) {
                        if (record.length() == 0) {
                            continue;
                        } else if (record.charAt(0) == '#') {
                            continue; // Comment
                        }
                        result.add(record.split(","));
                    }
                    reader.close();
                } else {
                    System.err.println("Cannot read file " + filePath);
                }
            } else {
                System.err.println("Cannot find file " + f.getCanonicalPath());
            }
        }
        catch (IOException e) {
            System.err.println(e);
        }

        return result.toArray(new String[0][0]);
    }

    public static int writeCsvFile(String filePath, String[][] records, String[] header) {
        int count = 0;
        try {
            File f = new File(filePath);
            if (f.canWrite()) {
                PrintWriter writer = new PrintWriter(new FileWriter(f));
                writer.println("# This file was automatically generated. Modify it at your own risk.\n");
                if (header != null) {
                    writer.println(String.join(", ", header));
                }
                for (String[] record : records) {
                    writer.println(String.join(", ", record));
                    count++;
                }
                writer.close();
            }
        }
        catch (IOException e) {
            System.err.println(e);
        }
        return count;
    }

}
