# Refactoring

 Teaching Refactoring with Martin Fowler

## The video store example

 Das Beispielprogramm ist einfach und inzwischen etwas nutzlos. Videotheken gibt es nicht mehr und
 Verwaltungsprogramme dafür sind also obsolet. Dennoch möchten wir dieses klassische Beispiel aus
 dem Buch *Refactoring*<sup id="fn_1">[1](#footnote_1)</sup>. von Martin Fowler, das bereits im Jahr 2000 erschienen ist hernehmen,
 um die Grundideen des Refactoring zu veranschaulichen.

 Das Programm soll Rechnungen für Kunden einer Videothek erstellen und ausdrucken bzw. auf dem Bildschirm 
 darstellen. Es ermittelt hierzu, welche Filme der Kunde wie lange ausgeliehen hat und berechnet daraus die 
 Leihgebühr. Dabei gibt es drei Arten von Filmen: Normale Filme (REGULAR), Kinderfilme (CHILDREN) und Neuerscheinungen. (NEW_RELEASE).
  
 Weiterhin zeigt das Programm Bonuspunkte (FrequentRenterPoints) an. Für jeden Film, den man ausleiht, erhält der 
 Kunde einen Bonuspunkt - leiht er Neuerscheinungen aus, erhält er sogar zwei.

 Die Elemente der Videothek werden durch drei Klassen repräsentiert.

  ![Klassendiagramm Ausgangspunkt](./VideoStore/doc/VideostoreClasses_000.png)

 **Movie** ist nur eine einfache Datenklasse.

 **Rental** repräsentiert eine Ausleihe eines Films durch einen Kunden.

 **Customer** repräsentiert einen Kunden der Videothek. Diese Klasse hat neben den Zugriffsmethoden auch eine Methode 
 `statement()`, die eine Rechnung auf dem Bildschirm ausgibt.

**Was halten Sie von dem Design dieses Programms?**

 Ich würde es nicht als besonders gelungen bezeichnen. Für ein einfaches Programm wie dieses machts das natürlich nichts. Es ist nichts gegen ein "Quick-and-Dirty-Programm" einzuwenden, solange es so einfach ist. Wäre der Code jedoch Teil eines komplexeren Systems, das regelmäßig erweitert werden muss, so hätte ich ein echtes Problem mit dem Programm.

 Obwohl ja das Programm irgendwie funktioniert ist der hässliche Code nicht nur ein ästhetisches Problem.
 Spätestens dann, wenn wir den Code ändern müssen, um z.B. eine andere Preisberechnung oder eine anderes
 Ausgabeformat für die Rechnung zu realisieren oder gar ein neues Bonusmodell oder weitere Produktkategorien
 hinzufügen sollen, macht es keinen echten Spaß mehr.

 Deshalb sollten wir zuerst ein sog. Refactoring durchführen. Hierbei ändern wir den Code um das Design des Programms zu verbessern, ohne die Funktionalität zu verändern.

 Martin Fowler gibt hier foglenden Merksatz:  

 > "When you have to add a feature to a program but the code is not structured in a convenient way, first refactor the program to make it easy to add the feature, then add the feature."

Wie geht nun das vonstatten: Refactoring?
Um hier erfolgreich braucht man vor allem eines: Tests, die sichersellen, dass wir wirklich keine unerwünschten Seiteneffekte in unseren Code einbauen. Und hier sieht man schon, Tests sind hier nicht vorhanden. Leider ist das oft so,
und fast genausso oft ist es fast unmöglich, sinnvolle Tests für die zu ändernde Funktionalität zu schreiben. Sehen Sie sich hier die Methode `statement()' an.

```java
public String statement() {
        double totalAmount = 0;
        int frequentRenterPoints = 0;

        String result = "Rental Record for " + getName() + "\n";
        for( Rental curRental : rentals ) {
            double thisAmount = 0;

            // determine amounts for each line
            switch( curRental.getMovie().getPriceCode() ) {
                case Movie.REGULAR:
                    thisAmount += 2;
                    if( curRental.getDaysRented() > 2 )
                        thisAmount += ( curRental.getDaysRented() - 2 ) * 1.5;
                    break;
                case Movie.NEW_RELEASE:
                    thisAmount += curRental.getDaysRented() * 3;
                    break;
                case Movie.CHILDRENS:
                    thisAmount += 1.5;
                    if( curRental.getDaysRented() > 3 )
                        thisAmount += ( curRental.getDaysRented() - 3 ) * 1.5;
                    break;

            }
            totalAmount += thisAmount;

            // add frequent renter points
            frequentRenterPoints++;
            // add bonus for a two day new release rental
            if( ( curRental.getMovie().getPriceCode() == Movie.NEW_RELEASE ) && curRental.getDaysRented() > 1 )
                frequentRenterPoints++;

            // show figures for this rental
            result += "\t" + curRental.getMovie().getTitle() + "\t days rented: " + curRental.getDaysRented() + "  = " +  String.valueOf( thisAmount ) + "\n";

        }
        // add footer lines
        result += "Amount owed is " + String.valueOf( totalAmount ) + "\n";
        result += "You earned " + String.valueOf( frequentRenterPoints ) + " frequent renter points";
        return result;
    }
```

Die Methode gibt einen String zurück. Also können wir hier ein paar Tests schreiben, die überprüfen, wie der zurückgegebene String momentan aussieht.

### Schritt 1: Tests für die Methode statement() hinzufügen

Was sollten wir alles Testen? Sehen sie sich dazu die statement-Methode an. Sie berechnet zum einen die Leihgebühr. Dabei ist die Höhe der Leihgebühr abhängig von:

* der Art des Mediums (regular, children's, new release)

* der Leihdauer

Weiterhin werden Bonuspunkte vergeben - auch hier gibt es einen Sonderfall. Beide Fälle müssen bei den Tests berücksichtigt werden.

Die Tests sollten also sinnvolle und interessante Testfälle dieser Kombinationen enthalten.

**Aufgabe:** Bestimmen Sie welche Kombinationen sinnvoll sind. Welche Tests planen Sie?

 ### Schritt 2: Zerlegen und Umverteilen der Methode statement
 
 Nachdem wir die Tests haben, können wir damit beginnen, den Code zu verbessern. Jetzt haben wir uns durch die Testfälle ein Sicherheitsnetz geschaffen und bei Änderungen können wir immer sofort sehen, ob was schief geht.
 
 Wie könnten wir die Methode zerlegen?
 
 Denken Sie hier an die Verantwortlichkeiten der Methode - was macht `statement()` eigentlich?
 
 **Aufgabe:** Welche Verantwortlichkeiten hat statement()?
 
 **Aufgabe:** Welche Zerlegung der Methode `statement()` schlagen Sie vor?
 
Die Methode statement hat drei Verantwortlichkeiten, die wir trennen sollten.

1. Sie berechnet die Leihgebühren 
    a) für jeden Leihevorgang
    b) insgesamt, die Gesamtgebühr

2. Sie berechnet die Bonuspunkte
    a) für jeden Leihvorgang
    b) insgesamt

3. Sie erzeugt den Ausgabetext für die Rechnung. 

#### Berechnung der Leihgebühr für eine Rental in Methode extrahieren

Es handelt sich hier um die Zeilen 7 bis 25. Wir können die Berechnung als eigene Methode herausziehen.

```java
 1  public String statement() {
 2          double totalAmount = 0;
 3          int frequentRenterPoints = 0;
 4
 5          String result = "Rental Record for " + getName() + "\n";
 6          for( Rental curRental : rentals ) {
 7              double thisAmount = 0;
 8
 9              // determine amounts for each line
10              switch( curRental.getMovie().getPriceCode() ) {
11                  case Movie.REGULAR:
12                      thisAmount += 2;
13                      if( curRental.getDaysRented() > 2 )
14                          thisAmount += ( curRental.getDaysRented() - 2 ) * 1.5;
15                      break;
16                  case Movie.NEW_RELEASE:
17                      thisAmount += curRental.getDaysRented() * 3;
18                      break;
19                  case Movie.CHILDRENS:
20                      thisAmount += 1.5;
21                      if( curRental.getDaysRented() > 3 )
22                          thisAmount += ( curRental.getDaysRented() - 3 ) * 1.5;
23                    break;
24
25              }
26              totalAmount += thisAmount;
27
28              // add frequent renter points
29              frequentRenterPoints++;
30              // add bonus for a two day new release rental
31              if( ( curRental.getMovie().getPriceCode() == Movie.NEW_RELEASE ) && curRental.getDaysRented() > 1 )
32                  frequentRenterPoints++;
33
34              // show figures for this rental
35              result += "\t" + curRental.getMovie().getTitle() + "\t days rented: " + curRental.getDaysRented() + "  =   " +  String.valueOf( thisAmount ) + "\n";
36
37          }
38          // add footer lines
39          result += "Amount owed is " + String.valueOf( totalAmount ) + "\n";
40          result += "You earned " + String.valueOf( frequentRenterPoints ) + " frequent renter points";
41          return result;
42      }
```

Die Signatur der neuen Methode könnte so aussehen:

`double calculateAmountFor( Rental curRental );`

und statement() jetzt so:

```java
 1     public String statement() {
 2         double totalAmount = 0;
 3         int frequentRenterPoints = 0;
 4 
 5         String result = "Rental Record for " + getName() + "\n";
 6         for( Rental curRental : rentals ) {
 7             double thisAmount = calculateAmountFor( curRental );
 8             totalAmount += thisAmount;
 9 
10             // add frequent renter points
11             frequentRenterPoints++;
12             // add bonus for a two day new release rental
13             if( ( curRental.getMovie().getPriceCode() == Movie.NEW_RELEASE ) && curRental.getDaysRented() > 1 )
14                frequentRenterPoints++;

15            // show figures for this rental
16            result += "\t" + curRental.getMovie().getTitle() + "\tdays rented: " + curRental.getDaysRented() + "  = " +  String.valueOf( thisAmount ) + "\n";
17
18         }
19         // add footer lines
20         result += "Amount owed is " + String.valueOf( totalAmount ) + "\n";
21         result += "You earned " + String.valueOf( frequentRenterPoints ) + " frequent renter points";
22         return result;
23     }
```

Das Gleiche können wir für die Berechnung der Bonuspunkte (Zeilen 10 - 14) durchführen.
Wir extrahieren `int calculateFrequentRenterPointsFor( Rental curRental )`

und erhalten nun:
  ```java
      public String statement() {
        double totalAmount = 0;
        int frequentRenterPoints = 0;

        String result = "Rental Record for " + getName() + "\n";
        for( Rental curRental : rentals ) {
            
            double thisAmount = calculateAmountFor( curRental );
            totalAmount += thisAmount;
            
            // add frequent renter points            
            frequentRenterPoints += calculateFrequentRenterPointsFor( curRental );

            // show figures for this rental
            result += "\t" + curRental.getMovie().getTitle() + "\tdays rented: " + curRental.getDaysRented() + "  = " +  String.valueOf( thisAmount ) + "\n";
        }
        // add footer lines
        result += "Amount owed is " + String.valueOf( totalAmount ) + "\n";
        result += "You earned " + String.valueOf( frequentRenterPoints ) + " frequent renter points";
        return result;
    }
```

und die extrahierte Methode:
```java
    double calculateAmountFor( Rental curRental ) {
        double thisAmount = 0;

        // determine amounts for each line
        switch( curRental.getMovie().getPriceCode() ) {
            case Movie.REGULAR:
                thisAmount += 2;
                if( curRental.getDaysRented() > 2 )
                    thisAmount += ( curRental.getDaysRented() - 2 ) * 1.5;
                break;
            case Movie.NEW_RELEASE:
                thisAmount += curRental.getDaysRented() * 3;
                break;
            case Movie.CHILDRENS:
                thisAmount += 1.5;
                if( curRental.getDaysRented() > 3 )
                    thisAmount += ( curRental.getDaysRented() - 3 ) * 1.5;
                break;

        }
        return thisAmount;
    }
```

Natürlich führen wir nach jedem Schritt die Tests erneut aus.

Die extrahierte Methode `calculateFrequentRenterPointsFor` könnte so aussehen:
  
```java
    int calculateFrequentRenterPointsFor( Rental curRental ) {
        int frequentRenterPoints = 0;
        frequentRenterPoints++;

        // add bonus for a two day new release rental
        if( ( curRental.getMovie().getPriceCode() == Movie.NEW_RELEASE ) && curRental.getDaysRented() > 1 )
            frequentRenterPoints++;
        return frequentRenterPoints;
    }
```

das sollten wir vereinfachen:

```java
    int calculateFrequentRenterPointsFor( Rental curRental ) {
        if( ( curRental.getMovie().getPriceCode() == Movie.NEW_RELEASE ) && curRental.getDaysRented() > 1 ) {
            // 2 bonus points for a two day new release rental
             return 2;
        }
        else {
            // 1 bonus point for others
            return 1;
        }
    }
```

Vielleicht fällt Ihnen auf, dass in den beiden extrahierten Methoden überhaupt keine Daten von Customer benutzt werden, 
sondern nur auf Rental zugegriffen wird. Das deutet darauf hin, dass die Methoden in der falschen Klasse liegen.
Beide Methoden gehören in die Klasse Rental.

Wir ziehen die Methoden um und benennen sie neu, die Klasse Rental sieht nun so aus:

```java
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

    public double calculateFee() {
        double thisAmount = 0;

        // determine amounts for each line
        switch( this.getMovie().getPriceCode() ) {
            case Movie.REGULAR:
                thisAmount += 2;
                if( this.getDaysRented() > 2 )
                    thisAmount += ( this.getDaysRented() - 2 ) * 1.5;
                break;
            case Movie.NEW_RELEASE:
                thisAmount += this.getDaysRented() * 3;
                break;
            case Movie.CHILDRENS:
                thisAmount += 1.5;
                if( this.getDaysRented() > 3 )
                    thisAmount += ( this.getDaysRented() - 3 ) * 1.5;
                break;

        }
        return thisAmount;
    }

    public int calculateBonus() {
        if( ( this.getMovie().getPriceCode() == Movie.NEW_RELEASE ) && this.getDaysRented() > 1 ) {
            // 2 bonus points for a two day new release rental
            return 2;
        }
        else {
            // 1 bonus point for others
            return 1;
        }
    }
}
```

Die Methode statement() hat nun folgendes Aussehen:

```java
    public String statement() {
        double totalAmount = 0;
        int frequentRenterPoints = 0;

        String result = "Rental Record for " + getName() + "\n";
        for( Rental curRental : rentals ) {

            double thisAmount = curRental.calculateFee();
            totalAmount += thisAmount;

            // add frequent renter points
            frequentRenterPoints += curRental.calculateBonus();

            // show figures for this rental
            result += "\t" + curRental.getMovie().getTitle() + "\tdays rented: " + curRental.getDaysRented() + "  = " +  String.valueOf( thisAmount ) + "\n";
        }
        // add footer lines
        result += "Amount owed is " + String.valueOf( totalAmount ) + "\n";
        result += "You earned " + String.valueOf( frequentRenterPoints ) + " frequent renter points";
        return result;
    }
```

Wir lagern die Berechnung der Summen für die Leihgebühr und die Bonuspunkte in zwei Methoden aus:

```java    
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
```

 und nutzen die beiden neuen privaten Methoden in `statement()`,
 wobei wir jetzt die Bibliotheks-Klasse `StringBuilder` verwenden, um den Text zusammenzubauen:

```java
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
```

Normalerweise macht man das natürlich in zwei Schritten. Dabei die Ausführung der Tests nie vergessen!

Jetzt ist sehr viel einfacher, eine Methode html-Statement zu schreiben!
Das können Sie ja selbst gerne versuchen.

Ein Ärgernis ist die schlechte Erweiterbarkeit der Berechnung der Leihgebühr. Grund dafür ist die Verwendung von
switch für die Abfrage der unterschiedlichen Filmtypen. 

```java
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
```

Wenn wir den Code betrachten fällt auf, dass aufgrund der Werte eines anderen Objektes verzweigen. 
Da ist meist keine gute Idee - wenn wir in unseren Methoden verzweigen müssen, 
sollten wir das nur auf Basis der eigenen Daten tun, nicht auf Basis fremder.
Hieraus folgt, dass getCharge in die Klasse Movie gehört!

Verschieben wir also die Methode:

Nach der Durchführung der Änderungen vereinfacht sich `Rental.calulateFee` zu: 
```java
    public double calculateFee() {
        return getMovie().calculateFee( getDaysRented() );
    }
```

In Movie gibt es eine gleichnamige Methode, mit Parameter daysRented:

```java
    public double calculateFee( int daysRented ) {
        double thisAmount = 0;

        // determine amounts for each line
        switch( getPriceCode() ) {
            case Movie.REGULAR:
                thisAmount += 2;
                if(daysRented > 2 )
                    thisAmount += ( daysRented - 2 ) * 1.5;
                break;
            case Movie.NEW_RELEASE:
                thisAmount += daysRented * 3;
                break;
            case Movie.CHILDRENS:
                thisAmount += 1.5;
                if( daysRented > 3 )
                    thisAmount += ( daysRented - 3 ) * 1.5;
                break;

        }
        return thisAmount;
    }
```

Das Gleiche mache ich mit der Berechnung der Bonuspunkte, da auch hier aufgrund des Filmtyps verzweigt wird.

In Klasse Rental:
```java
    public  int calculateBonus() {
        return getMovie().calculateBonus( getDaysRented() );
    }
```

In Klasse Movie:
```java
    public  int calculateBonus( int daysRented ) {
        if( ( getPriceCode() == Movie.NEW_RELEASE ) && daysRented > 1 )
            // extra bonus point for new released movies
            return 2;
        else
            return 1;
    }
```

Wir haben hier verschiedene Arten von Filmen, die gleiche Fragen verschieden beantworten.
Das hört sich nach einer Aufgabe für Unterklassen an. Wir können drei Unterklassen von Movie
bilden, von denen jede ihre eigene Version von `calculateFee() und calculateBonus() haben kann.

![Klassendiagramm Polymorphy](./VideoStore/doc/Simple%20Polymorphy.png)

Dies ermöglicht es mir, den switch-Befehl durch Polymorphismus zu ersetzen. Leider hat dies einen kleinen Fehler. 
Es funktioniert nicht, da ein Film seine Klassifizierung im "Laufe seines Lebens" ändern kann. Ein Objekt kann
jedoch nicht einfach seine Klasse ändern. Hierfür gibt es jedoch eine Lösung, nämlich das State-Pattern.

![Klassendiagramm StatePattern](./VideoStore/doc/src_after_refactoring_State_Pattern.png)
 
 Dies ist eines jener Entwurfsmuster, die bereits 1995 von der sog. "Gang of Four" in *Designpatterns*<sup id="fn_2">[2](#footnote_2)</sup> veröffentlicht wurde. 
 
 Design Patterns sind eine Sammlung bewährter Lösungsideen für bekannter Probleme in der Software-Entwicklung. 

 
 Wir führen für die Verbesserung 3 Schritte durch
 1. Wir führen ein Interface Price ein, die nur die Methode getPriceCode enthält.
    Weiterhin führen wir drei Unterklassen ein (RegularPrice, ChildrensPrice, NewReleasePrice), die alle das Interface Price implementieren und einen PriceCode zurückgeben.
    
 2. Wir passen Movie so an, dass es jetzt Price nutzt, statt PriceCode.
 
 3. Wir verschieben die Berechnungen der Leihgebühr und der Boni in die Unterklassen und passen das Interface an.   
 
 **Wir starten, damit Price und Unterklassen einzuführen:**
 
 ```java
public interface Price {
    int getPriceCode();
}

public class RegularPrice implements Price {
    @Override
    public int getPriceCode() {
        return Movie.REGULAR;
    }
}

public class ChildrensPrice implements Price {
    @Override
    public int getPriceCode() {
        return Movie.CHILDRENS;
    }
}

public class NewReleasePrice implements Price{
    @Override
    public int getPriceCode() {
        return Movie.NEW_RELEASE;
    }
}
```

**Jetzt passen wir Movie an:**

```java
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
}
```

**Schließlich verschieben wir die Berechnung in die Unterklassen:**

```java
public interface Price {
    int getPriceCode();
    double getFee( int daysRented );
    int getBonus( int daysRented );
}

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
...
```
 
 Movie sieht jetzt so aus:

 ```java
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
```
 
 Es ist jetzt viel einfache, die Klassenstruktur von Filmen und die Regeln für die Abrechnung und die Bonuspunkte zu verändern.
 Auch das Einführen neuer Preisklassen ist jetzt sehr einfach.
 
 Das ist ein sehr einfaches Beispiel, aber es gibt Ihnen bereits ein Gefühl dafür, was Refaktorisieren ist.
 Die wichtigste, was Sie vielleicht mitnehmen konnten, ist der Rhythmus des Refaktorisierens:
 Testen -> kleine Änderungen -> Testen -> kleine Änderungen -> Testen.
 
 Dieser Rhythmus ermöglicht es uns, schnelle und vor allem sichere Fortschritte beim Verbessern der Codebasis.  
 
 ---
 <b id="footnote_1">(1)</b> Fowler, Martin: Refactoring, Improving the Design of Existing Code. 1999 [↩](#fn_1)
 <b id="footnote_2">(2)</b> Gamma, Helm, Johnson, Vlissides: Designpatterns. 1995 [↩](#fn_2)