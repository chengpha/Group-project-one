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
        List<Warehouse> warehouseList = new ArrayList<>();

        //read in all json shipment files from the input directory
        try {
            Files.walk(Paths.get(String.format(System.getProperty("user.dir"), "%s/input")))
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create warehouses if they do not exist; add the shipments to the warehouses
        for (Shipment s : shipmentList) {
            Warehouse warehouse;
            if (warehouseList.stream().noneMatch(w -> w.getWarehouseId().equals(s.getWarehouseId()))) {
                warehouse = new Warehouse(s.getWarehouseId());
                warehouseList.add(warehouse);
            } else
                warehouse = warehouseList.stream()
                        .filter(w -> w.getWarehouseId().equals(s.getWarehouseId()))
                        .findFirst()
                        .get();

            if(warehouse.addShipment(s))
                System.out.printf("The shipment %s has been added to the warehouse %s.%n",
                        s.getShipmentId(),
                        warehouse.getWarehouseId());
            else
                System.out.printf(
                        "Freight receipt is disabled for the warehouse: %s. The shipment %s has not been added.%n",
                        warehouse.getWarehouseId(),
                        s.getShipmentId());
        }

        System.out.println("======================");
        for (Warehouse warehouse : warehouseList) {
            System.out.println(warehouse.getWarehouseId());
            System.out.println(warehouse.exportAllShipmentsAsJsonString());
        }
        System.out.println("======================");
    }
}

