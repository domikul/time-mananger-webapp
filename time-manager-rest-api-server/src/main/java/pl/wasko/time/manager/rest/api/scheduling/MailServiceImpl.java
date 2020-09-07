package pl.wasko.time.manager.rest.api.scheduling;

import lombok.AllArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import pl.wasko.time.manager.rest.api.helper.TimeHelper;
import pl.wasko.time.manager.rest.api.model.entity.Task;
import pl.wasko.time.manager.rest.api.model.entity.User;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@FunctionalInterface
interface ThrowingConsumer<T, E extends Exception> {
    void accept(T t) throws E;
}

@Component
@AllArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    static <T> Consumer<T> throwingConsumerWrapper(ThrowingConsumer<T, Exception> throwingConsumer) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    public void sendPriorityNotifications(Task task, List<String> emails) {
        String content = "<h3>Time Manager Notification</h3><br>" +
                "Task " + task.getTaskName() + " with priority " + task.getPriority().getPriority() + " is coming to an end. <br>" +
                "There is only " + TimeHelper.secondsAsTime(
                        Duration.between(new Date().toInstant(), task.getDeadline().toInstant()).getSeconds()) + " time left to finish.";

        sendMessages(emails, "Time Manager Notification - deadline alert", content);
    }

    private void sendMessages(List<String> tos, String subject, String content) {

        tos.forEach(throwingConsumerWrapper(it -> {

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            mimeMessage.setContent(content, "text/html");
            helper.setTo(it);
            helper.setSubject(subject);
            helper.setFrom("noreply@time-manager.com");

            System.out.println("mail send mock");
            mailSender.send(mimeMessage);
        }));

    }

    @Override
    public void sendEndRequestNotification(Task task, User user) {
        String content = "<h3>Time Manager Notification</h3><br>" +
                "User " + user.getFirstName() + " " + user.getLastName() + " has requested your task " + task.getTaskName() + " to an end. <br>" +
                "If you wish, you can finish it.";

        sendMessages(Collections.singletonList(task.getUser().getEmail().getEmailName()), "Time Manager Notification - end request", content);
    }
}
