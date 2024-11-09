package iterator;

import businesslogic.BLFacadeImplementation;

public class IteratorMain {
    public static void main(String[] args) {
    	BLFacadeImplementation facade = new BLFacadeImplementation();
        ExtendedIterator<String> iterator = facade.getDepartingCitiesIterator();
        
        System.out.println("DBtikan zuzenean hiriak hartu");
        System.out.println(facade.getDepartCities());

        System.out.println("_____________________");
        System.out.println("FROM LAST TO FIRST");
        iterator.goLast();
        while (iterator.hasPrevious()) {
            String city = iterator.previous();
            System.out.println(city);
        }

        System.out.println();
        System.out.println("_____________________");
        System.out.println("FROM FIRST TO LAST");
        iterator.goFirst();
        while (iterator.hasNext()) {
            String city = iterator.next();
            System.out.println(city);
        }
    }
}
