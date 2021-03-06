import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class CountMeUpTester {

    public static final int MAX_CLIENTS = 100;
    public static CountDownLatch startLatch = new CountDownLatch(1);
    public static void main(String[] args) {

        CountMeUpTesterClient countMeUpTesterClient;

        Thread clientThread;
        ArrayList<Thread> clientThreads = new ArrayList<>(MAX_CLIENTS);
        int voterId = 1;
        for(int i = 1; i <= MAX_CLIENTS; i++) {
            countMeUpTesterClient = new CountMeUpTesterClient(voterId);
            clientThread = new Thread(countMeUpTesterClient);
            clientThreads.add(clientThread);
            clientThread.setDaemon(true);

            clientThread.start();
            voterId++;
        }
        startLatch.countDown();
        for(Thread clThread : clientThreads) {
            try {
                clThread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Exiting test");
    }
}

class CountMeUpTesterClient implements Runnable {
    private int voterId;

    public CountMeUpTesterClient(int voterId) {
        this.voterId = voterId;
    }

    private void readResponse(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader in = new BufferedReader(inputStreamReader);
        StringBuffer strBuf = new StringBuffer();
        String line;

        while((line = in.readLine()) != null) {
            strBuf.append(line);
             System.out.println(line);
        }
    }

    @Override
    public void run() {
        Scanner in  = new Scanner(System.in);
        InputStream inputStream = null;
        try {
            CountMeUpTester.startLatch.await();
            int i = 1;
            while(i <= 3) {
                System.out.println("user " + voterId + " voted " + i + " time(s)");
                int rand = randomWithRange(1, 5);
                String urlLink = "http://localhost:3000/vote/" + voterId + "/candidate-" + rand;
                URL url = new URL(urlLink);
                inputStream = null;
                HttpURLConnection con;
                con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                inputStream = con.getInputStream();
                readResponse(inputStream);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        in.close();
    }

    int randomWithRange(int min, int max) {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
}