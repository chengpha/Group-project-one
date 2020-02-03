package com.val;

import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.nio.file.Files.move;

public class Run {
    private Gson gson = new Gson();
    private List<Shipment> shipmentList = new ArrayList<>();
    private List<Warehouse> warehouseList = new ArrayList<>();

    public Run(){}

    public List<Warehouse> getWarehouseList(){return warehouseList;}

    public void processJsonInputFiles(String input, String processed){
        try {
            Files.walk(Paths.get(input))
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
                        //move processed files to "processed" directory; if they exist, overwrite them;
                        Path check = null;
                        try {
                            Path temp = Paths.get(processed + "/" + f.getFileName());
                            if(Files.exists(temp)){
                                Files.delete(temp);
                            }
                            check = move(Paths.get(String.valueOf(f)),temp);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(check != null)
                            System.out.println("File has been moved successfully");
                        else
                            System.out.println("Failed to move the file");
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addShipmentsToWarehouses(){
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
            if(!warehouse.isFreightReceiptEnabled())
                continue;

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
    }

    public void exportAllShipmentsFromWarehouse(Warehouse warehouse, String location){
        String json = warehouse.exportAllShipmentsAsJsonString();
        try {
            Files.write(Paths.get(MessageFormat.format("{0}/{1}_{2}.json",
                    location,
                    warehouse.getWarehouseId(),
                    new Date().getTime())),
                    json.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
