package com.val;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Warehouse {
    private String warehouse_id;
    private List<Shipment> shipments = new ArrayList<>();
    boolean freightReceiptEnable = true;

    public Warehouse(String warehouse_id){
        this.warehouse_id = warehouse_id;
    }

    public boolean addShipment(Shipment shipment){
        if (freightReceiptEnable)
            if (shipments.stream().noneMatch(s -> s.getShipmentId().equals(shipment.getShipmentId()))) {
                shipments.add(shipment);
                return true;
            } else return false;
        else return false;
    }

    public String getWarehouseId (){ return warehouse_id;}

    public void enableFreightReceipt(){
        freightReceiptEnable = true;
    }

    public void disableFreightReceipt(){
        freightReceiptEnable = false;
    }

    public boolean isFreightReceiptEnabled(){ return freightReceiptEnable; }

    public String exportAllShipmentsToJsonString(){
        return new Gson().toJson(new Shipments(shipments));
    }

    public void exportAllShipmentsToFile(String location){
        if (Files.exists(Paths.get(location))) {
            String json = exportAllShipmentsToJsonString();
            try {
                Files.write(Paths.get(MessageFormat.format("{0}/{1}_{2}.json",
                        location,
                        getWarehouseId(),
                        new Date().getTime())),
                        json.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
            return;
    }
}
