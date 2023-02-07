package top.yinzsw.blog.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import top.yinzsw.blog.constant.MQConst;
import top.yinzsw.blog.model.dto.EmailCodeDTO;

/**
 * 邮箱消息队列监听器
 *
 * @author yinzsW
 * @since 23/01/01
 */
@Slf4j
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(MQConst.EMAIL_CODE_QUEUE),
        exchange = @Exchange(MQConst.EMAIL_EXCHANGE),
        key = MQConst.EMAIL_CODE_KEY
))
@Service
@RequiredArgsConstructor
public class EmailCaptchaListener {
    private final JavaMailSender mailSender;
    private @Value("${spring.mail.username}") String from;

    @RabbitHandler
    public void sendEmailVerificationCode(EmailCodeDTO emailCodeDTO) {
        String subject = "验证码 [yinzsW]";
        String verificationCodeText = createVerificationCodeText(emailCodeDTO.getCode(), emailCodeDTO.getTimeoutMinutes());
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(emailCodeDTO.getEmail());
        message.setSubject(subject);
        message.setText(verificationCodeText);
        try {
            mailSender.send(message);
        } catch (MailException e) {
            log.error("验证码发送失败 接收者邮箱:{} 验证码:{}", emailCodeDTO.getEmail(), emailCodeDTO.getCode());
        }
    }

    private String createVerificationCodeText(String code, long minutes) {
        return String.format("您的验证码为 %s 有效期%d分钟，请不要告诉他人哦！", code, minutes);
    }
}
