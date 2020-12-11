import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    Customer customer = new Customer( "customer A" );

    @BeforeEach
    void setup_tests() {
        customer = new Customer( "customer A" );
    }

    @Test
    void test_statement_no_rentals() {
        String expected_text = "Rental Record for customer A\n" +
                               "Amount owed is 0.0\n" +
                               "You earned 0 frequent renter points";

        String invoice_text = customer.statement();
        assertEquals( expected_text, invoice_text );

        System.out.println( "test_statement_no_rentals" );
        System.out.println( invoice_text );
    }

    @Test
    void test_statement_one_regular_two_days() {
        String expected_text = "Rental Record for customer A\n" +
                               "\tregular_movie\tdays rented: 2  = 2.0\n" +
                               "Amount owed is 2.0\n" +
                               "You earned 1 frequent renter points";

        Movie regular_movie = new Movie( "regular_movie", Movie.REGULAR );
        Rental rental = new Rental( regular_movie, 2 );
        customer.addRental( rental );

        String invoice_text = customer.statement();
        assertEquals( expected_text, invoice_text );

        System.out.println( "test_statement_one_regular_two_days" );
        System.out.println( invoice_text );
    }

    @Test
    void test_statement_one_regular_three_days() {
        String expected_text = "Rental Record for customer A\n" +
                "\tregular_movie\tdays rented: 3  = 3.5\n" +
                "Amount owed is 3.5\n" +
                "You earned 1 frequent renter points";

        Movie regular_movie = new Movie( "regular_movie", Movie.REGULAR );
        Rental rental = new Rental( regular_movie, 3 );
        customer.addRental( rental );

        String invoice_text = customer.statement();
        assertEquals( expected_text, invoice_text );

        System.out.println( "test_statement_one_regular_three_days" );
        System.out.println( invoice_text );
    }

    @Test
    void test_statement_two_regular_one_and_three_days() {
        String expected_text = "Rental Record for customer A\n" +
                "\tregular_movie_1\tdays rented: 1  = 2.0\n" +
                "\tregular_movie_2\tdays rented: 3  = 3.5\n" +
                "Amount owed is 5.5\n" +
                "You earned 2 frequent renter points";

        Movie regular_movie_1 = new Movie( "regular_movie_1", Movie.REGULAR );
        Rental rental_1 = new Rental( regular_movie_1, 1 );
        customer.addRental( rental_1 );

        Movie regular_movie_2 = new Movie( "regular_movie_2", Movie.REGULAR );
        Rental rental_2 = new Rental( regular_movie_2, 3 );
        customer.addRental( rental_2 );

        String invoice_text = customer.statement();
        assertEquals( expected_text, invoice_text );

        System.out.println( "test_statement_two_regular_one_and_three_days" );
        System.out.println( invoice_text );
    }

    @Test
    void test_statement_one_children_three_days() {
        String expected_text = "Rental Record for customer A\n" +
                "\tchildren_movie\tdays rented: 3  = 1.5\n" +
                "Amount owed is 1.5\n" +
                "You earned 1 frequent renter points";

        Movie regular_movie = new Movie( "children_movie", Movie.CHILDRENS );
        Rental rental = new Rental( regular_movie, 3 );
        customer.addRental( rental );

        String invoice_text = customer.statement();
        assertEquals( expected_text, invoice_text );

        System.out.println( "test_statement_one_children_three_days" );
        System.out.println( invoice_text );
    }

    @Test
    void test_statement_one_children_four_days() {
        String expected_text = "Rental Record for customer A\n" +
                "\tchildren_movie\tdays rented: 4  = 3.0\n" +
                "Amount owed is 3.0\n" +
                "You earned 1 frequent renter points";

        Movie regular_movie = new Movie( "children_movie", Movie.CHILDRENS );
        Rental rental = new Rental( regular_movie, 4 );
        customer.addRental( rental );

        String invoice_text = customer.statement();
        assertEquals( expected_text, invoice_text );

        System.out.println( "test_statement_one_children_four_days" );
        System.out.println( invoice_text );
    }

    @Test
    void test_statement_one_new_release_one_day() {
        String expected_text = "Rental Record for customer A\n" +
                "\tnew_release_movie\tdays rented: 1  = 3.0\n" +
                "Amount owed is 3.0\n" +
                "You earned 1 frequent renter points";

        Movie regular_movie = new Movie( "new_release_movie", Movie.NEW_RELEASE );
        Rental rental = new Rental( regular_movie, 1 );
        customer.addRental( rental );

        String invoice_text = customer.statement();
        assertEquals( expected_text, invoice_text );

        System.out.println( "test_statement_one_new_release_one_day" );
        System.out.println( invoice_text );
    }

    @Test
    void test_statement_one_new_release_three_days() {
        String expected_text = "Rental Record for customer A\n" +
                "\tnew_release_movie\tdays rented: 3  = 9.0\n" +
                "Amount owed is 9.0\n" +
                "You earned 2 frequent renter points";

        Movie regular_movie = new Movie( "new_release_movie", Movie.NEW_RELEASE );
        Rental rental = new Rental( regular_movie, 3 );
        customer.addRental( rental );

        String invoice_text = customer.statement();
        assertEquals( expected_text, invoice_text );

        System.out.println( "test_statement_one_new_release_three_days" );
        System.out.println( invoice_text );
    }

    @Test
    void test_statement_with_all_kinds_of_movies_one_and_four_days() {
        String expected_text = "Rental Record for customer A\n" +
                "\tregular_movie_1\tdays rented: 1  = 2.0\n" +
                "\tregular_movie_2\tdays rented: 4  = 5.0\n" +
                "\tchildren_movie_1\tdays rented: 1  = 1.5\n" +
                "\tchildren_movie_2\tdays rented: 4  = 3.0\n" +
                "\tnew_release_movie_1\tdays rented: 1  = 3.0\n" +
                "\tnew_release_movie_2\tdays rented: 4  = 12.0\n" +
                "Amount owed is 26.5\n" +
                "You earned 7 frequent renter points";

        {
            Movie regular_movie_1 = new Movie( "regular_movie_1", Movie.REGULAR );
            Rental rental = new Rental( regular_movie_1, 1 );
            customer.addRental( rental );
        }
        {
            Movie regular_movie_4 = new Movie( "regular_movie_2", Movie.REGULAR );
            Rental rental = new Rental( regular_movie_4, 4 );
            customer.addRental( rental );
        }
        {
            Movie children_movie_1 = new Movie( "children_movie_1", Movie.CHILDRENS );
            Rental rental = new Rental( children_movie_1, 1 );
            customer.addRental( rental );
        }
        {
            Movie children_movie_4 = new Movie( "children_movie_2", Movie.CHILDRENS );
            Rental rental = new Rental( children_movie_4, 4 );
            customer.addRental( rental );
        }
        {
            Movie new_release_movie_1 = new Movie( "new_release_movie_1", Movie.NEW_RELEASE );
            Rental rental = new Rental( new_release_movie_1, 1 );
            customer.addRental( rental );
        }
        {
            Movie new_release_movie_4 = new Movie( "new_release_movie_2", Movie.NEW_RELEASE );
            Rental rental = new Rental( new_release_movie_4, 4 );
            customer.addRental( rental );
        }

        String invoice_text = customer.statement();
        assertEquals( expected_text, invoice_text );

        System.out.println( "test_statement_with_all_kinds_of_movies_one_and_four_days" );
        System.out.println( invoice_text );
    }

}
