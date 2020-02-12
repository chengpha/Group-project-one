# Group-project-one

Group Assignment 1 - ICS 372

Compile and run the program using JavaFX.

The program has these buttons:

1. Choose File - When clicked, this button opens up a dialogue box where youc an choose which JSON file to open and import all the shipment data.
2. Display All Shipments - When clicked this displays all the shipments that were imported from the JSON and were later added in manually. Click the close button to close this display.
3. Disable Freight for Selected Warehouse - This disables a warehouse from receiving a shipment. Click again to enable freight shipment.
4. Export All Shipments for Selected Warehouse To Json File - Exports all the shipments that are currently saved in the program to a JSON file.

This IntelliJ project has three directories in the root folder of the project: input, output and archive. "Input" directory is not a required directory for the project to run because the json files that are to be processed can be picked up from any location using "Choose File" button on the UI of the application. For the sake of convenience, the directory had been added so that all JSON files could be dropped into one "input" folder for processing. "Archive" directory, as its name suggests, archives all processed files. "Output" directory stores all warehouse shipments extracts. The naming convention of the output file is the warehouse ID followed by a date stamp converted to a string and separated by a "\_". That is also a Maven project as it uses Maven to download Gson library to handle JSON parsing.
