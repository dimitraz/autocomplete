package utils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.Socket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Luigi Zuccarelli
 */
public class TheServerThreadDispatcher implements Runnable {

    private Map<String, Object> map = null;
    private boolean bRunning = false;
    private String name;
    private Socket socket;
    private ServerHandler handler;

    public TheServerThreadDispatcher(Socket socket) throws IOException {
        this.socket = socket;
        handler = new ServerHandler();
    }

    public boolean isRunning() {
        return bRunning;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void run() {
        bRunning = true;
        String response = "";
        BufferedInputStream localbin = null;
        BufferedOutputStream localbout = null;

        try {
            byte[] b = new byte[8192];
            localbin = new BufferedInputStream(socket.getInputStream());
            localbout = new BufferedOutputStream(socket.getOutputStream());
            int len = localbin.read(b);
            String input = new String(b, 0, len, "UTF-8");
            System.out.println(input);

            int n = input.indexOf("name=");
            String content = "";
            if (n == -1) {
                // StringBuilder 'contentBuilder' to return the
                // contents of the HTML page as a string
                StringBuilder contentBuilder = new StringBuilder();
                try {
                    // Reads in the HTML page one
                    // line at a time
                    BufferedReader in = new BufferedReader(new FileReader("data/index.html"));
                    String str;
                    while ((str = in.readLine()) != null) {
                        // append string to contentBuilder
                        contentBuilder.append(str);
                    }
                    in.close();
                } catch (IOException e) {
                }

                // Returns the page as
                // the response
                response = contentBuilder.toString();
            } else {
                String sub = input.substring(n);
                System.out.println(sub);

                // Split sub into its various
                // sub components
                String[] array = sub.split("&");
                String[] idString = array[1].split("=");
                String[] searchValue = array[2].split("=");
                String[] matchesString = array[3].split("=");

                int id = Integer.parseInt(idString[1]);
                int matches = Integer.parseInt(matchesString[1]);

                // Send out the response
                response = handler.doAutocomplete(id, searchValue[1], matches);
                // System.out.println(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // response = json.message("ERROR " + e.toString(),"KO");
        } finally {
            try {
                // Send back the response
                byte[] bout = new byte[8192];
                int n = 0;
                ByteArrayInputStream bis = new ByteArrayInputStream(response.getBytes());
                while ((n = bis.read(bout)) != -1) {
                    localbout.write(bout, 0, n);
                    localbout.flush();
                }
                bis.close();
                localbin.close();
                localbout.close();
                socket.close();
                bRunning = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
