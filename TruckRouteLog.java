/*
 * Name: Mithil Agneya
 * Date: 05-29-25
 * Assignment: Java Files Project
 */

 //importing libraries
 import java.io.*;
 import java.time.LocalDate;
 import java.time.format.DateTimeFormatter;
 import java.time.format.DateTimeParseException;
 import java.util.*;
 import java.util.stream.Collectors;
 
 public class TruckRouteLog {
    //attributes
     private static final Scanner scanner = new Scanner(System.in);
     private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
     private static final String fileName = "routes.txt";
     private static ArrayList<String> routes = new ArrayList<>();
 
     public static void main(String[] args) {
         loadRoutes();
         int choice;
 
         do {
            //ui for user input
             System.out.println("\nTruck Route Log Menu:");
             System.out.println("1. View All Routes");
             System.out.println("2. Add New Route");
             System.out.println("3. Delete Route");
             System.out.println("4. Search Route");
             System.out.println("5. Filter Routes by Date Range");
             System.out.println("6. Filter by City or Shipment Type");
             System.out.println("7. Edit Route");
             System.out.println("8. Statistics Report");
             System.out.println("9. Backup Routes");
             System.out.println("10. Exit");
             System.out.print("Choose an option: ");
            
             //error trapping
             while (!scanner.hasNextInt()) {
                 System.out.print("Please enter a valid number: ");
                 scanner.next();
             }
             choice = scanner.nextInt();
             scanner.nextLine();
 
             switch (choice) {
                 case 1 -> viewRoutes();
                 case 2 -> addRoute();
                 case 3 -> deleteRoute();
                 case 4 -> searchRoute();
                 case 5 -> filterByDateRange();
                 case 6 -> filterByKeyword();
                 case 7 -> editRoute();
                 case 8 -> statisticsReport();
                 case 9 -> backupRoutes();
                 case 10 -> {
                     saveRoutes();
                     System.out.println("Exiting. Data saved.");
                 }
                 default -> System.out.println("Invalid option. Try again.");
             }
         } while (choice != 10);
     }
 
     private static void loadRoutes() {
         routes.clear();
         try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
             String line;
             while ((line = br.readLine()) != null) {
                 if (!line.trim().isEmpty()) {
                     routes.add(line);
                 }
             }
             
         } catch (FileNotFoundException e) {
             System.out.println("No existing routes found. Starting fresh.");
         } catch (IOException e) {
             System.out.println("Error reading file: " + e.getMessage());
         }
     }
 
     private static void saveRoutes() {
         try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
             for (String route : routes) {
                 bw.write(route);
                 bw.newLine();
             }
         } catch (IOException e) {
             System.out.println("Error writing to file: " + e.getMessage());
         }
     }
 
     private static void backupRoutes() {
         String backupName = "backup_routes.txt";
         try (BufferedWriter bw = new BufferedWriter(new FileWriter(backupName))) {
             for (String route : routes) {
                 bw.write(route);
                 bw.newLine();
             }
             System.out.println("Backup saved as " + backupName);
         } catch (IOException e) {
             System.out.println("Backup failed: " + e.getMessage());
         }
     }
 
     private static void viewRoutes() {
         if (routes.isEmpty()) {
             System.out.println("No routes logged yet.");
         } else {
             routes.stream().sorted().forEach(System.out::println);
         }
     }
 
     private static void addRoute() {
         System.out.print("Enter date (YYYY-MM-DD): ");
         String date = scanner.nextLine();
         if (!isValidDate(date)) {
             System.out.println("Invalid date format.");
             return;
         }
         System.out.print("From (City ST): ");
         String from = scanner.nextLine();
         System.out.print("To (City ST): ");
         String to = scanner.nextLine();
         System.out.print("Shipment Type: ");
         String type = scanner.nextLine();
 
         String entry = date + ": " + from + " to " + to + "; " + type;
         routes.add(entry);
         System.out.println("Route added.");
     }
 
     private static boolean isValidDate(String date) {
         try {
             LocalDate.parse(date, dateFormat);
             return true;
         } catch (DateTimeParseException e) {
             return false;
         }
     }
 
     private static void deleteRoute() {
         viewRoutes();
         System.out.print("Enter full route text to delete: ");
         String target = scanner.nextLine();
         if (routes.remove(target)) {
             System.out.println("Route deleted.");
         } else {
             System.out.println("Route not found.");
         }
     }
 
     private static void searchRoute() {
         System.out.print("Enter keyword to search: ");
         String keyword = scanner.nextLine().toLowerCase();
         routes.stream()
                 .filter(route -> route.toLowerCase().contains(keyword))
                 .forEach(System.out::println);
     }
 
     private static void filterByDateRange() {
        //filtering date; getting user input
         System.out.print("Enter start date (YYYY-MM-DD): ");
         String start = scanner.nextLine();
         System.out.print("Enter end date (YYYY-MM-DD): ");
         String end = scanner.nextLine();
 
         try {
             LocalDate startDate = LocalDate.parse(start, dateFormat);
             LocalDate endDate = LocalDate.parse(end, dateFormat);
 
             routes.stream()
                   .filter(r -> {
                       String date = r.split(":")[0];
                       LocalDate routeDate = LocalDate.parse(date, dateFormat);
                       return (routeDate.isEqual(startDate) || routeDate.isAfter(startDate)) &&
                              (routeDate.isEqual(endDate) || routeDate.isBefore(endDate));
                   })
                   .forEach(System.out::println);
 
         } catch (DateTimeParseException e) {
             System.out.println("Invalid date format.");
         }
     }
 
     private static void filterByKeyword() {
         System.out.print("Filter by keyword (city or type): ");
         String keyword = scanner.nextLine().toLowerCase();
         routes.stream()
         //filtering routes based on query
               .filter(r -> r.toLowerCase().contains(keyword))
               .forEach(System.out::println);
     }
 
     private static void editRoute() {
         viewRoutes();
         System.out.print("Enter the full route to edit: ");
         String oldRoute = scanner.nextLine();
         if (!routes.contains(oldRoute)) {
             System.out.println("Route not found.");
             return;
         }
 
         System.out.print("Enter new route in format YYYY-MM-DD: From ST to To ST; Type: ");
         String newRoute = scanner.nextLine();
         if (!validateRouteFormat(newRoute)) {
             System.out.println("New route format is invalid.");
             return;
         }
         routes.set(routes.indexOf(oldRoute), newRoute);
         System.out.println("Route updated.");
     }
 
     private static boolean validateRouteFormat(String route) {
         // check whether route is in intended format
         try {
             String[] parts = route.split(":");
             if (parts.length < 2) return false;
             String datePart = parts[0].trim();
             if (!isValidDate(datePart)) return false;
             String rest = parts[1];
             if (!rest.contains(" to ") || !rest.contains(";")) return false;
             return true;
         } catch (Exception e) {
             return false;
         }
     }
 
     private static void statisticsReport() {
         System.out.println("\n--- Route Statistics ---");
         System.out.println("Total Routes: " + routes.size());
 
         Map<String, Long> shipmentCounts = routes.stream()
                 .map(r -> r.substring(r.indexOf(";") + 2))
                 .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
 
         shipmentCounts.forEach((k, v) -> System.out.println(k + ": " + v));
 
         List<String> destinations = routes.stream()
                 .map(r -> r.split(" to ")[1].split(";")[0])
                 .collect(Collectors.toList());
 
         Map<String, Long> destCounts = destinations.stream()
                 .collect(Collectors.groupingBy(d -> d, Collectors.counting()));
 
         System.out.println("\nTop Destinations:");
         destCounts.entrySet().stream()
                 .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                 .limit(5)
                 .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
     }
 }
 