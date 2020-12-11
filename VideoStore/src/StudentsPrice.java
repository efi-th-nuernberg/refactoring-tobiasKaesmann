public class StudentsPrice implements Price {
    @Override
    public int getPriceCode() {
        return Movie.STUDENTS;
    }

    @Override
    public double getFee( int daysRented ) {
        if( daysRented < 3 )
            return 1;
        else
            return daysRented;
    }

    @Override
    public int getBonus( int daysRented ) {
        return 1;
    }
}
