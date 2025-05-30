
# Truck Route Log

Truck Route Log is a Java console application that allows users to log, manage, and analyze truck delivery routes. The program uses file I/O to store route data persistently across sessions.

## Features

* Add, view, edit, and delete truck routes
* Filter routes by date range or keyword (city or shipment type)
* Search for specific routes using keywords
* Generate basic statistics (top destinations, shipment type counts)
* Backup route data to a separate file
* Persistent data storage using `routes.txt`

## Route Format

Each route is stored using the following format:


`YYYY-MM-DD: FromCity ST to ToCity ST; Shipment Type`


Example:


`2023-07-23: Dallas TX to Miami FL; Auto Parts`


## How to Run

1. Compile the program using:

   
  `javac TruckRouteLog.java`
   
2. Run the program:

   
   `java TruckRouteLog`
  

## Files Used

* `routes.txt`: Stores the main list of logged truck routes
* `backup_routes.txt`: Optional backup file created during backups

## Notes

* The program uses standard Java libraries (no external dependencies)
* Route data is saved automatically upon exit
* Make sure `routes.txt` is in the same directory as the program if reusing saved data


