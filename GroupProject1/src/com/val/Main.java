package com.val;

import java.text.MessageFormat;

public class Main {

    public static void main(String[] args) {
        Run run = new Run();
        //read in all json shipment files from "input" directory and move them to "processed" directory after processing;
        run.processJsonInputFiles(MessageFormat.format("{0}/input", System.getProperty("user.dir")),
                MessageFormat.format("{0}/processed", System.getProperty("user.dir")));

        //create warehouses if they do not exist; add shipments to warehouses;
        //duplicate shipments are not allowed;
        run.addShipmentsToWarehouses();

        //get the first warehouse in the array and export its shipments to output directory
        if (!run.getWarehouseList().isEmpty()) {
            Warehouse warehouse = run.getWarehouseList().get(0);
            run.exportAllShipmentsFromWarehouse(warehouse, MessageFormat.format( "{0}/output",System.getProperty("user.dir")));
        }

        //show the list of all shipments for each warehouse
        System.out.println("\n\nShow the list of all shipments for each warehouse: ");
        for (Warehouse w : run.getWarehouseList()) {
            System.out.println(w.getWarehouseId()+":");
            System.out.println("\t\t"+ w.exportAllShipmentsAsJsonString());
        }
    }
}

