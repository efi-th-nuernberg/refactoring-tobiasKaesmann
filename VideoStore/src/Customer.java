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
        // add header line
        StringBuilder result = new StringBuilder();
        result.append( "Rental Record for " ).append( getName() ).append( "\n" );

        // add line for each rental
        for( Rental curRental : rentals ) {
            result.append( "\t" ).append( curRental.getMovie().getTitle() ).append( "\tdays rented: " ).append( curRental.getDaysRented() ).append( "  = " ).append( String.valueOf( curRental.calculateFee() ) ).append( "\n" );
        }

        // add footer lines
        result.append( "Amount owed is " ).append( String.valueOf( getTotalAmount() ) ).append( "\n" );
        result.append( "You earned " ).append( String.valueOf( getFrequentRenterPoints() ) ).append( " frequent renter points" );
        return result.toString();
    }

    public String htmlStatement() {
        // add header line
        StringBuilder result = new StringBuilder();
        result.append( "<H2>Rentals for <EM>" ).append( getName() ).append( "</EM></H2><P>\n" );

        // add line for each rental
        for( var curRental : rentals ) {
            // show figures for this rental
            result.append( curRental.getMovie().getTitle() ).append(  ": " ).append( String.valueOf( curRental.calculateFee() ) ).append( "<BR>\n" );
        }

        //add footer lines
        result.append( "<P>Amount owed is <EM>" ).append( String.valueOf( getTotalAmount() ) ) .append( "</EM><P>\n" );
        result.append( "On this rental you earned <EM>" ).append( String.valueOf( getFrequentRenterPoints() ) ).append( "</EM> frequent renter points<P>" );
        return result.toString();
    }

    private double getTotalAmount() {
        double totalAmount = 0.0;
        for( Rental curRental : rentals ) {
            totalAmount += curRental.calculateFee();
        }
        return totalAmount;
    }

    private int getFrequentRenterPoints() {
        int frequentRenterPoints = 0;
        for( Rental curRental : rentals ) {
            frequentRenterPoints += curRental.calculateBonus();
        }
        return frequentRenterPoints;
    }
}
