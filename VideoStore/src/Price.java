public interface Price {
    int getPriceCode();
    double getFee( int daysRented );
    int getBonus( int daysRented );
}
