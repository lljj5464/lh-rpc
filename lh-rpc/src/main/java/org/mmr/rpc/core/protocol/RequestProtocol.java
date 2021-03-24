package org.mmr.rpc.core.protocol;

import java.io.Serializable;

/**RPC框架请求协议类，用于在网络中传输。如果一个类要在网络中传输，必须要实现接口Serializable
 * @author shkstart
 * @create 2021-03-07 13:51
 */
public class RequestProtocol implements Serializable {
    private String interfaceClassName;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameterValues;

    public String getInterfaceClassName() {
        return interfaceClassName;
    }

    public void setInterfaceClassName(String interfaceClassName) {
        this.interfaceClassName = interfaceClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(Object[] parameterValues) {
        this.parameterValues = parameterValues;
    }
}
