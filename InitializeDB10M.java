import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class InitializeDB10M {
    public static final int MAX_CLIENTS = 100;
    public static CountDownLatch startLatch = new CountDownLatch(1);
    public static void main(String[] args) {
        initializeCandidatesInDB();
//        initializeVotersInDB();
        CountMeUp10MTestClient countMeUp10MTestClient;

        Thread clientThread;
        ArrayList<Thread> clientThreads = new ArrayList<>(MAX_CLIENTS);
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
            while (i <= 3300) {
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

    protected static void runRequests(InputStream inputStream, String urlLink, int i) throws IOException {
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

class InitializeDbClient implements Runnable {

    public InitializeDbClient() {
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        String urlLink = "http://localhost:3000/new/voter/";
        InputStream inputStream = null;
        try {
            InitializeDB10M.startLatch.await();
            System.out.println("Connecting to " + urlLink);
            int i = 1;
            while (i <= 3300) {
                InitializeDB10M.runRequests(null, urlLink, i);
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
}