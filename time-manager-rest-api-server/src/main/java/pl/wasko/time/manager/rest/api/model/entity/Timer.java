/*
 * Created on 2020-07-10 ( Time 09:27:29 )
 * Generated by Telosys Tools Generator ( version 3.1.2 )
 */
// This Bean has a basic Primary Key (not composite) 

package pl.wasko.time.manager.rest.api.model.entity;

import org.hibernate.annotations.Where;

import java.io.Serializable;

//import javax.validation.constraints.* ;
//import org.hibernate.validator.constraints.* ;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * Persistent class for entity stored in table "timers"
 *
 * @author Telosys Tools Generator
 *
 */

@Entity
@Table(name="timers")
// Define named queries here
@NamedQueries ( {
  @NamedQuery ( name="Timers.countAll", query="SELECT COUNT(x) FROM Timer x" )
} )
@Where(clause = "deleted='0'")
public class Timer implements Serializable
{
    private static final long serialVersionUID = 1L;

    //----------------------------------------------------------------------
    // ENTITY PRIMARY KEY ( BASED ON A SINGLE FIELD )
    //----------------------------------------------------------------------
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id_timer", nullable=false)
    private Integer    idTimer      ;


    //----------------------------------------------------------------------
    // ENTITY DATA FIELDS 
    //----------------------------------------------------------------------    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="start_time", nullable=false)
    private Date       startTime    ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="end_time")
    private Date       endTime      ;

    @Column(name="deleted", nullable=false)
    private Boolean    deleted      ;

	// "taskId" (column "task_id") is not defined by itself because used as FK in a link 
	// "userId" (column "user_id") is not defined by itself because used as FK in a link 


    //----------------------------------------------------------------------
    // ENTITY LINKS ( RELATIONSHIP )
    //----------------------------------------------------------------------
    @ManyToOne
    @JoinColumn(name="user_id", referencedColumnName="id_user")
    private User user;

    @ManyToOne
    @JoinColumn(name="task_id", referencedColumnName="id_task")
    private Task task;

    @OneToMany(mappedBy="timer", targetEntity= TimerStep.class)
    @OrderBy(value = "startTime ASC")
    private List<TimerStep> listOfTimerSteps;


    //----------------------------------------------------------------------
    // CONSTRUCTOR(S)
    //----------------------------------------------------------------------
    public Timer()
    {
		super();
    }
    
    //----------------------------------------------------------------------
    // GETTER & SETTER FOR THE KEY FIELD
    //----------------------------------------------------------------------
    public void setIdTimer( Integer idTimer )
    {
        this.idTimer = idTimer ;
    }
    public Integer getIdTimer()
    {
        return this.idTimer;
    }

    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR FIELDS
    //----------------------------------------------------------------------
    //--- DATABASE MAPPING : start_time ( DATETIME ) 
    public void setStartTime( Date startTime )
    {
        this.startTime = startTime;
    }
    public Date getStartTime()
    {
        return this.startTime;
    }

    //--- DATABASE MAPPING : end_time ( DATETIME ) 
    public void setEndTime( Date endTime )
    {
        this.endTime = endTime;
    }
    public Date getEndTime()
    {
        return this.endTime;
    }

    //--- DATABASE MAPPING : deleted ( BIT ) 
    public void setDeleted( Boolean deleted )
    {
        this.deleted = deleted;
    }
    public Boolean getDeleted()
    {
        return this.deleted;
    }


    //----------------------------------------------------------------------
    // GETTERS & SETTERS FOR LINKS
    //----------------------------------------------------------------------
    public void setUser(User user)
    {
        this.user = user;
    }
    public User getUser()
    {
        return this.user;
    }

    public void setTask(Task task)
    {
        this.task = task;
    }
    public Task getTask()
    {
        return this.task;
    }


    //----------------------------------------------------------------------
    // toString METHOD
    //----------------------------------------------------------------------
    public String toString() { 
        StringBuffer sb = new StringBuffer(); 
        sb.append("["); 
        sb.append(idTimer);
        sb.append("]:"); 
        sb.append(startTime);
        sb.append("|");
        sb.append(endTime);
        sb.append("|");
        sb.append(deleted);
        return sb.toString(); 
    }

    public List<TimerStep> getListOfTimerSteps() {
        return listOfTimerSteps;
    }

    public void setListOfTimerSteps(List<TimerStep> listOfTimerSteps) {
        this.listOfTimerSteps = listOfTimerSteps;
    }

    public Boolean isUserOwner(User user) {
        return this.user.equals(user);
    }
}
