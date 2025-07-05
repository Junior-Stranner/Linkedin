package br.com.judev.backend.feature.authentication.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;
/*
A anotação @FullTextField é usada para marcar um campo da entidade como indexado para busca textual completa
 (full-text search). Ou seja, esse campo será armazenado em um índice e poderá ser pesquisado com recursos como:
Busca por palavras-chave,
Busca parcial (ex: "Jo" encontra "João"),
Relevância,
Tokenização, etc.

O parâmetro analyzer define como o texto será processado antes de ser armazenado no índice.
 O "standard" é o analisador padrão do Lucene/Elasticsearch, que:
Converte texto para minúsculas,
Remove pontuação,
Divide o texto por espaços,
E aplica outras regras básicas de tokenização.
 */
@Entity(name = "users")
//@Indexed(index = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Email
    @Column(unique = true)
    private String email;
    private Boolean emailVerified = false;
    private String emailVerificationToken = null;
    private LocalDateTime emailVerificationTokenExpiryDate = null;
    @JsonIgnore
    private String password;
    private String passwordResetToken = null;
    private LocalDateTime passwordResetTokenExpiryDate = null;

    @FullTextField(analyzer = "standard")
    private String firstName = null;
    @FullTextField(analyzer = "standard")
    private String lastName = null;
    @FullTextField(analyzer = "standard")
    private String company = null;
    @FullTextField(analyzer = "standard")
    private String position = null;
    private String location = null;
    private String profilePicture = null;
    private String coverPicture = null;
    private Boolean profileComplete = false;
    private String about = null;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public User() {
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getEmail() {
        return email;
    }

    public String getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(String emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
    }

    public LocalDateTime getEmailVerificationTokenExpiryDate() {
        return emailVerificationTokenExpiryDate;
    }

    public void setEmailVerificationTokenExpiryDate(LocalDateTime emailVerificationTokenExpiryDate) {
        this.emailVerificationTokenExpiryDate = emailVerificationTokenExpiryDate;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public LocalDateTime getPasswordResetTokenExpiryDate() {
        return passwordResetTokenExpiryDate;
    }

    public void setPasswordResetTokenExpiryDate(LocalDateTime passwordResetTokenExpiryDate) {
        this.passwordResetTokenExpiryDate = passwordResetTokenExpiryDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        updateProfileCompletionStatus();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        updateProfileCompletionStatus();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        updateProfileCompletionStatus();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
        updateProfileCompletionStatus();
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void updateProfile(String firstName, String lastName, String company,
                              String position, String location, String about) {
        if (firstName != null) this.firstName = firstName;
        if (lastName != null) this.lastName = lastName;
        if (company != null) this.company = company;
        if (position != null) this.position = position;
        if (location != null) this.location = location;
        if (about != null) this.about = about;

        updateProfileCompletionStatus();
    }

    public void updateProfileCompletionStatus() {
        this.profileComplete = (this.firstName != null && this.lastName != null &&
                this.company != null && this.position != null &&
                this.location != null);
    }


    public Boolean getProfileComplete() {
        return profileComplete;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getCoverPicture() {
        return coverPicture;
    }

    public void setCoverPicture(String coverPicture) {
        this.coverPicture = coverPicture;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }
}
