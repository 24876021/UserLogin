package com.example.userlogin.handler.password;

import com.example.userlogin.constant.RsaProperties;
import com.example.userlogin.utils.RSAUtils;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 密码编译器
对前端发送的rsa密码进行解密，后端使用BCrypt对密码进行加密
 */
@NoArgsConstructor
public class PasswordEncoder extends BCryptPasswordEncoder {

    //private String pwds = "";

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        // 接收到的前端的密码
        String pwd = rawPassword.toString();
        // 进行rsa解密
        try {
            pwd = RSAUtils.decryptByPrivateKey(RsaProperties.privateKey, pwd);
            //pwds = pwd;
            //String encode = encode(pwds);
            //System.out.println(encode);
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }
        if (encodedPassword != null && encodedPassword.length() != 0) {
            return BCrypt.checkpw(pwd, encodedPassword);
        } else {
            return false;
        }
    }

    /**
     * 注册的时候使用此方法对密码加密
     * @param rawPassword
     * @return
     */
    @Override
    public String encode(CharSequence rawPassword) {
        return super.encode(rawPassword);
    }
}
