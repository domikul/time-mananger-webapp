/*
 * Created on 2020-07-10 ( Time 09:27:29 )
 * Generated by Telosys Tools Generator ( version 3.1.2 )
 */
// This Bean has a basic Primary Key (not composite) 

package pl.wasko.time.manager.rest.api.model.entity;

import java.io.Serializable;

//import javax.validation.constraints.* ;
//import org.hibernate.validator.constraints.* ;

import java.util.List;

import javax.persistence.*;

/**
 * Persistent class for entity stored in table "emails"
 *
 * @author Telosys Tools Generator
 *
 */

@Entity
@Table(name="emails")
// Define named queries here
@NamedQueries ( {
  @NamedQuery ( name="Emails.countAll", query="SELECT COUNT(x) FROM Email x" )
} )
public class Email implements Serializable
{
    private static final long serialVersionUID = 1L;

    //----------------------------------------------------------------------
    // ENTITY PRIMARY KEY ( BASED ON A SINGLE FIELD )
    //----------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id_email", nullable=false)
    private Integer    idEmail      ;


    //----------------------------------------------------------------------
    // ENTITY DATA FIELDS 
    //----------------------------------------------------------------------    
    @Column(name="email_name", nullable=false, length=255)
    private String     emailName    ;



    //----------------------------------------------------------------------
    // ENTITY LINKS ( RELATIONSHIP )
    //----------------------------------------------------------------------
    @OneToMany(mappedBy="email", targetEntity= Subscription.class)
    private List<Subscription> listOfSubscriptions;

    @OneToMany(mappedBy="email", targetEntity= User.class)
    private List<User> listOfUsers ;


    //----------------------------------------------------------------------
    // CONSTRUCTOR(S)
    //----------------------------------------------------------------------
    public Email()
    {
		super();
    }

    public Email(Integer idEmail, String emailName) {
        this.idEmail = idEmail;
        this.emailName = emailName;
    }

    //----------------------------------------------------------------------
    // GETTER & SETTER FOR THE KEY FIELD
    //----------------------------------------------------------------------
    public void setIdEmail( Integer idEmail )
    {
        this.idEmail = idEmail ;
    }
    public Integer getIdEmail()
    {
        return this.idEmail;
    }

    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR FIELDS
    //----------------------------------------------------------------------
    //--- DATABASE MAPPING : email_name ( VARCHAR ) 
    public void setEmailName( String emailName )
    {
        this.emailName = emailName;
    }
    public String getEmailName()
    {
        return this.emailName;
    }


    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR LINKS
    //----------------------------------------------------------------------
    public void setListOfSubscriptions( List<Subscription> listOfSubscriptions )
    {
        this.listOfSubscriptions = listOfSubscriptions;
    }
    public List<Subscription> getListOfSubscriptions()
    {
        return this.listOfSubscriptions;
    }

    public void setListOfUsers( List<User> listOfUsers )
    {
        this.listOfUsers = listOfUsers;
    }
    public List<User> getListOfUsers()
    {
        return this.listOfUsers;
    }


    //----------------------------------------------------------------------
    // toString METHOD
    //----------------------------------------------------------------------
    public String toString() { 
        StringBuffer sb = new StringBuffer(); 
        sb.append("["); 
        sb.append(idEmail);
        sb.append("]:"); 
        sb.append(emailName);
        return sb.toString(); 
    }

    public Integer getId() {
        return this.getIdEmail();
    }
}
