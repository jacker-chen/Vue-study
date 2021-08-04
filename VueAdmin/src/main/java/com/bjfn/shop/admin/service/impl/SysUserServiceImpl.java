package com.bjfn.shop.admin.service.impl;


import com.bjfn.shop.admin.common.dto.SysPage;
import com.bjfn.shop.admin.entity.SysAuth;
import com.bjfn.shop.admin.entity.SysRole;
import com.bjfn.shop.admin.entity.SysUser;
import com.bjfn.shop.admin.mapper.SysUserMapper;
import com.bjfn.shop.admin.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfn.shop.admin.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author bjfn
 * @since 2021-07-18
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public List<SysUser> getUserList() {
        return sysUserMapper.selectList(null);
    }

    /**
     * 根据用户id查询用户角色与权限资源
     * @param user_id
     * @return
     */
    public List< GrantedAuthority> getUserRoleAndAuth(Long user_id){
        SysUser sysUser = sysUserMapper.selectById(user_id);
        if(redisUtil.hasKey("GrantedAuthority:" + sysUser.getUsername())){
            String authStr = (String) redisUtil.get("GrantedAuthority:" + sysUser.getUsername());
            return AuthorityUtils.commaSeparatedStringToAuthorityList(authStr);
        }
        String authorityStr = getAuthorityStr(user_id);

        return AuthorityUtils.commaSeparatedStringToAuthorityList(authorityStr);
    }

    @Override
    public SysUser getUserByUsername(String username) {
        return sysUserMapper.getUserByUsername(username);
    }

    @Override
    public SysPage getUsers(Object o, Integer curPage, Integer pageSize) {
        return null;
    }

    /**
     * 要挖用户id获取角色与资源的字符串，用“，”用拼接
     * @param user_id
     * @return
     */
    public String getAuthorityStr(Long user_id){
        SysUser sysUser = sysUserMapper.selectById(user_id);
        StringBuffer sb = new StringBuffer();
        //获取角色
        List<SysRole> roleList = sysUserMapper.getRoleByUserId(user_id);
        //获取资源权限
        List<SysAuth> authList = sysUserMapper.getAuthByUserId(user_id);
        String roleNameStr = roleList.stream().map(SysRole::getRoleName).collect(Collectors.joining(","));
        sb.append(roleNameStr);
        if(roleList.size() > 0){
            String authStr = authList.stream().map(SysAuth::getPermission).collect(Collectors.joining(","));
            if(authStr.length()>0){
                sb.append(",").append(authStr);
            }
        }
        //写入缓存
        redisUtil.set("GrantedAuthority:"+ sysUser.getUsername(),sb.toString(),60*60);
        return sb.toString();
    }
}
