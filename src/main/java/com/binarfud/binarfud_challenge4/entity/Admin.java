package com.binarfud.binarfud_challenge4.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class Admin {
    @Id
    @Column(name = "admin_id")
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String adminId;

    @Column(name = "username", length = 100)
    private String username;

    @Column(length = 100)
    private String password;
}
