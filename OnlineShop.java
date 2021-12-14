import java.io.*;
import java.util.*;

class Product implements Comparable<Product> {
    public String name;
    public int price;
    public int weight;
    public int totalSaleInCash;
    public int totalSaleInUnits;

    public Product(String n, int p, int w) {
        name = n;
        this.weight = w;
        this.price = p;
    }

    public String getName() {
        return name;
    }

    public void setTotalSaleUnit(int allSale) {
        totalSaleInUnits = allSale;
    }

    public int TotalSaleInCash() {
        totalSaleInCash = totalSaleInUnits * price;
        return totalSaleInCash;
    }

    public int compareTo(Product other) {
        return Integer.compare(other.TotalSaleInCash(), this.TotalSaleInCash());
    }

    public int totalSaleInUnits() {
        return totalSaleInUnits;
    }

    public void printTotalSale() {
        System.out.printf("%-25s total Sale = %-5d Baht, %2d Units \n", name, totalSaleInCash, totalSaleInUnits);
    }
}

class Postage  {
    public String type;
    public int minWeight;
    public double maxWeight;
    public int rate;

    public Postage(String ty, int min, double max, int ra) {
        type = ty;
        minWeight = min;
        maxWeight = max;
        rate = ra;
    }

}

class Customer implements Comparable<Customer> {
    public String name;
    public String type;
    public int totalWeight = 0;
    public int totalBill = 0;
    public int totalBillProduct = 0;

    public int[] CustomerOrder = new int[5];

    public Customer(String n, String ty, int blue, int clip, int proj, int power, int adapter) {
        name = n;
        type = ty;
        CustomerOrder[0] = blue;
        CustomerOrder[1] = clip;
        CustomerOrder[2] = proj;
        CustomerOrder[3] = power;
        CustomerOrder[4] = adapter;
    }

    public String getType() {
        return type;
    }

    //process customer total weight and total bill all product
    public void processCustomer(ArrayList<Product> Prod) {
        totalBill = 0;

        System.out.printf("\n %-5s >> ", name);

        for (int i = 0; i < Prod.size(); i++) {
            System.out.printf("%s (%1d)  ", Prod.get(i).getName(), CustomerOrder[i]);
            totalBillProduct += CustomerOrder[i] * Prod.get(i).price;
            totalWeight += CustomerOrder[i] * Prod.get(i).weight;
        }

        System.out.printf("\n \t\t\t Product price = %5d    Total weight = %5d ", totalBillProduct, totalWeight);
    }

    //process Postage bill
    public void processPostage(ArrayList<Postage> Pos) {
        if (getType().trim().equals("E")) {
            for (int i = 0; i < 5; i++) {
                if (totalWeight > Pos.get(i).minWeight && totalWeight <= Pos.get(i).maxWeight) {
                    totalBill = totalBillProduct + Pos.get(i).rate;
                    System.out.printf("\n \t\t\t postage(%s)    = %5d ", getType().trim(), Pos.get(i).rate);
                }
            }
        }
        if (getType().trim().equals("R")) {
            for (int i = 5; i < 10; i++) {
                if (totalWeight > Pos.get(i).minWeight && totalWeight <= Pos.get(i).maxWeight) {
                    totalBill = totalBillProduct + Pos.get(i).rate;
                    System.out.printf("\n \t\t\t postage(%s)    = %5d ", getType().trim(), Pos.get(i).rate);
                }
            }
        }
        System.out.printf("   Total bill   = %5d    ", totalBill);
    }

    public int totalBill() {
        return totalBill;
    }

    public int compareTo(Customer other) {
        return Integer.compare(other.totalBill(), this.totalBill());
    }

    public void printSortTotalBill() {
        System.out.printf("%-8s total Bill = %-5d  \n", name, totalBill);
    }
}

class OnlineShop {
    public static void main(String[] args) {
        String str1, str2, str3, line = null;
        boolean found = false, found2 = false, found3 = false;
        Scanner scan = null;
        ArrayList<Postage> postages = new ArrayList<>();
        ArrayList<Product> products = new ArrayList<>();

        Scanner input = new Scanner(System.in);

        //**********************************   products     *********************************/

        System.out.print("Enter file name for products: ");
        str1 = input.nextLine();

        while (!found) {
            try {
                scan = new Scanner(new File(str1));
                found = true;
            } catch (FileNotFoundException e) {
                System.out.println(e);
                System.out.print("Enter file name for products: ");
                str1 = input.nextLine();
            }
        }
        while (scan.hasNext()) {
            line = scan.nextLine(); // bluetooth speaker, 1400, 300
            String[] buf = line.split(","); //split string to each string
            int price = Integer.parseInt(buf[1].trim());
            int weight = Integer.parseInt(buf[2].trim());

            products.add(new Product(buf[0], price, weight));
        }
        scan.close();

        //**********************************   postages     *********************************/
        double maxW;
        double inf = Double.POSITIVE_INFINITY;
        int i ;

        System.out.print("Enter file name for postage rates: ");
        str2 = input.nextLine();

        while (!found2) {
            try {
                scan = new Scanner(new File(str2));
                found2 = true;
            } catch (FileNotFoundException e) {
                System.out.println(e);
                System.out.print("Enter file name for postage rates: ");
                str2 = input.nextLine();
            }
        }
        while (scan.hasNext()) {
            line = scan.nextLine(); // E,    0,  100(inf),  50
            String[] buf = line.split(","); //split string to each string

            int minW = Integer.parseInt(buf[1].trim());

            if (buf[2].trim().equals("inf")) {
                maxW = inf;
                // System.out.println("inf");
            } else {
                maxW = Double.parseDouble(buf[2].trim());
            }

            int rate = Integer.parseInt(buf[3].trim());
            postages.add(new Postage(buf[0], minW, maxW, rate));
        }

        scan.close();


        //**********************************   customers     *********************************/

        ArrayList<Customer> customers = new ArrayList<>();
        System.out.print("Enter file name for customer orders: ");
        str3 = input.nextLine();


        while (!found3) {
            try {
                scan = new Scanner(new File(str3));
                found3 = true;
            } catch (FileNotFoundException e) {
                System.out.println(e);
                System.out.print("Enter file name for customer orders: ");
                str3 = input.nextLine();
            }

        }
        while (scan.hasNext()) {
            line = scan.nextLine(); //Annie, E, 3,  0, 1, 3, 0
            String[] buf = line.split(","); //split string to each string
            int check = 0;
            int[] arr = new int[5];

            try {

                //change type become E,R
                if (!buf[1].trim().equals("E") && !buf[1].trim().equals("R")) {
                    buf[1] = "R";
                    check += 1;
                }

                //fixing oh to zero
                for (i = 2; i < buf.length; i++) {
                    if (buf[i].trim().equals("o") || buf[i].trim().equals("O")) {
                        buf[i] = "0";
                        check += 1;
                    }
                }

                arr[0] = Integer.parseInt(buf[2].trim());
                arr[1] = Integer.parseInt(buf[3].trim());
                arr[2] = Integer.parseInt(buf[4].trim());
                arr[3] = Integer.parseInt(buf[5].trim());
                arr[4] = Integer.parseInt(buf[6].trim());


                if (check >= 1) {
                    System.out.println("\nInput error: " + line);
                    System.out.printf("Correction : %s %s %d %d %d %d %d \n", buf[0], buf[1], arr[0], arr[1], arr[2], arr[3], arr[4]);
                }

                //fixing negative number
                for (i = 0; i < arr.length; i++) {
                    if (arr[i] < 0) {
                        arr[i] = 0;
                        throw new Exception("number is negative");
                    }
                }


            } catch (NumberFormatException e) {
                System.out.println("\nInput error: " + line);
                System.out.printf("Correction : %s %s %d %d %d %d %d \n", buf[0], buf[1], arr[0], arr[1], arr[2], arr[3], arr[4]);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("\nInput error: " + line);
                //fixing negative number after exception
                for (i = 0; i < arr.length; i++) {
                    if (arr[i] < 0) {
                        arr[i] = 0;
                    }
                }
                System.out.printf("Correction : %s %s %d %d %d %d %d \n", buf[0], buf[1], arr[0], arr[1], arr[2], arr[3], arr[4]);

            } catch (RuntimeException e) {
                System.out.println("\nInput error: " + line);
                System.out.printf("Correction : %s %s %d %d %d %d %d \n", buf[0], buf[1], arr[0], arr[1], arr[2], arr[3], arr[4]);
            } catch (Exception e) {
                System.out.println("\nInput error: " + line);
                System.out.printf("Correction : %s %s %d %d %d %d %d \n", buf[0], buf[1], arr[0], arr[1], arr[2], arr[3], arr[4]);
            }

            customers.add(new Customer(buf[0], buf[1], arr[0], arr[1], arr[2], arr[3], arr[4]));
        }
        scan.close();

        
        /*------------------------------------  processing    --------------------------------*/
        int eachSale = 0;
        //add item units from customer to product"
        for (int a = 0; a < customers.size(); a++) {//10
            for (int b = 0; b < products.size(); b++) {//5
                eachSale = products.get(b).totalSaleInUnits() + customers.get(a).CustomerOrder[b];
                products.get(b).setTotalSaleUnit(eachSale);
            }
        }


        //send arraylist from product and postage to customer"
        System.out.print("\n-----Process Orders-----");

        for (Customer customer : customers) {
            customer.processCustomer(products);
            customer.processPostage(postages);
        }

        //sort customers and print
        Collections.sort(customers);
        System.out.println("\n\n-----Sort product by total bill-----");
        for (int j = 0; j < customers.size(); j++) {
            customers.get(j).printSortTotalBill();
        }

        //sort product and print
        Collections.sort(products);
        System.out.println("\n-----Sort product by total Sales in Cash-----");
        for (int j = 0; j < products.size(); j++) {
            products.get(j).printTotalSale();
        }


    }
}
    

 
