package com.val;

import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Gson gson = new Gson();
        List<Shipment> shipmentList = new ArrayList<>();

        //read in all json shipment files from the project directory
        try {
            Files.walk(Paths.get(System.getProperty("user.dir")))
                    .filter(f -> {
                        if (FilenameUtils.getExtension(String.valueOf(f)).contains("json")) return true;
                        else return false;
                    })
                    .forEach(f -> {
                        try (Reader reader = new FileReader(String.valueOf(f))) {
                            List<Shipment> list = gson.fromJson(reader, Shipments.class).getShipmentList();
                            shipmentList.addAll(list);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
            shipmentList.forEach( s-> System.out.println(s));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

