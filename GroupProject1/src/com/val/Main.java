package com.val;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Run run = new Run();
        List<Shipment> shipmentList;
        List<Warehouse> warehouseList;

        //read in all json shipment files from "input" directory and move them to "processed" directory after processing;
        shipmentList = run.processJsonInputFiles(MessageFormat.format("{0}/input", System.getProperty("user.dir")),
                                                 MessageFormat.format("{0}/processed", System.getProperty("user.dir")));

        //create warehouses if they do not exist; add shipments to warehouses;
        //duplicate shipments are not allowed;
        warehouseList = run.addShipmentsToWarehouses(shipmentList);

        //get the first warehouse in the array and export its shipments to output directory
        if (!warehouseList.isEmpty()) {
            Warehouse warehouse = warehouseList.get(0);
            run.exportAllShipmentsFromWarehouse(warehouse, MessageFormat.format( "{0}/output",System.getProperty("user.dir")));
        }

        //show the list of all shipments for each warehouse
        System.out.println("\n\nShow the list of all shipments for each warehouse: ");
        for (Warehouse w : warehouseList) {
            System.out.println(w.getWarehouseId()+":");
            System.out.println("\t\t"+ w.exportAllShipmentsAsJsonString());
        }
    }
}

