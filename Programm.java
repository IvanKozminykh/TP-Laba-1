import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

class Publisher {
    private String name;
    private String city;
    private String country;

    // Сеттеры
    public void setName(String name) { this.name = name; }
    public void setCity(String city) { this.city = city; }
    public void setCountry(String country) { this.country = country; }

    @Override
    public String toString() {
        return "Publisher{" +
                "name='" + name + '\'' + "\n" +
                ", city='" + city + '\'' + "\n" +
                ", country='" + country + '\'' + "\n" +
                '}';
    }
}

class Book {
    private String title;
    private String author;
    private int year;
    private String genre;
    private String price;
    private String isbn;
    private Publisher publisher;
    private String language;
    private String awards;

    // Сеттеры
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setYear(int year) { this.year = year; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setPrice(String price) { this.price = price; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setPublisher(Publisher publisher) { this.publisher = publisher; }
    public void setLanguage(String language) { this.language = language; }
    public void setAwards(String awards) { this.awards = awards; }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' + "\n" +
                ", author='" + author + '\'' + "\n" +
                ", year=" + year + "\n" +
                ", genre='" + genre + '\'' + "\n" +
                ", price='" + price + '\'' + "\n" +
                ", isbn='" + isbn + '\'' + "\n" +
                ", publisher=" + publisher +
                ", language='" + language + '\'' + "\n" +
                ", awards='" + awards + '\'' + "\n" +
                '}';
    }
}

public class Programm {
    public static void main(String[] args) {
        String filePath = "D:/random_structure_14.xml";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line;

            // Читаем весь файл в одну строку
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

            // Парсинг отдельных полей книги
            currentBook.setTitle(extractContent(bookContent, "title"));
            currentBook.setAuthor(extractContent(bookContent, "author"));
            currentBook.setYear(Integer.parseInt(extractContent(bookContent, "year")));
            currentBook.setGenre(extractContent(bookContent, "genre"));
            currentBook.setPrice(extractPrice(bookContent));
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

    private static String extractPrice(String content) {
        String startTag = "<price";
        String endTag = "</price>";
        int startIndex = content.indexOf(startTag);
        if (startIndex == -1) return "";

        int valueStartIndex = content.indexOf(">", startIndex) + 1;
        int valueEndIndex = content.indexOf(endTag, valueStartIndex);
        if (valueEndIndex == -1) return "";

        return content.substring(valueStartIndex, valueEndIndex).trim();
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

    private static String extractAwards(String content) {
        StringBuilder awards = new StringBuilder();
        String startTag = "<award>";
        String endTag = "</award>";
        int startIndex = 0;

        while ((startIndex = content.indexOf(startTag, startIndex)) != -1) {
            int endIndex = content.indexOf(endTag, startIndex);
            if (endIndex == -1) break; 
            awards.append(extractContent(content.substring(startIndex, endIndex + endTag.length()), "award")).append(", ");
            startIndex = endIndex + endTag.length();
        }

        if (awards.length() > 0) {
            awards.setLength(awards.length() - 2);
        }
        return awards.toString();
    }
}
