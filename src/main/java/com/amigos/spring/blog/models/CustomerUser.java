package com.amigos.spring.blog.models;
/*
 - CustomerUser = User, who uses the application
 - Has properties like id, name, email, password, intro, role etc
 */

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "customer_users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @Size(min = 3, max = 100, message = "Name must be between 3 & 100 characters")
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Email must be a valid email")
    @NotBlank(message = "Email is required.")
    private String email;

    @Size(min = 3, max = 50, message = "Password should be between 3 & 50 characters")
    @NotEmpty(message = "Empty password not allowed.")
    @Column(name = "password", nullable = false)
    private String password;

    @Size(min = 10, max = 300,message = "Introduction must be at least 10 & maximum 300 characters long")
    @Column(name = "intro", nullable = false)
    private String intro;

    @Column(name = "created_date")
    private Date createdDate = new Date();

    @Column(name = "deleted_date")
    private Date deletedDate = null ;

}
