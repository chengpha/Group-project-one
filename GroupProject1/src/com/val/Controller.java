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

    public List<Warehouse> getWarehouseList(){
        return warehouseList;
    }

    public String processJsonInputFile(String input, String processed){
        List<Shipment> shipmentList = new ArrayList<>();
        String msg = "";

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
                     msg += String.format("Freight receipt is disabled for warehouse %s.%n", warehouse.getWarehouseId());
                    continue;
                }

                if(warehouse.addShipment(s))
                    msg += String.format("Shipment %s has been added to the warehouse %s.%n",
                            s.getShipmentId(),
                            warehouse.getWarehouseId());
                else
                    msg += String.format(
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
                msg += String.format("File %s has been successfully moved to %s %n", f.getName(), processed);
            else
                msg += String.format("Failed to move %s file to %s %n", f.getName(), processed);

            return msg;
        } else {
            return String.format("File specified does not exist: %s  %n", input);
        }
    }

    public String printAllWarehousesWithShipments(){
        String msg = "";
        for (Warehouse w : warehouseList)
            msg += String.format("Warehouse ID - " + w.getWarehouseId()+":%n\t\t"+ w.exportAllShipmentsToJsonString() +"%n");
        return msg;
    }
}
