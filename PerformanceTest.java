import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class PerformanceTest {

    public static void main(String[] args) throws MalformedURLException {

        String urlLink = "http://localhost:3000/vote/count/candidate-";
        int i = 1;
        long startTime = System.nanoTime();
        while (i <= 5) {
            testPerformance(urlLink+i);
            i++;
        }
        long endTime = System.nanoTime();

        long testDuration = endTime - startTime;
        // Test duration in nanoseconds
        System.out.println("test duration = " + testDuration);
    }

    private static void readResponse(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader in = new BufferedReader(inputStreamReader);
        StringBuffer strBuf = new StringBuffer();
        String line;

        while((line = in.readLine()) != null) {
            strBuf.append(line);
            System.out.println(line);
        }
    }

    private static void testPerformance(String urlLink) throws MalformedURLException {
        Scanner in = new Scanner(System.in);
        InputStream inputStream = null;
        System.out.println("Connecting to " + urlLink);
        URL url = new URL(urlLink);
        inputStream = null;
        HttpURLConnection con;
        int i = 0;
        long startTime = System.nanoTime();
        try {
            System.out.println("Connecting to " + urlLink);

            con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            inputStream =  con.getInputStream();
            readResponse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        in.close();
    }
}
