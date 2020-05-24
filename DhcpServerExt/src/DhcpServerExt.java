


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;


public class DhcpServerExt {

    private static final String TAG = "DhcpServerExt";

    private static final int REQ_TYPE_GET_HASH_CONFLICT_LIST = 1;

    private ServerSocket dhcp_server;
    private Socket dhcp_client;
    private BufferedReader br_in;

    public DhcpServerExt() {
        System.out.println("DhcpServerExt::DhcpServerExt()");
    }

    public void startDhcpServerExt() {
        System.out.println("DhcpServerExt::startDhcpServer()");
        try {
            dhcp_server = new ServerSocket(7777);
            while(true) {
                System.out.println("DhcpServerExt::start to accept client");
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
        System.out.println("DhcpServerExt::closeDhcpServer()");
        try {
            dhcp_server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class ServerThread implements Runnable {

        private Socket dhcp_client;
        private BufferedReader in;
        private BufferedWriter out;

        public ServerThread(Socket dhcp_client) {
            this.dhcp_client = dhcp_client;
            String dchp_client_ip = dhcp_client.getInetAddress().getHostAddress();
            int dhcp_client_port = dhcp_client.getPort();
            System.out.println("DhcpServerExt::" + "ip =" + dchp_client_ip + "-------" + "port = " + dhcp_client_port);
            try {
                in = new BufferedReader(new InputStreamReader(dhcp_client.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(dhcp_client.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            System.out.println("DhcpServerExt::handle the client req");
            try {
                String reqData = in.readLine();
                JsonObject jb = new JsonParser().parse(reqData).getAsJsonObject();
                int type = jb.get("type").getAsInt();
                if (type == REQ_TYPE_GET_HASH_CONFLICT_LIST) {
                    // 读取冲突表
                    MyDao md = new MyDao();
                    List<USModel> datas = md.queryAll();
                    for(int i = 0;i < datas.size();i++) {
                        JsonObject jb2 = new JsonObject();
                        jb2.addProperty("url", datas.get(i).getUrl());
                        jb2.addProperty("service_id", datas.get(i).getService_id());
                        out.write(jb2.toString());
                        out.write('\n');
                        out.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    in.close();
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

