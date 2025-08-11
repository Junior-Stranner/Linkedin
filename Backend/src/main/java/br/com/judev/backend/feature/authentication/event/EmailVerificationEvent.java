package br.com.judev.backend.feature.authentication.event;

public class EmailVerificationEvent {
    private final String toEmail;
    private final String subject;
    private final String body;

    public EmailVerificationEvent(String toEmail, String subject, String body) {
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
    }

    public String getToEmail() {
        return toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}
