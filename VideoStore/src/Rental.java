public class Rental {

    private final Movie movie;
    private final int daysRented;

    public Rental( Movie movie, int daysRented ) {
        this.movie = movie;
        this.daysRented = daysRented;
    }

    public Movie getMovie() {
        return movie;
    }

    public int getDaysRented() {
        return daysRented;
    }

    public  int calculateBonus() {
        if( ( getMovie().getPriceCode() == Movie.NEW_RELEASE ) && getDaysRented() > 1 )
            // extra bonus point for new released movies
            return 2;
        else
            return 1;
    }

    public double calculateFee() {
        double thisAmount = 0;

        // determine amounts for each line
        switch( getMovie().getPriceCode() ) {
            case Movie.REGULAR:
                thisAmount += 2;
                if(getDaysRented() > 2 )
                    thisAmount += ( getDaysRented() - 2 ) * 1.5;
                break;
            case Movie.NEW_RELEASE:
                thisAmount += getDaysRented() * 3;
                break;
            case Movie.CHILDRENS:
                thisAmount += 1.5;
                if( getDaysRented() > 3 )
                    thisAmount += ( getDaysRented() - 3 ) * 1.5;
                break;

        }
        return thisAmount;
    }
}
