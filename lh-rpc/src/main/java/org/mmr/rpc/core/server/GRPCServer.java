package org.mmr.rpc.core.server;

import org.mmr.rpc.core.protocol.RequestProtocol;
import sun.nio.ch.ThreadPool;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * RPC框架服务端的核心实现类
 *
 * 核心实现步骤：
 * 1、注册（暴露）调用的服务接口
 * 2、启动服务端
 * @author shkstart
 * @create 2021-03-07 13:49
 */
public class GRPCServer {
    //定义存储暴露服务列表
    Map<String, Object> serverMap = new ConcurrentHashMap<>(32);
    //定义一个线程池
    ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(8,20,200, TimeUnit.MICROSECONDS, new ArrayBlockingQueue<Runnable>(10));

    /**
     * 暴露服务的方法
     * @param interfaceClass
     * @param instance
     */
    public  void publishServiceAPI(Class<?> interfaceClass, Object instance){
        this.serverMap.put(interfaceClass.getName(), instance);
    }

    /**
     * 发布服务的方法
     * @param port
     */
    public void start(int port){

        try {
            //创建网络服务端
            ServerSocket serverSocket = new ServerSocket();
            //绑定指定的端口
            serverSocket.bind(new InetSocketAddress(port));
            System.out.println("=========================lh RPC Server Staring.... ========================");

            while(true){
                poolExecutor.execute(new ServerTask(serverSocket.accept()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private class ServerTask implements Runnable{
        private final Socket socket;
        public ServerTask(Socket socket1){
            this.socket = socket1;
        }

        @Override
        public void run() {
            try(
                    ObjectInputStream deSerializer = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream serializer = new ObjectOutputStream(socket.getOutputStream());
                    ){
                //反序列化获取客户端传入的数据
                RequestProtocol requestProtocol = (RequestProtocol) deSerializer.readObject();
                //获取接口全名称
                String interfaceName = requestProtocol.getInterfaceClassName();
                Object instance = serverMap.get(interfaceName);
                if(instance == null){
                    return;
                }
                //创建一个方法对象(反射实例）
                Method method = instance.getClass().
                        getDeclaredMethod(requestProtocol.getMethodName(), requestProtocol.getParameterTypes());
                //调用方法
                Object result = method.invoke(instance, requestProtocol.getParameterValues());
                //序列化调用结果
                serializer.writeObject(result);



            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
