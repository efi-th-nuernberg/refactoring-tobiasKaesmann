public class ChildrensPrice implements Price {
    @Override
    public int getPriceCode() {
        return Movie.CHILDRENS;
    }

    @Override
    public double getFee( int daysRented ) {
        double thisAmount = 1.5;
        if( daysRented > 3 )
            thisAmount += ( daysRented - 3 ) * 1.5;

        return thisAmount;
    }

    @Override
    public int getBonus( int daysRented  ) {
        return 1;
    }
}
