import java.util.ArrayList;

public class Customer {

    private final String name;
    private final ArrayList<Rental> rentals = new ArrayList<>();

    public Customer( String name ) {
        this.name = name;
    }

    public void addRental( Rental arg ) {
        rentals.add( arg );
    }

    public String getName() {
        return name;
    }

    public String statement() {
        double totalAmount = 0;
        int frequentRenterPoints = 0;

        String result = "Rental Record for " + getName() + "\n";
        for( Rental curRental : rentals ) {
            double thisAmount = 0;

            // determine amounts for each line
            switch( curRental.getMovie().getPriceCode() ) {
                case Movie.REGULAR:
                    thisAmount += 2;
                    if( curRental.getDaysRented() > 2 )
                        thisAmount += ( curRental.getDaysRented() - 2 ) * 1.5;
                    break;
                case Movie.NEW_RELEASE:
                    thisAmount += curRental.getDaysRented() * 3;
                    break;
                case Movie.CHILDRENS:
                    thisAmount += 1.5;
                    if( curRental.getDaysRented() > 3 )
                        thisAmount += ( curRental.getDaysRented() - 3 ) * 1.5;
                    break;

            }
            totalAmount += thisAmount;

            // add frequent renter points
            frequentRenterPoints++;
            // add bonus for a two day new release rental
            if( ( curRental.getMovie().getPriceCode() == Movie.NEW_RELEASE ) && curRental.getDaysRented() > 1 )
                frequentRenterPoints++;

            // show figures for this rental
            result += "\t" + curRental.getMovie().getTitle() + "\tdays rented: " + curRental.getDaysRented() + "  = " +  String.valueOf( thisAmount ) + "\n";

        }
        // add footer lines
        result += "Amount owed is " + String.valueOf( totalAmount ) + "\n";
        result += "You earned " + String.valueOf( frequentRenterPoints ) + " frequent renter points";
        return result;
    }
}
