    package com.microservice.scheduler;

    import com.microservice.entity.Loan;
    import com.microservice.repository.LoanRepository;
    import com.microservice.service.EmailService;
    import com.microservice.service.SmsService;

    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;

    import org.springframework.scheduling.annotation.Scheduled;
    import org.springframework.stereotype.Component;

    import java.time.LocalDate;
    import java.util.List;

    @Component
    @RequiredArgsConstructor
    @Slf4j
    public class EmiScheduler {

        private final LoanRepository repo;
        private final EmailService emailService;
        private final SmsService smsService;

        @Scheduled(cron = "0 0 9 1 * ?")
        public void run(){

            log.info("EMI Scheduler Started");

            List<Loan> loans = repo.findByStatus("APPROVED");

            for(Loan loan : loans){

                if(loan.getNextEmiDate() != null &&
                        loan.getNextEmiDate().equals(LocalDate.now())){

                    try {
                        log.info("Sending EMI reminder for {}", loan.getAccountNumber());

                        // EMAIL
                        emailService.sendEmail(
                                "sangamprasad1993@gmail.com",
                                "EMI REMINDER",
                                loan.getAccountNumber(),
                                loan.getEmiAmount(),
                                loan.getTotalAmount()
                        );


                        smsService.sendSms(
                                "+919999999999",
                                loan.getAccountNumber(),
                                "EMI REMINDER",
                                loan.getEmiAmount(),
                                loan.getTotalAmount()
                        );

                        // NEXT MONTH
                        loan.setNextEmiDate(
                                loan.getNextEmiDate().plusMonths(1)
                        );

                        repo.save(loan);

                    } catch (Exception e){
                        log.error("Error sending EMI reminder", e);
                    }
                }
            }
        }
    }
