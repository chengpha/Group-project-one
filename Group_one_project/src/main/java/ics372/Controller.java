package ics372;

/**
 * Class ICS 372 Spring 2020
 * Programming Assignment 1
 *
 * @authors: Valentin Kalenik, Cheng Pha, Luke Pha,Tommy Moua, Tina Martinez, Jesus Flores
 * <p>
 * Controller Class
 */

import com.google.gson.Gson;
import org.apache.commons.io.FilenameUtils;
import org.w3c.dom.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.*;

/**
 * This class manages reading a JSON file and creating warehouses based on shipments.
 * Allows to retrieve the warehouse list and printing the list.
 */
public class Controller {
    private Gson gson = new Gson();
    private List<Warehouse> warehouseList = new ArrayList<>();

    public Controller() {
    }

    public List<Warehouse> getWarehouseList() {
        return warehouseList;
    }

    public String processJsonInputFile(File f) {
        List<Shipment> shipmentList = new ArrayList<>();
        String msg = "";

        try (Reader reader = new FileReader(String.valueOf(f))) {
            String ext = FilenameUtils.getExtension(f.getName());
            if(ext.equals("json")) {
                List<Shipment> list = (List<Shipment>) gson.fromJson(reader, Shipments.class).getShipmentList();
                shipmentList.addAll(list);
            } else {
                try {
                    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                    Document doc = dBuilder.parse(f);
                    doc.getDocumentElement().normalize();

                    // Gets all the shipment lists
                    NodeList shipmentNodeList = doc.getElementsByTagName("Shipment");

                    // Loop through each shipment
                    for (int index = 0; index < shipmentNodeList.getLength(); index++) {
                        Node shipment = shipmentNodeList.item(index);

                        if (shipment.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement = (Element) shipment;

                            // Gets all the values needed
                            String warehouseID = ((Element) (shipment.getParentNode())).getAttribute("id");
                            String shipmentID =  eElement.getAttribute("id");
                            String shipmentMethod = eElement.getAttribute("type");
                            Double weight = Double.parseDouble(doc.getElementsByTagName("Weight").item(index).getTextContent());
                            Long receiptDate = Long.parseLong(doc.getElementsByTagName("ReceiptDate").item(index).getTextContent());

                            // Creates a shipment and adds it to the list
                            Shipment s = new Shipment(warehouseID, shipmentID, shipmentMethod, weight, receiptDate);
                            shipmentList.add(s);
                        }
                    }

                    } catch(Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e) {
            msg = "File not found";
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
            if (!warehouse.isFreightReceiptEnabled()) {
                msg += String.format("Freight receipt is disabled for warehouse %s.%nShipment %s won't be added.%n",
                        warehouse.getWarehouseId(),
                        s.getShipmentId());
                continue;
            }

            if (warehouse.addShipment(s))
                msg += String.format("Shipment %s has been added to warehouse %s.%n",
                        s.getShipmentId(),
                        warehouse.getWarehouseId());
            else
                msg += String.format(
                        "Duplicate shipment ID: %s for warehouse: %s.%nShipment won't be added.%n",
                        s.getShipmentId(),
                        warehouse.getWarehouseId());
        }


        String fileHasBeenRead = f.getName() + " has been imported.\n";
        return fileHasBeenRead + msg;
    }

    public String printAllWarehousesWithShipments() {
        String msg = String.format("SHIPMENTS FOR ALL WAREHOUSES:%n");
        for (Warehouse w : warehouseList) {
            String[] shipments = w.exportAllShipmentsToJsonString().split("},");
            String temp = "";
            for (int i = 0; i < shipments.length; i++) {
                temp += String.format("%s%n\t\t\t\t\t\t\t\t  ", shipments[i] + (i < shipments.length - 1 ? "}," : ""));
            }
            msg += String.format("Warehouse ID - " + w.getWarehouseId() + ":%n\t\t" + temp + "%n");
        }
        return msg;
    }
}
