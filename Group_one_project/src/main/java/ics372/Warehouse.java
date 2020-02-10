package ics372;


import com.google.gson.Gson;
import java.util.ArrayList;
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
            }
        return false;
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

    @Override
    public String toString(){ return warehouse_id;}
}
