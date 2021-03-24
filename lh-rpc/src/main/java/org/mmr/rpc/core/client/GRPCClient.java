package org.mmr.rpc.core.client;

import org.mmr.rpc.core.protocol.RequestProtocol;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * RPC框架客户端核心实现类
 * @author shkstart
 * @create 2021-03-07 13:48
 */
public class GRPCClient {
    /**
     * 通过动态代理获取调用接口的对应实例
     * @param interfaceClass
     * @param address
     * @param <T>
     * @return
     */
    public static <T> T getRemoteProxy(final Class<T> interfaceClass, final InetSocketAddress address){
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass}, new InvocationHandler() {

                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        try(Socket socket = new Socket()){
                            //通过网络链接服务端
                            socket.connect(address);
                            try (
                                    //获取输出流
                                    ObjectOutputStream serializer = new ObjectOutputStream(socket.getOutputStream());
                                    //获取输入流
                                    ObjectInputStream deserializer = new ObjectInputStream(socket.getInputStream());
                                    ){
                                //创建一个RPC框架中请求协议对象
                                RequestProtocol requestProtocol = new RequestProtocol();
                                //填充属性
                                requestProtocol.setInterfaceClassName(interfaceClass.getName());
                                requestProtocol.setMethodName(method.getName());
                                requestProtocol.setParameterTypes(method.getParameterTypes());
                                requestProtocol.setParameterValues(args);
                                //序列化协议对象（放入到网络中）
                                serializer.writeObject(requestProtocol);
                                //反序列化（把服务端放入的数据获取出来
                                Object result = deserializer.readObject();
                                return result;
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        return null;
                    }
                });
    }
}
