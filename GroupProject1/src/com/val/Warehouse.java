package com.val;

import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private String warehouse_id;
    private List<Shipment> shipments;
    boolean isFreightReceiptEnabled = true;

    public Warehouse(String warehouse_id){
        this.warehouse_id = warehouse_id;
        shipments = new ArrayList<>();
    }

    public void addShipment(Shipment shipment){
        if (isFreightReceiptEnabled)
            shipments.add(shipment);
    }

    public List<Shipment> getAllShipments(){
        return shipments;
    }

    public void enableFreightReceipt(){
        isFreightReceiptEnabled = true;
    }

    public void disableFreightReceipt(){
        isFreightReceiptEnabled = false;
    }

    public void exportAllShipments(){

    }
}
