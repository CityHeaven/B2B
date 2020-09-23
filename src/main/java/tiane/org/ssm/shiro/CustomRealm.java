package tiane.org.ssm.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tiane.org.ssm.entity.BizAgency;
import tiane.org.ssm.service.LoginService;
import tiane.org.ssm.util.JWTUtil;

@Service
public class CustomRealm extends AuthorizingRealm{

    @Autowired
    private LoginService loginService;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        // 设置操作权限
//        Set<String> permission = new HashSet<>(Arrays.asList(user.getPermission().split(",")));
//        simpleAuthorizationInfo.addStringPermissions(permission);
//        return simpleAuthorizationInfo;
        //获取登录用户名
        String name = JWTUtil.getUsername(principalCollection.toString());;
        //查询用户名称
        BizAgency user = loginService.getAgencyByName(name);
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        if(user.getParentId() == null){
            simpleAuthorizationInfo.addRole("admin");
            simpleAuthorizationInfo.addStringPermission("add");
        }else {
            simpleAuthorizationInfo.addRole("user");
            simpleAuthorizationInfo.addStringPermission("");
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = JWTUtil.getUsername(token);
        if (username == null) {
            throw new AuthenticationException("Token invalid");
        }
        BizAgency user = loginService.getAgencyByName(username);
        if (user == null) {
            throw new AuthenticationException("User didn't existed!");
        }

        if (! JWTUtil.verify(token, username, user.getPassword())) {
            throw new AuthenticationException("Username or password error");
        }

        return new SimpleAuthenticationInfo(token, token, "custom_realm");
    }
}
