package com.example.storereservation.domain.partner.persist;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Entity(name = "PARTNER")
public class PartnerEntity implements UserDetails {

    @Id
    private String partnerId;
    private String password;

    private String partnerName;
    private String phone;

    private Long storeId;
    private String storeName;

    private String memberType; //ROLE_PARTNER

    private LocalDateTime createAt;
    private LocalDateTime updateDt;

    public void setStore(Long storeId, String storeName){
        this.storeId = storeId;
        this.storeName = storeName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_PARTNER"));
        return grantedAuthorities;
    }

    @Override
    public String getUsername() {
        return this.partnerId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
