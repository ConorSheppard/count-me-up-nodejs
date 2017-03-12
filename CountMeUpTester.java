import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class CountMeUpTester {
    public static final int MAX_CLIENTS = 100;
    public static CountDownLatch startLatch = new CountDownLatch(1);
    public static void main(String[] args) {

        CountMeUpTesterClient CountMeUpTesterClient;

        Thread clientThread;
        ArrayList<Thread> clientThreads = new ArrayList<>(MAX_CLIENTS);
        int userId = 100;
        for(int i = 0; i < MAX_CLIENTS; i++) {
            testNotificationClient = new TestNotificationClient(userId);
            clientThread = new Thread(testNotificationClient);
            clientThreads.add(clientThread);
            clientThread.setDaemon(true);

            clientThread.start();
            userId++;
        }

        startLatch.countDown();
        for(Thread clThread : clientThreads) {
            try {
                clThread.join();
                System.out.println("Reaping client thread " + clThread);
            } catch (Exception e) {}
        }
        System.out.println("Exiting test");
    }
}

class CountMeUpTesterClient implements Runnable {
    private int userId;

    public CountMeUpTesterClient(int userId) {
        this.userId = userId;
    }

    private void readResponse(InputStream inputStream) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader in = new BufferedReader(inputStreamReader);
        StringBuffer strBuf = new StringBuffer();
        String line;

        while((line = in.readLine()) != null) {
            strBuf.append(line);
//            System.out.println(line);
        }
    }

    @Override
    public void run() {
        System.out.println("Starting thread " + userId + " ...");
        Scanner in  = new Scanner(System.in);
        String urlLink = "http://localhost:3000/vote/" + voterId + "/" + candidateId;
        InputStream inputStream = null;
        try {
            NotificationTester.startLatch.await();
            System.out.println("Connecting to " + urlLink);
            URL url = new URL(urlLink);
            inputStream = null;
            HttpURLConnection con;
            int i = 0;
            long startTime = System.nanoTime();
            while(i < 100) {
                con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("GET");
                inputStream =  con.getInputStream();
                readResponse(inputStream);
                i++;
            }
            long endTime = System.nanoTime();

            long testDuration = endTime - startTime;
            // Test duration in nanoseconds
            System.out.println("test duration = " + testDuration);

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
}