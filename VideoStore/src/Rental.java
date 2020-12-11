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
        return getMovie().calculateBonus( getDaysRented() );
    }

    public double calculateFee() {
        return getMovie().calculateFee( getDaysRented() );
    }
}
