public class RegularPrice implements Price {
    @Override
    public int getPriceCode() {
        return Movie.REGULAR;
    }

    @Override
    public double getFee( int daysRented ) {
        double thisAmount = 2;
        if(daysRented > 2 )
            thisAmount += ( daysRented - 2 ) * 1.5;
        return thisAmount;
    }

    @Override
    public int getBonus( int daysRented ) {
        return 1;
    }
}
