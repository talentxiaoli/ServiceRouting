

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class DhcpServer {

    private static final String TAG = "DhcpServer";

    private static String sr_prev = "fd15:4ba5:5a2b:1008";
    private static String sr_hash = "sha256";
    private static String sr_ext_ip = "fd15:4ba5:5a2b:1008:bde7:5050:c0bc:42c1";


    private static ServerSocket dhcp_server;
    private static Socket dhcp_client;
    private static BufferedReader br_in;

    public DhcpServer() {
        System.out.println("DhcpServer::DhcpServer()");
    }

    public void startDhcpServer() {
        System.out.println("DhcpServer::startDhcpServer()");
        try {
            dhcp_server = new ServerSocket(6666);
            while(true) {
                System.out.println("DhcpServer::start to accept client");
                dhcp_client = dhcp_server.accept();
                new Thread(new ServerThread(dhcp_client)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dhcp_server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void closeDhcpServer() {
        System.out.println("DhcpServer::closeDhcpServer()");
        try {
            dhcp_server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ServerThread implements Runnable {

        private Socket dhcp_client;
        private BufferedWriter out;

        public ServerThread(Socket dhcp_client) {
            this.dhcp_client = dhcp_client;
            String dchp_client_ip = dhcp_client.getInetAddress().getHostAddress();
            int dhcp_client_port = dhcp_client.getPort();
            System.out.println("DhcpServer::" + "ip =" + dchp_client_ip + "-------" + "port = " + dhcp_client_port);
            try {
                out = new BufferedWriter(new OutputStreamWriter(dhcp_client.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            System.out.println("DhcpServer::handle the client req");
            try {
                JsonObject jb = new JsonObject();
                jb.addProperty("sr_prev", sr_prev);
                jb.addProperty("sr_hash", sr_hash);
                jb.addProperty("sr_ext_ip", sr_ext_ip);
                out.write(jb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    out.close();
                    dhcp_client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        DhcpServer server = new DhcpServer();
        server.startDhcpServer();
        server.closeDhcpServer();
    }

}

