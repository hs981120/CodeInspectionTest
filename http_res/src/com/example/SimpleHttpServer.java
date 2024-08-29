package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class SimpleHttpServer {
    public static void main(String[] args) throws IOException {
        
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        
        server.createContext("/", new MyHandler());

       
        server.start();

        System.out.println("HTTP Server started on port 8000");
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
        
            String requestMethod = exchange.getRequestMethod();
            
            if (requestMethod.equalsIgnoreCase("POST")) {
          
                BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
                StringBuilder requestBody = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    requestBody.append(line);
                }
                reader.close();
                
                String response = new String(Files.readAllBytes(Paths.get("D:/megaware/http_res/http_res/IF_CLB_049_res.json")));

//                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.getResponseHeaders().set("Content-Type", "application/json");


                exchange.sendResponseHeaders(200, response.getBytes().length);


                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
            else if (requestMethod.equalsIgnoreCase("GET"))
            {

                String response = "<message>Hello, World!</message>";
                

                exchange.getResponseHeaders().set("Content-Type", "application/xml");
                

                exchange.sendResponseHeaders(200, response.getBytes().length);
                

                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            	
            }
            else {

                exchange.sendResponseHeaders(405, -1);
            }
        }
    }
}

