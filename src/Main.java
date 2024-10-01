import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        runSimulation("simulation1.txt");
    }

    public static void runSimulation(String filename) {
        HashMap<Integer,SimpleHost> hosts = new HashMap<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;
            int hostAddress = Integer.parseInt(br.readLine());
            SimpleHost host = new SimpleHost();
            FutureEventList fel = new LinkedEventList();
            host.setHostParameters(hostAddress, fel);
            hosts.put(hostAddress, host);

            line = br.readLine();
            while (!line.equals("-1")) {
                String[] parts = line.split(" ");
                int neighborAddress = Integer.parseInt(parts[0]);
                int distance = Integer.parseInt(parts[1]);
                SimpleHost neighborHost = new SimpleHost();
                neighborHost.setHostParameters(neighborAddress, fel);
                host.addNeighbor(neighborHost, distance);
                neighborHost.addNeighbor(host,distance);
                hosts.put(neighborAddress, neighborHost);
                line = br.readLine();
            }

            line = br.readLine();
            while (line != null) {
                String[] params = line.split(" ");
                int srcAddr = Integer.parseInt(params[0]);
                int destAddr = Integer.parseInt(params[1]);
                int interval = Integer.parseInt(params[2]);
                int duration = Integer.parseInt(params[3]);

                SimpleHost srcHost = hosts.get(srcAddr);
                srcHost.sendPings(destAddr, interval, duration);
                line = br.readLine();
            }

            while (fel.size() != 0) {
                Event current = fel.removeFirst();
                if (current == null) {
                    break;
                }
                current.handle();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}