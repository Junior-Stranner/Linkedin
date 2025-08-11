package br.com.judev.backend.feature.authentication.listeners;

import br.com.judev.backend.feature.authentication.event.EmailVerificationEvent;
import br.com.judev.backend.feature.authentication.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;


/*Indica que essa classe é um componente gerenciado pelo Spring, ou seja,
 ele vai criar a instância automaticamente e injetar as dependências.*/
@Component
public class EmailVerificationListener {

    private static final Logger logger = LoggerFactory.getLogger(EmailVerificationListener.class);
    private final EmailService emailService;

    public EmailVerificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    /*@TransactionalEventListener: diz que esse método vai reagir a um evento publicado dentro de uma transação.
phase = TransactionPhase.AFTER_COMMIT: a ação só vai rodar depois que a transação do banco for concluída
com sucesso. Ou seja, o e-mail só será enviado se o registro do usuário no banco tiver sido salvo corretamente.*/
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmailVerificationEvent(EmailVerificationEvent event){
        try{
            emailService.sendEmail(event.getToEmail(), event.getSubject(), event.getBody());
            logger.info("Verification email sent to {}", event.getToEmail());
        }catch(Exception e){
            logger.error("Failed to send verification email to {}: {}", event.getToEmail(), e.getMessage(), e);
        }
    }
}

/*
handleEmailVerificationEvent recebe o evento EmailVerificationEvent, que traz
as informações do destinatário, assunto e corpo do e-mail.

Tenta enviar o e-mail usando o serviço emailService.

Se enviar com sucesso, registra uma mensagem informativa no log.

Se falhar, registra o erro no log para ajudar no diagnóstico, mas não lança
exceção para não quebrar o fluxo da aplicação.

Resumo simples:
Essa classe ouve o evento de verificação de email e, após a confirmação
 que o usuário foi salvo no banco, envia o email de verificação de forma assíncrona e segura,
 tratando erros sem atrapalhar o registro.
 */
