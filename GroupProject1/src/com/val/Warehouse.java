package com.val;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private String warehouse_id;
    private List<Shipment> shipments = new ArrayList<>();
    boolean isFreightReceiptEnabled = true;

    public Warehouse(String warehouse_id){
        this.warehouse_id = warehouse_id;
    }

    public boolean addShipment(Shipment shipment){
        if (isFreightReceiptEnabled){
            shipments.add(shipment);
            return true;
        } else return false;
    }

    public String getWarehouseId (){ return warehouse_id;}

    public List<Shipment> getAllShipments(){
        return shipments;
    }

    public void enableFreightReceipt(){
        isFreightReceiptEnabled = true;
    }

    public void disableFreightReceipt(){
        isFreightReceiptEnabled = false;
    }

    public String exportAllShipmentsAsJsonString(){
        return new Gson().toJson(new Shipments(shipments));
    }
}
