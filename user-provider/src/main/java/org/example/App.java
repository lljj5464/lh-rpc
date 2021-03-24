package org.example;

import org.example.service.UserService;
import org.example.service.UserServiceImpl;
import org.mmr.rpc.core.server.GRPCServer;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //创建一个RPC的服务端
        GRPCServer grpcServer = new GRPCServer();
        //发布暴露的服务
        grpcServer.publishServiceAPI(UserService.class, new UserServiceImpl());
        //启动服务
        grpcServer.start(12346);
    }
}
