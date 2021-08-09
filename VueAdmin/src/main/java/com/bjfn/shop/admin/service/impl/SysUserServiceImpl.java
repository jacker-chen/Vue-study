package com.bjfn.shop.admin.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bjfn.shop.admin.common.dto.SysPage;
import com.bjfn.shop.admin.entity.SysAuth;
import com.bjfn.shop.admin.entity.SysRole;
import com.bjfn.shop.admin.entity.SysUser;
import com.bjfn.shop.admin.entity.SysUserRole;
import com.bjfn.shop.admin.mapper.SysUserMapper;
import com.bjfn.shop.admin.mapper.SysUserRoleMapper;
import com.bjfn.shop.admin.service.ISysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bjfn.shop.admin.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Arrays;
import java.util.Date;
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

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        if(redisUtil.hasKey("GrantedAuthority:" + sysUser.getUserCode())){
            String authStr = (String) redisUtil.get("GrantedAuthority:" + sysUser.getUserCode());
            return AuthorityUtils.commaSeparatedStringToAuthorityList(authStr);
        }
        String authorityStr = getAuthorityStr(user_id);

        return AuthorityUtils.commaSeparatedStringToAuthorityList(authorityStr);
    }

    @Override
    public SysUser getUserByUsername(String userCode) {
        return sysUserMapper.getUserByUserCode(userCode);
    }

    @Override
    public SysPage getUsers(String userName, Integer curPage, Integer pageSize) {
        Page<SysUser> userPage = new Page<>(curPage,pageSize);
        if(!StringUtils.isBlank(userName)){
            userPage = sysUserMapper.selectPage(userPage, new QueryWrapper<SysUser>().lambda().like(SysUser::getUserName,userName));
        }else{
            userPage = sysUserMapper.selectPage(userPage, null);
        }
        SysPage sysPage = new SysPage(curPage,pageSize,userPage.getTotal(),userPage.getRecords());
        return sysPage;
    }

    @Override
    public int add(SysUser sysUser) {
        SysUser userByUserCode = sysUserMapper.getUserByUserCode(sysUser.getUserCode());
        if(userByUserCode != null){
            return -1;
        }
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        sysUser.setOperUser(SecurityContextHolder.getContext().getAuthentication().getName());
        sysUser.setCreateTime(new Date());
        int insert = sysUserMapper.insert(sysUser);
        return insert;
    }

    @Override
    public int del(Long[] ids) {
        sysUserRoleMapper.delete(new QueryWrapper<SysUserRole>().in("user_id",ids));
        int i = sysUserMapper.deleteBatchIds(Arrays.asList(ids));
        return i;
    }

    @Override
    public int edit(SysUser sysUser) {
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        int i = sysUserMapper.updateById(sysUser);
        return i;
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
        redisUtil.set("GrantedAuthority:"+ sysUser.getUserCode(),sb.toString(),60*60);
        return sb.toString();
    }
}
