import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class InitializeDB {

    public static void main(String[] args) {
        initializeCandidatesInDB();
        initializeVotersInDB();
    }

    private static void initializeCandidatesInDB() {
        Scanner in = new Scanner(System.in);
        String urlLink = "http://localhost:3000/new/candidate/candidate-";
        InputStream inputStream = null;
        try {
            System.out.println("Connecting to " + urlLink);
            int i = 1;
            while (i <= 5) {
                runRequests(null, urlLink, i);
                i++;
            }
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

    private static void initializeVotersInDB() {
        Scanner in = new Scanner(System.in);
        String urlLink = "http://localhost:3000/new/voter/";
        InputStream inputStream = null;
        try {
            System.out.println("Connecting to " + urlLink);
            int i = 1;
            while (i <= 100) {
                runRequests(null, urlLink, i);
                i++;
            }
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

    private static void runRequests(InputStream inputStream, String urlLink, int i) throws IOException {
        URL url = new URL(urlLink+i);
        HttpURLConnection con;
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        inputStream = con.getInputStream();
        readResponse(inputStream);
    }

    private static void readResponse(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader in = new BufferedReader(inputStreamReader);
        StringBuffer strBuf = new StringBuffer();
        String line;

        while ((line = in.readLine()) != null) {
            strBuf.append(line);
//            System.out.println(line);
        }
    }
}
