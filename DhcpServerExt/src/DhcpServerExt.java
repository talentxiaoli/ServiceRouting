

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class DhcpServerExt {

    private static final String TAG = "DhcpServer";


    private static ServerSocket dhcp_server;
    private static Socket dhcp_client;
    private static BufferedReader br_in;

    public DhcpServerExt() {
        System.out.println("DhcpServer::DhcpServer()");
    }

    public void startDhcpServerExt() {
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

    public void closeDhcpServerExt() {
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
                out.write("wo shi ext");
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
        DhcpServerExt server = new DhcpServerExt();
        server.startDhcpServerExt();
        server.closeDhcpServerExt();
    }

}

