public class NewReleasePrice implements Price{
    @Override
    public int getPriceCode() {
        return Movie.NEW_RELEASE;
    }

    @Override
    public double getFee( int daysRented ) {
        return daysRented * 3;
    }

    @Override
    public int getBonus( int daysRented ) {
        return daysRented > 1 ? 2 : 1;
    }
}
