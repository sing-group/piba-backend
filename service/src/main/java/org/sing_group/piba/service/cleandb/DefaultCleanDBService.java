package org.sing_group.piba.service.cleandb;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Stream;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import org.sing_group.piba.domain.dao.spi.user.UserDAO;
import org.sing_group.piba.domain.entities.passwordrecovery.PasswordRecovery;

@Startup
@Singleton
public class DefaultCleanDBService {

  @Inject
  private UserDAO userDAO;

  @Schedule(dayOfWeek = "*")
  private void cleanDB() {
    Stream<PasswordRecovery> list = userDAO.getAllPasswordRecovery();

    for (PasswordRecovery pass : list.toArray(PasswordRecovery[]::new)) {
      System.out.println(pass.getDate());
      if (pass.getDate().toInstant().plus(24, ChronoUnit.HOURS).isBefore(new Date().toInstant())) {
        this.userDAO.deletePasswordRecovery(pass);
      }
    }
    System.out.println("Db cleaned");
  }

}
