package ics372;
/**
 * Class ICS 372 Spring 2020
 * Programming Assignment 1
 *
 * @authors: Valentin Kalenik, Cheng Pha, Luke Pha,Tommy Moua, Tina Martinez, Jesus Flores
 * <p>
 * This class creates a shipment lists, which allows to get the list of shipments in a warehouse.
 */
import java.util.List;

public class Shipments {
    private List<Shipment> warehouse_contents;

    public Shipments(List<Shipment> warehouse_contents) {
        this.warehouse_contents = warehouse_contents;
    }

    public List<Shipment> getShipmentList() {
        return warehouse_contents;
    }
}
