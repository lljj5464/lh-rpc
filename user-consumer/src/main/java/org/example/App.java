package org.example;

import org.example.service.UserService;
import org.mmr.rpc.core.client.GRPCClient;

import java.net.InetSocketAddress;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        UserService proxy = GRPCClient.getRemoteProxy(UserService.class, new InetSocketAddress("127.0.0.1", 12346));
        String username = proxy.addUsername("gerry");
        System.out.println(username);
    }
}
