package pl.wasko.time.manager.rest.api.model.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class EndRequestKey implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name="user_id", nullable=false)
    private Integer    userId       ;

    @Column(name="task_id", nullable=false)
    private Integer    taskId       ;


    public EndRequestKey()
    {
        super();
    }

    public EndRequestKey(Integer userId, Integer taskId )
    {
        super();
        this.userId = userId ;
        this.taskId = taskId ;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public boolean equals(Object obj) {
        if ( this == obj ) return true ;
        if ( obj == null ) return false ;
        if ( this.getClass() != obj.getClass() ) return false ;
        EndRequestKey other = (EndRequestKey) obj;
        //--- Attribute userId
        if ( userId == null ) {
            if ( other.userId != null )
                return false ;
        } else if ( ! userId.equals(other.userId) )
            return false ;
        //--- Attribute taskId
        if ( taskId == null ) {
            if ( other.taskId != null )
                return false ;
        } else if ( ! taskId.equals(other.taskId) )
            return false ;
        return true;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;

        //--- Attribute userId
        result = prime * result + ((userId == null) ? 0 : userId.hashCode() ) ;
        //--- Attribute taskId
        result = prime * result + ((taskId == null) ? 0 : taskId.hashCode() ) ;

        return result;
    }
}
