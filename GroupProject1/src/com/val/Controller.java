package com.val;

import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.Files.move;

public class Controller {
    private Gson gson = new Gson();
    private List<Warehouse> warehouseList = new ArrayList<>();

    public Controller(){}

    public List<Warehouse> processJsonInputFile(String input, String processed){
        List<Shipment> shipmentList = new ArrayList<>();

        //check if file exists
        if (Files.exists(Paths.get(input))) {
            File f = new File(input);
            if (!FilenameUtils.getExtension(String.valueOf(f)).contains("json"))
                return null;

            try (Reader reader = new FileReader(String.valueOf(f))) {
                List<Shipment> list = gson.fromJson(reader, Shipments.class).getShipmentList();
                shipmentList.addAll(list);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*
            create warehouses if they do not exist; add shipments to warehouses;
            duplicate shipments are not allowed;
            */
            for (Shipment s : shipmentList) {
                Warehouse warehouse;
                if (warehouseList.stream().noneMatch(w -> w.getWarehouseId().equals(s.getWarehouseId()))) {
                    warehouse = new Warehouse(s.getWarehouseId());
                    warehouseList.add(warehouse);
                } else
                    warehouse = warehouseList
                            .stream()
                            .filter(w -> w.getWarehouseId().equals(s.getWarehouseId()))
                            .findFirst()
                            .get();

                //if the freight receipt in the warehouse is disabled, do not add any shipments
                if(!warehouse.isFreightReceiptEnabled()){
                    System.out.printf("Freight receipt is disabled for warehouse %s.%n", warehouse.getWarehouseId());
                    continue;
                }

                if(warehouse.addShipment(s))
                    System.out.printf("Shipment %s has been added to the warehouse %s.%n",
                            s.getShipmentId(),
                            warehouse.getWarehouseId());
                else
                    System.out.printf(
                            "Duplicate shipment ID: %s for the warehouse: %s. The shipment won't be added.%n",
                            s.getShipmentId(),
                            warehouse.getWarehouseId());
            }

            /*
            move processed files to "processed" directory;
            if the file already exists in the directory, overwrite it;
            */
            Path check = null;
            try {
                Path temp = Paths.get(processed + "/" + f.getName());
                if (Files.exists(temp)) {
                    Files.delete(temp);
                }
                check = move(Paths.get(String.valueOf(f)), temp);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (check != null)
                System.out.printf("File %s has been moved successfully to %s %n", f.getName(), processed);
            else
                System.out.printf("Failed to move %s file to %s %n", f.getName(), processed);

            return warehouseList;
        } else {
            return null;
        }
    }

    public void listAllWarehousesWithShipments(){
        System.out.println("\n\nShow the list of all shipments for each warehouse: ");
        for (Warehouse w : warehouseList) {
            System.out.println(w.getWarehouseId()+":");
            System.out.println("\t\t"+ w.exportAllShipmentsToJsonString());
        }
    }
}
