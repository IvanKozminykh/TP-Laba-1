import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Publisher {
    private String name;
    private String city;
    private String country;

    public void setName(String name) { this.name = name; }
    public void setCity(String city) { this.city = city; }
    public void setCountry(String country) { this.country = country; }

    public String getName() { return name; }
    public String getCity() { return city; }
    public String getCountry() { return country; }

    @Override
    public String toString() {
        return "Publisher{" +
                "name='" + name + '\'' + "\n" +
                ", city='" + city + '\'' + "\n" +
                ", country='" + country + '\'' + "\n" +
                '}';
    }
}

class Price {
    private String currency;
    private String amount;

    public void setCurrency(String currency) { this.currency = currency; }
    public void setAmount(String amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public String getAmount() { return amount; }

    @Override
    public String toString() {
        return "Price{" +
                "currency='" + currency + '\'' +
                ", amount='" + amount + '\'' +
                '}';
    }

    public static Price extractPrice(String content) {
        String startTag = "<price";
        String endTag = "</price>";
        int startIndex = content.indexOf(startTag);
        if (startIndex == -1) return null;

        Price price = new Price();
        int currencyStartIndex = content.indexOf("currency=\"", startIndex) + 10;
        int currencyEndIndex = content.indexOf("\"", currencyStartIndex);
        if (currencyStartIndex != -1 && currencyEndIndex != -1) {
            price.setCurrency(content.substring(currencyStartIndex, currencyEndIndex).trim());
        }

        int valueStartIndex = content.indexOf(">", startIndex) + 1;
        int valueEndIndex = content.indexOf(endTag, valueStartIndex);
        if (valueEndIndex != -1) {
            price.setAmount(content.substring(valueStartIndex, valueEndIndex).trim());
        }

        return price;
    }
}

class Book {
    private String title;
    private String author;
    private int year;
    private String genre;
    private Price price;
    private String isbn;
    private Publisher publisher;
    private String language;
    private List<String> awards = new ArrayList<>();

    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setYear(int year) { this.year = year; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setPrice(Price price) { this.price = price; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setPublisher(Publisher publisher) { this.publisher = publisher; }
    public void setLanguage(String language) { this.language = language; }
    public void setAwards(List<String> awards) { this.awards = awards; }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    public String getGenre() { return genre; }
    public Price getPrice() { return price; }
    public String getIsbn() { return isbn; }
    public Publisher getPublisher() { return publisher; }
    public String getLanguage() { return language; }
    public List<String> getAwards() { return awards; }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' + "\n" +
                ", author='" + author + '\'' + "\n" +
                ", year=" + year + "\n" +
                ", genre='" + genre + '\'' + "\n" +
                ", price=" + price + "\n" +
                ", isbn='" + isbn + '\'' + "\n" +
                ", publisher=" + publisher + "\n" +
                ", language='" + language + '\'' + "\n" +
                ", awards=" + awards + "\n" +
                '}';
    }
}

public class Programm {
    public static void main(String[] args) {
        String filePath = "D:/random_structure_14.xml"; // Укажите правильный путь к файлу

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line.trim());
            }

            String xmlContent = sb.toString();

            parseBooks(xmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void parseBooks(String xmlContent) {
        String bookTagStart = "<book ";
        String bookTagEnd = "</book>";

        int startIndex = 0;

        while ((startIndex = xmlContent.indexOf(bookTagStart, startIndex)) != -1) {
            int endIndex = xmlContent.indexOf(bookTagEnd, startIndex);
            if (endIndex == -1) break;
            endIndex += bookTagEnd.length();
            String bookContent = xmlContent.substring(startIndex, endIndex);
            Book currentBook = new Book();

            currentBook.setTitle(extractContent(bookContent, "title"));
            currentBook.setAuthor(extractContent(bookContent, "author"));
            currentBook.setYear(Integer.parseInt(extractContent(bookContent, "year")));
            currentBook.setGenre(extractContent(bookContent, "genre"));
            currentBook.setPrice(Price.extractPrice(bookContent));
            currentBook.setIsbn(extractContent(bookContent, "isbn"));
            currentBook.setPublisher(extractPublisher(bookContent));
            currentBook.setLanguage(extractContent(bookContent, "language"));
            currentBook.setAwards(extractAwards(bookContent));

            System.out.println(currentBook);

            startIndex = endIndex;
        }
    }

    private static String extractContent(String content, String tag) {
        String startTag = "<" + tag + ">";
        String endTag = "</" + tag + ">";
        int startIndex = content.indexOf(startTag);
        if (startIndex == -1) return "";
        startIndex += startTag.length();
        int endIndex = content.indexOf(endTag, startIndex);
        if (endIndex == -1) return "";
        return content.substring(startIndex, endIndex).trim();
    }

    private static Publisher extractPublisher(String content) {
        Publisher publisher = new Publisher();
        String startTag = "<publisher>";
        String endTag = "</publisher>";
        int startIndex = content.indexOf(startTag);
        if (startIndex == -1) return publisher;
        int endIndex = content.indexOf(endTag, startIndex);
        if (endIndex == -1) return publisher;

        String publisherContent = content.substring(startIndex, endIndex);

        publisher.setName(extractContent(publisherContent, "name"));
        publisher.setCity(extractContent(publisherContent, "city"));
        publisher.setCountry(extractContent(publisherContent, "country"));

        return publisher;
    }

    private static List<String> extractAwards(String content) {
        List<String> awards = new ArrayList<>();
        String startTag = "<award>";
        String endTag = "</award>";
        int startIndex = 0;

        while ((startIndex = content.indexOf(startTag, startIndex)) != -1) {
            int endIndex = content.indexOf(endTag, startIndex);
            if (endIndex == -1) break;
            // Извлекаем содержимое между <award> и </award>
            String awardContent = content.substring(startIndex + startTag.length(), endIndex).trim();
            awards.add(awardContent); // Добавляем извлечённое содержимое в список
            startIndex = endIndex + endTag.length(); // Переходим к следующему тегу <award>
        }

        return awards;
    }

}
