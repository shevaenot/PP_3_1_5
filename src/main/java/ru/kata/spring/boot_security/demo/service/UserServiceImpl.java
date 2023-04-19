package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final RoleDao roleDao;

    public UserServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userDao.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> listUsers() {
        return userDao.findAll();
    }

    @Override
    @Transactional
    public boolean saveUser(User user) {
        User userBas = userDao.findByName(user.getUsername());
        if (userBas != null) {
            return false;
        }
        user.setPassword(bCryptPasswordEncoder().encode(user.getPassword()));
        System.out.println(user);
        List<String> list = user.getRoles().stream().map(Role::getRole).collect(Collectors.toList());
        List<Role> roleList = listByRole(list);
        user.setRoles(roleList);
        userDao.save(user);
        return true;
    }

    @Override
    @Transactional
    public void removeUser(Long id) {
        userDao.deleteById(id);
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        User userBase = getById(user.getId());
        if (!userBase.getPassword().equals(user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder().encode(user.getPassword()));
        }
        List<String> list = user.getRoles().stream().map(Role::getRole).collect(Collectors.toList());
        List<Role> roleList = listByRole(list);
        user.setRoles(roleList);
        userDao.save(user);
    }

    @Override
    public User findByName(String userName) {
        return userDao.findByName(userName);
    }

    @Override
    public List<Role> listByRole(List<String> name) {
        return roleDao.listByName(name);
    }

}
