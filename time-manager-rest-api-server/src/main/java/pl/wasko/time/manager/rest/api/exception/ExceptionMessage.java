package pl.wasko.time.manager.rest.api.exception;

public enum ExceptionMessage {

    BUCKET_NOT_FOUND("There is no bucket with such an id"),
    TASK_NOT_FOUND("There is no task with such an id"),
    USER_NOT_FOUND("There is no user with such an id"),
    ROLE_NOT_FOUND("There is no role with such id"),
    EMAIL_NOT_FOUND("There is no email with such an id"),
    EMAIL_ALREADY_EXISTS("Such an email already exists"),
    POSITION_NOT_FOUND("There is no position with such id"),
    COOWNER_NOT_FOUND("There is no Co Owner with given ID in this bucket"),
    TIMER_NOT_FOUND("There is no timer with such an id"),
    STATUS_NOT_FOUND("There is no status with such an id"),
    PRIORITY_NOT_FOUND("There is no priority with such an id"),
    SUBSCRIPTION_NOT_FOUND("There is no subscription with such an id"),
    INSUFFICIENT_PERMISSIONS("Only admin can perform this action"),
    WRONG_USER_IDS("Wrong user ids"),
    WRONG_EMAIL("This email is already used."),
    WRONG_STATUS_SELECTED("Wrong status selected"),
    WRONG_UPDATE_TASK_NUMBER("The specified maximum number of tasks will not accommodate the existing ones."),
    DEADLINE_BEFORE_CURRENT("Deadline cannot be before current date"),
    NEGATIVE_ESTIMATED_END_TIME("Estimated end time cannot be negative"),
    USER_NOT_SHARED_TASK("This user is not shared this task"),
    NOT_ALLOWED_SHARE_TASK("You are not allowed to share this task"),
    NOT_ALLOWED_GET_TASK_SHARES("You are not allowed to get this task shares"),
    NOT_ALLOWED_SHARE_BUCKET("You are not allowed to share this bucket"),
    NOT_ALLOWED_EDIT_TASK_SHARES("You are not allowed to edit this task's shares"),
    NOT_ALLOWED_EDIT_FINISHED_TASK("You are not allowed to edit finished task"),
    NOT_ALLOWED_EDIT_TASK("You are not allowed to edit this task"),
    NOT_ALLOWED_EDIT_USER("You are not allowed to modify this data."),
    NOT_ALLOWED_START_TIMER("You are not allowed to start timer in this task"),
    NOT_ALLOWED_STOP_TIMER("You are not allowed to stop this timer"),
    NOT_ALLOWED_SUSPEND_TIMER("You are not allowed to suspend this timer"),
    NOT_ALLOWED_RESUME_TIMER("You are not allowed to resume this timer"),
    NOT_ALLOWED_DELETE_TASK("You are not allowed to delete this task"),
    NOT_ALLOWED_DELETE_BUCKET("You are not allowed to delete this bucket"),
    NOT_ALLOWED_UPDATE_BUCKET("You are not allowed to update this bucket"),
    NOT_ALLOWED_GET_BUCKET_SHARES("You are not allowed to get this bucket shares"),
    NOT_ALLOWED_ADD_SUBSCRIPTION("You are not allowed to add subscription to this task"),
    NOT_ALLOWED_DELETE_SUBSCRIPTION("You are not allowed to delete this subscription"),
    NOT_AUTHORIZED_TO_BUCKET("You are not authorized to this bucket"),
    NOT_AUTHORIZED_TO_TASK("You are not authorized to this task"),
    NOT_AUTHORIZED("You are not authorized to access this data"),
    CANNOT_ADD_TASKS("You cannot add more tasks"),
    CANNOT_SEND_END_REQUEST("You cannot send end request to this task"),
    TOKEN_NOT_VALID_EMAIL("Token does not specify valid email"),
    TOKEN_NOT_ANY_USER("Token does not specify any user"),
    CANNOT_ADD_BUCKETS("Your account type does not allow you to add more buckets"),
    CANNOT_ADD_MORE_THAN_ONE_TIMER("You cannot add more than one timer to a task"),
    NOT_ACTIVATED("You are not activated"),
    EMAIL_IN_USE("You cannot delete this email. This email is currently in use"),
    WRONG_TOKEN("Wrong token"),
    UNAUTHORIZED("Unauthorized"),
    WRONG_CREDENTIALS("Wrong credentials"),
    ALREADY_SUBSCRIBED("This email already subscribes this task"),
    ALREADY_END_REQUESTED("You already requested this task to end"),
    CANNOT_SHARE_TASK_TO_YOURSELF("You cannot share task to yourself"),
    CANNOT_SHARE_BUCKET_TO_YOURSELF("You cannot share bucket to yourself"),
    CANNOT_GET_INACTIVE_USERS("Only admin can get inactive users"),
    CANNOT_GET_INACTIVE_USER_BUCKETS("You have to be an admin and user has to be inactive");

    private final String message;

    private ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
