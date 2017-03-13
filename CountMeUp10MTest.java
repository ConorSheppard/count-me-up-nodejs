import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

public class CountMeUp10MTest {

    public static final int MAX_CLIENTS = 100;
    public static CountDownLatch startLatch = new CountDownLatch(1);
    public static volatile Deque<Integer> stack = new ArrayDeque<Integer>(3300);
    public static void main(String[] args) {

        CountMeUp10MTestClient countMeUp10MTestClient;

        Thread clientThread;
        ArrayList<Thread> clientThreads = new ArrayList<>(MAX_CLIENTS);
        int j = 0;
        System.out.println("before while");
        while (j <= 3300) {
            stack.push(j);
            j++;
        }
        System.out.println("before for");
        for(int i = 1; i <= MAX_CLIENTS; i++) {
            System.out.println("in for: " + i);
            countMeUp10MTestClient = new CountMeUp10MTestClient(stack.pop());
            clientThread = new Thread(countMeUp10MTestClient);
            clientThreads.add(clientThread);
            clientThread.setDaemon(true);
            clientThread.start();
        }
        System.out.println("before countDown");
        startLatch.countDown();
        for(Thread clThread : clientThreads) {
            try {
                clThread.join();
                 System.out.println("Reaping client thread " + clThread);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Exiting test");
    }
}

class CountMeUp10MTestClient implements Runnable {
    private int voterId;

    public CountMeUp10MTestClient(int voterId) {
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
        System.out.println("Starting thread " + voterId + " ...");
        Scanner in  = new Scanner(System.in);
        InputStream inputStream = null;
        try {
            CountMeUp10MTest.startLatch.await();
            int i = 1;
            System.out.println("before while in run");
            while(i <= 99) {
                System.out.println("voter " + voterId + " voted " + i);
                int rand = randomWithRange(1, 5);
                String urlLink = "http://localhost:3000/vote/" + voterId + "/candidate-" + rand;
                URL url = new URL(urlLink);
                inputStream = null;
                HttpURLConnection con;
                con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                inputStream = con.getInputStream();
                readResponse(inputStream);
                if(i % 3 == 0) {
                    int newVoterId = CountMeUp10MTest.stack.pop();
                    System.out.println("newVoterId: " + newVoterId);
                    voterId = newVoterId ;
                }
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
