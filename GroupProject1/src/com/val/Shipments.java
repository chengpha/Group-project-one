package com.val;

import java.util.List;

public class Shipments {
    private List<Shipment> warehouse_contents;
    public Shipments(List<Shipment> warehouse_contents){
        this.warehouse_contents = warehouse_contents;
    }
    public List<Shipment> getShipmentList(){
        return warehouse_contents;
    }
}
