package org.example.service;

/**
 * @author shkstart
 * @create 2021-03-07 14:54
 */
public class UserServiceImpl implements UserService{
    @Override
    public String addUsername(String name) {
        return "添加的姓名为：" + name;
    }
}
