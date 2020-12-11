public class Movie {

    public static final int CHILDRENS = 2;
    public static final int REGULAR = 0;
    public static final int NEW_RELEASE = 1;

    private final String title;
    private Price price;

    public Movie( String title, int priceCode ) {
        this.title = title;
        setPriceCode( priceCode );
    }

    public String getTitle() {
        return title;
    }

    public int getPriceCode() {
        return price.getPriceCode();
    }

    public void setPriceCode( int priceCode ) {
        switch( priceCode ) {
            case Movie.REGULAR:
                this.price = new RegularPrice();
                break;
            case Movie.CHILDRENS:
                this.price = new ChildrensPrice();
                break;
            case Movie.NEW_RELEASE:
                this.price = new NewReleasePrice();
                break;
            default:
                throw new IllegalArgumentException( "Incorrect Price Code" );
        }
    }

    public double calculateFee( int daysRented ) {
        return price.getFee( daysRented );
    }

    public  int calculateBonus( int daysRented ) {
        return price.getBonus( daysRented );
    }
}