package com.example.arrowdb.auxiliary;

import com.example.arrowdb.entity.Employee;
import com.example.arrowdb.entity.Vocation;
import com.example.arrowdb.services.EmployeeService;
import com.example.arrowdb.services.VocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class EmployeeStatusChanger {

    private final EmployeeService employeeService;
    private final VocationService vocationService;
    private final MailSenderService mailSenderService;

    @Transactional
    @EventListener(ContextRefreshedEvent.class)
    @Scheduled(cron = "0 0 1 * * *") //*/1 * * * * ? //0 0 1 * * ?
    public void schedule() {
        List<Vocation> vocationList = vocationService.findAllVocations();
        for (Vocation vocation : vocationList) {
            if (LocalDate.now().isAfter(vocation.getStartOfVocation().minusDays(1))
                    && LocalDate.now().isBefore(vocation.getEndOfVocation().plusDays(1))) {
                employeeService.changeEmployeeStatusOnVocation();
                break;
            } else {
                employeeService.changeEmployeeStatusOnActive();
            }
        }
    }

    @Transactional
    @Scheduled(cron = "0 0 2 * * *")
    public void sendMailAboutVocation() {
        List<Vocation> vocationList = vocationService.findAllVocations();
        List<Employee> employeeList = employeeService.findEmployeeMailSend();
        for (Vocation vocation : vocationList) {
            if (vocation.getStartOfVocation().equals(LocalDate.now())) {
                for (Employee employee : employeeList) {
                    mailSenderService.send(employee.getEmail(),
                            "Уведомление об отпуске",
                            String.format("Работник %s %s.%s., %s, в период с %s по %s будет находиться в отпуске\n" +
                                            "Заместитель: %s, %s, телефон: %s, e-mail: %s",
                                    vocation.getEmployee().getSurName(),
                                    vocation.getEmployee().getName().charAt(0),
                                    vocation.getEmployee().getMiddleName().charAt(0),
                                    vocation.getEmployee().getProfession().getProfessionName(),
                                    vocation.getStartOfVocation().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                                    vocation.getEndOfVocation().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                                    vocation.getDuringEmployee().toString(),
                                    vocation.getDuringEmployee().getProfession().getProfessionName(),
                                    vocation.getDuringEmployee().getPhoneNumber() != null ?
                                            vocation.getDuringEmployee().getPhoneNumber() : "не указан",
                                    vocation.getDuringEmployee().getEmail() != null ?
                                            vocation.getDuringEmployee().getEmail() : "не указан"));
                }
            }
        }
    }
}