package org.sing_group.piba.domain.dao.patient;

import java.util.List;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Default;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.sing_group.piba.domain.dao.DAOHelper;
import org.sing_group.piba.domain.dao.spi.patient.PatientDAO;
import org.sing_group.piba.domain.entities.patient.Patient;

@Default
@Transactional(value = TxType.MANDATORY)
public class DefaultPatientDAO implements PatientDAO {

  @PersistenceContext
  protected EntityManager em;
  protected DAOHelper<String, Patient> dh;

  public DefaultPatientDAO() {
    super();
  }

  public DefaultPatientDAO(EntityManager em) {
    this.em = em;
    createDAOHelper();
  }

  @PostConstruct
  protected void createDAOHelper() {
    this.dh = DAOHelper.of(String.class, Patient.class, this.em);
  }

  @Override
  public Patient create(Patient patient) {
    return this.dh.persist(patient);
  }

  @Override
  public Stream<Patient> getPatients() {
    return this.dh.list().stream();
  }

  @Override
  public Stream<Patient> searchBy(String value) {
    List<Patient> patients =
      this.em.createQuery("SELECT p FROM Patient p WHERE p.publicID LIKE :value", Patient.class)
        .setParameter("value", value + "%").getResultList();
    return patients.stream();
  }

  @Override
  public Patient get(String id) {
    return this.dh.getBy("id", id);
  }

}
