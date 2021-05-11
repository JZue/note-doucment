package com.jzue.note.security.notesecurity;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author jzue
 * @date 2019/9/29 下午3:59
 **/
public class MyAuthority implements GrantedAuthority {
    private static final long serialVersionUID = 5698641074914331015L;

    /**
     * 权限
     */
    private String authority;

    /**
     * 权限
     * @return authority
     */
    @Override
    public String getAuthority() {
        return authority;
    }

    /**
     * 权限
     * @param authority 权限
     */
    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
